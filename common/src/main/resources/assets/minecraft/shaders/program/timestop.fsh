#version 150

uniform sampler2D DepthSampler;  // Depth buffer
uniform sampler2D DiffuseSampler;
uniform mat4 ProjMat;      // Inverse projection matrix
uniform mat4 modelViewMatrix;            // Inverse view matrix (camera transform)

in vec2 texCoord;
out vec4 fragColor;

vec3 decodeLocation()
{
    vec4 clipSpaceLocation;
    clipSpaceLocation.xy = texCoord * 2.0f - 1.0f;
    clipSpaceLocation.z = texture(DepthSampler, texCoord).r * 2.0f - 1.0f;
    clipSpaceLocation.w = 1.0f;
    vec4 homogenousLocation = ProjMat * clipSpaceLocation;
    return homogenousLocation.xyz / homogenousLocation.w;
}

void main() {
    vec3 pixelLoc = decodeLocation();

    fragColor = vec4(vec3(distance(pixelLoc, vec3(-161, 76, 90))/50), 1.0);
}
