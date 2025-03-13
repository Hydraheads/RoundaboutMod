#version 330
#extension GL_ARB_explicit_attrib_location : enable

uniform sampler2D DiffuseSampler;

layout(location = 0) out vec4 fragColor;

void main() {
    fragColor = vec4(0.2, 0.3, 0.3, 1.0);
}