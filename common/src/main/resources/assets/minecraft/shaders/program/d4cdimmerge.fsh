#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float GameTime;
uniform float GameTimeStart;
uniform float PartialTick;

const vec4 Zero = vec4(0.0);
const vec4 Half = vec4(0.5);
const vec4 One = vec4(1.0);
const vec4 Two = vec4(2.0);

const float Pi = 3.1415926535;
const float PincushionAmount = 0.02;
const float CurvatureAmount = 0.02;
const float ScanlineAmount = 0.8;
const float ScanlineScale = 1.0;
const float ScanlineHeight = 1.0;
const float ScanlineBrightScale = 1.0;
const float ScanlineBrightOffset = 0.0;
const float ScanlineOffset = 0.0;
const vec3 Floor = vec3(0.05, 0.05, 0.05);
const vec3 Power = vec3(0.8, 0.8, 0.8);

out vec4 fragColor;



vec3 hueShift(vec3 color, float angle) {
    float s = sin(angle);
    float c = cos(angle);

    mat3 rotation = mat3(
        0.299 + 0.701 * c + 0.168 * s,
        0.587 - 0.587 * c + 0.330 * s,
        0.114 - 0.114 * c - 0.497 * s,

        0.299 - 0.299 * c - 0.328 * s,
        0.587 + 0.413 * c + 0.035 * s,
        0.114 - 0.114 * c + 0.292 * s,

        0.299 - 0.300 * c + 1.250 * s,
        0.587 - 0.588 * c - 1.050 * s,
        0.114 + 0.886 * c - 0.203 * s
    );

    return rotation * color;
}
void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    // Create a moderately shifted version
    vec3 shifted = hueShift(original.rgb, 0.7);

    // Only partially apply the shift
    vec3 color = mix(
        original.rgb,
        shifted,
        0.45
    );

    // Calculate brightness
    float gray = dot(
        color,
        vec3(0.299, 0.587, 0.114)
    );

    // Reduce saturation for softer/pastel colors
    color = mix(
        vec3(gray),
        color,
        0.72
    );

    fragColor = vec4(
        clamp(color, 0.0, 1.0),
        original.a
    );
}
