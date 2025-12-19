#version 330

// The multiplier to the color desaturation. From 0.0 (not desaturated) to 1.0 (completely desaturated).
#define DESATURATION 0.8
// Comment this out if you don't want a bubble but rather just make the terrain desaturated.
#define BUBBLE

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
uniform vec4 CameraPos;
uniform vec4 CameraRot;
uniform vec3 BubblePos;
uniform float BubbleRadius;
uniform vec3 BubbleTint;

in vec2 texCoord;
out vec4 fragColor;

// These are from 1.21, might require tweaking depending on the version!
const float _near = 0.1;
const float _far = 1000.;

float linearizeDepth(float depth)
{
    float z = depth * 2.0 - 1.0;
    return (_near * _far) / (_far + _near - z * (_far - _near));
}

vec3 rotateByQuat(vec3 v, vec4 q)
{
    vec3 t = 2.0 * cross(q.xyz, v);
    return v + q.w * t + cross(q.xyz, t);
}

vec4 generic_desaturate(vec3 color, float factor)
{
    vec3 lum = vec3(0.299, 0.587, 0.114);
    vec3 gray = vec3(dot(lum, color));
    return vec4(mix(color, gray, factor), 1.0);
}

void main() {
    ivec2 resolution = textureSize(DiffuseSampler, 0);
    float aspect = float(resolution.x) / float(resolution.y);

    vec4 col = texture(DiffuseSampler, texCoord);

    vec3 camPos = CameraPos.xyz;

    vec2 ndc = vec2(
        texCoord.x * 2.0 - 1.0,
        1.0 - texCoord.y * 2.0
    );

    float FOV = CameraPos.w;
    float fovTan = tan(radians(FOV)/2.);
    vec3 forward = normalize(rotateByQuat(vec3(0., 0., 1.), CameraRot));
    vec3 up = normalize(rotateByQuat(vec3(0., 1., 0.), CameraRot));
    vec3 right = normalize(cross(forward, up));

    vec3 rayDir = normalize(
        forward +
        right * ndc.x * fovTan * aspect +
        up * (-ndc.y) * fovTan
    );

    vec3 rayPos = camPos;
    bool hit = false;
#ifdef BUBBLE
    // Raymarch the physical bubble (WARNING: expensive!)
    for (int i = 0; i < 64; i++)
    {
        float d = length(rayPos-BubblePos)-BubbleRadius;
        rayPos += rayDir*d;
        if (d < 0.01)
        {
            hit = true;
            break;
        }
    }
#endif

    float depth = linearizeDepth(texture(MainDepthSampler, texCoord).r);
    float worldDist = length(vec3(1., (2.*texCoord - 1.) * vec2(aspect, 1.) * tan(radians(FOV / 2.))) * depth);
    // Cast a ray from our camera to pixel, see where it lands in the world. Not a perfect replication of screenspace -> worldspace, but a good enough one at that! (and also cheap!)
    vec3 worldPos = camPos+rayDir*worldDist;

    float bubbleDist = distance(worldPos, BubblePos);
    if (bubbleDist < BubbleRadius
#ifdef BUBBLE
        || (hit && distance(rayPos, camPos) < worldDist)
#endif
    )
    {
        col.rgb = generic_desaturate(col.rgb, DESATURATION).rgb*BubbleTint;
#ifdef BUBBLE
        // Add the fresnel effect
        vec3 normal = normalize(BubblePos-rayPos);
        col.rgb += pow(1.-clamp(dot(normal, rayDir), 0., 1.), BubbleRadius-5.);
#endif
    }

    fragColor = col;
}