#version 150

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

uniform sampler2D DiffuseSampler;

void main() {
	vec4 texColor = texture(DiffuseSampler, texCoord);

	fragColor = mix(texColor, vec4(1.0, 0.0, 0.0, 1.0), 0.5);
}
