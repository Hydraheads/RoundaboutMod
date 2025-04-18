#version 150

uniform sampler2D DiffuseSampler;

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 worldCoordinate;

out vec4 fragColor;

void main()
{
    fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}