/*
This shader is licensed under CC BY-SA 4.0 (see here: https://creativecommons.org/licenses/by-sa/4.0/deed.en)
Made by GoldenGate9 for Roundabout in 2025
*/

#version 330

// See lib/worldspace.fsh for the origin of this line
const float _near=.1;const float _far=1000.;float linearizeDepth(float b){float c=b*2.-1.;return(_near*_far)/(_far+_near-c*(_far-_near));}vec3 rotateByQuat(vec3 d,vec4 e){vec3 f=2.*cross(e.xyz,d);return d+e.w*f+cross(e.xyz,f);}vec3 getWorldCoord(vec2 g,float h,float i,vec3 j,vec4 k,sampler2D l,out vec3 m,out float n){vec2 o=vec2(g.x*2.-1.,1.-g.y*2.);float p=tan(radians(i)/2.);vec3 q=normalize(rotateByQuat(vec3(0.,0.,1.),k));vec3 s=normalize(rotateByQuat(vec3(0.,1.,0.),k));vec3 t=normalize(cross(q,s));m=normalize(q+t*o.x*p*h+s*(-o.y)*p);float u=linearizeDepth(texture(l,g).r);n=length(vec3(1.,(2.*g-1.)*vec2(h,1.)*tan(radians(i/2.)))*u);return j+m*n;}

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
uniform float BubbleMaxRadius;
uniform vec3 BubbleTint;
uniform float DesaturateAllInside;
uniform float GroundLinesOpacity;

in vec2 texCoord;
out vec4 fragColor;

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

    bool desaturateAll = (DesaturateAllInside > 0.5);

    vec3 camPos = CameraPos.xyz;
    float FOV = CameraPos.w;
    vec3 rayDir;
    float worldDist;
    vec3 worldPos = getWorldCoord(texCoord, aspect, FOV, camPos, CameraRot, MainDepthSampler, rayDir, worldDist);

    float bubbleRadius = BubbleRadius/BubbleMaxRadius;
    bubbleRadius = 1.-pow(1.-bubbleRadius, 3);
    bubbleRadius *= BubbleMaxRadius;

    vec3 rayPos = camPos;
    bool hit = false;

    bool camInside = (distance(camPos, BubblePos) <= bubbleRadius);
    if (!camInside)
    {
#ifdef BUBBLE
        // Raymarch the physical bubble (WARNING: expensive!)
        for (int i = 0; i < 64; i++)
        {
            float d = length(rayPos-BubblePos)-bubbleRadius;
            rayPos += rayDir*d;
            if (d < 0.01)
            {
                if (distance(rayPos, camPos) < worldDist)
                {
                    col.rgb = generic_desaturate(col.rgb, DESATURATION).rgb*BubbleTint;

                    // Add the fresnel effect
                    vec3 normal = normalize(BubblePos-rayPos);
                    col.rgb += pow(1.-clamp(dot(normal, rayDir), 0., 1.), 5.);
                }

                hit = true;
                break;
            }
        }
#endif
    }
    else {
        if (desaturateAll)
        {
            col.rgb = generic_desaturate(col.rgb, DESATURATION).rgb*BubbleTint;
        }
    }

    float bubbleDist = distance(worldPos, BubblePos);
    if (bubbleDist < bubbleRadius)
    {
        if (!(camInside && desaturateAll))
            col.rgb = generic_desaturate(col.rgb, DESATURATION).rgb*BubbleTint;

        float edge = smoothstep(0., 1., clamp(bubbleDist/bubbleRadius, 0., 1.));
        col.rgb += clamp(pow(edge, bubbleRadius*2.), 0., 1.)*GroundLinesOpacity;
    }

    fragColor = col;
}