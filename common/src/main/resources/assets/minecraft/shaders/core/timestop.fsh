#version 150

in vec2 texCoord;
in vec4 vertexColor;

out vec4 fragColor;

uniform sampler2D DiffuseSampler;

uniform vec4 ColorModulator;

void main() {
	vec4 texColor = texture(DiffuseSampler, texCoord);

	vec4 finalColor = texColor * vertexColor * ColorModulator;

	float gray = dot(finalColor.rgb, vec3(0.299, 0.587, 0.114));
	fragColor = vec4(vec3(gray), finalColor.a);
}
