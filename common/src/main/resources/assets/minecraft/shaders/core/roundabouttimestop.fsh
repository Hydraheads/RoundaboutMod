#version 150

uniform sampler2D DiffuseSampler;

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 worldCoordinate;

out vec4 fragColor;

vec4 generic_desaturate(vec3 color, float factor)
{
    vec3 lum = vec3(0.299, 0.587, 0.114);
    vec3 gray = vec3(dot(lum, color));
    return vec4(mix(color, gray, factor), 1.0);
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }

    fragColor = generic_desaturate(color.rgb, 1.0);
}