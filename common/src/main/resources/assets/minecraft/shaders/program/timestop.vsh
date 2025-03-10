#version 150

in vec3 Position;
uniform mat4 modelViewMatrix;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;
uniform vec3 CameraPosition;

out vec2 texCoord;
out vec4 position;
out vec4 worldCoords;

void main(){
    // Transform local position to world space
    vec4 worldPos = modelViewMatrix * vec4(Position + ChunkOffset, 1.0);

    // Project into screen space
    vec4 outPos = ProjMat * worldPos;
    gl_Position = vec4(outPos.xy, 0.2, 1.0); // Adjust Z value if needed

    // Convert to normalized texture coordinates
    texCoord = outPos.xy * 0.5 + 0.5;
    position = outPos;

    // Compute world coordinates correctly
    worldCoords = vec4(worldPos.xyz + CameraPosition, 1.0);
}