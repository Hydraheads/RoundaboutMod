#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

float LinearizeDepth(in vec2 uv)
{
    float zNear = 0.5;
    float zFar  = 2000.0;
    float depth = texture2D(MainDepthSampler, uv).x;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    float d = LinearizeDepth(texCoord);

    fragColor = vec4(d, d, d, 1.0);
}