#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;

uniform float FogDensity;
uniform float FogNear;
uniform float FogFar;
uniform vec3 FogColor;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    float depth = texture(MainDepthSampler, texCoord).r * 2 - 1;
    float linearizeDepth = (FogNear * FogFar) / (depth * (FogNear - FogFar) + FogFar) / FogFar;
    fragColor = mix(texture(DiffuseSampler, texCoord), vec4(FogColor, 1.0), linearizeDepth * FogDensity);
}