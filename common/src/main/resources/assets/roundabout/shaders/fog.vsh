#version 330
#extension GL_ARB_explicit_attrib_location : enable

in vec2 Position;
in vec2 TexCoord;
in vec4 Color;

uniform mat4 u_projView;

out vec4 vertexColor;
out vec2 texCoord;

void main()
{
    gl_Position = u_projView * vec4(Position, 0.0, 1.0);
    texCoord = TexCoord;
    vertexColor = Color;
}