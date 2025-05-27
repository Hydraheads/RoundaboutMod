#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

float LinearizeDepth(in vec2 uv)
{
    float zNear = 0.005;
    float zFar  = 12.0;
    float depth = texture2D(MainDepthSampler, uv).r * 2 - 1;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    float d = LinearizeDepth(texCoord);

    vec2 uv = texCoord;
    uv *= 20.;

    vec2 barb = vec2(mod(uv.x + uv.y, 2.));
    barb = ceil(barb - 1.);

    vec3 yellow = vec3(255.0/255.0, 225.0/255.0, 89.0/255.0);
    vec3 yellowLine = barb.xxx * yellow;

    if (yellowLine == vec3(0.0))
    {
        yellowLine = vec3(1.0);
    }


    fragColor = mix(texture(DiffuseSampler, texCoord), vec4(yellowLine, 1.0), d);
}