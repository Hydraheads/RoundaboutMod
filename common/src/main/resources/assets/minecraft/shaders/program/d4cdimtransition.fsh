#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

float LinearizeDepth(in vec2 uv)
{
    float zNear = 150.0;
    float zFar  = 2000.0;
    float depth = texture2D(MainDepthSampler, uv).x;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    float d = LinearizeDepth(texCoord);

    vec2 uv = texCoord;
    uv *= 20.;

    vec2 barb = vec2(mod(uv.x + uv.y, 2.));
    barb = ceil(barb - 1.);

    vec3 col = barb.xxx;

    fragColor = mix(texture(DiffuseSampler, texCoord), vec4(col, 1.0) * vec4(255.0/255.0, 251.0/255.0, 0.0, 1.0), d);
}