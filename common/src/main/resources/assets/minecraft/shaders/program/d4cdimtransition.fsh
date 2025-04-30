#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

in vec2 texCoord;
out vec4 fragColor;

void main() {
//    float depth = texture(MainDepthSampler, texCoord).r;
//
//    if (depth == 1.0)
//    {
//        discard;
//    }

    fragColor = texture(DiffuseSampler, texCoord) * vec4(0.5, 0.0, 0.0, 1.0);
}