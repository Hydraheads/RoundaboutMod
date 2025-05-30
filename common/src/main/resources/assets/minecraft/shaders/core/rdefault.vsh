#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 CameraPosition;
uniform vec3 ChunkOffset;

out vec4 vertexColor;
out vec2 texCoord0;
out vec4 worldCoordinate;

void main() {
    vec4 worldPos = vec4(Position + ChunkOffset + CameraPosition, 1.0);

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    texCoord0 = UV0;
    worldCoordinate = worldPos;
}