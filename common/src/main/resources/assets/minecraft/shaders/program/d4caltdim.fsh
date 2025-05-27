#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
uniform float FrameCount;

in vec2 texCoord;
out vec4 fragColor;

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0., -1./3., 2./3., -1.);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.*d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1., 2./3., 1./3., 3.);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6. - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0., 1.), c.y);
}

float vignetteSquare(vec2 uv, float size) {
    vec2 dist = abs(uv - 0.5);
    return smoothstep(size, 0.5, max(dist.x, dist.y));
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    // Animated hue shift
    float hueShift = mod(FrameCount / 300.0, 1.0); // full hue cycle every 300 frames
    vec3 hsv = rgb2hsv(color.rgb);
    hsv.x = mod(hsv.x + hueShift, 1.0);
    vec3 shiftedColor = hsv2rgb(hsv);

    // Vignette blend
    float vignette = vignetteSquare(texCoord, 0.35);
    vec3 finalColor = mix(color.rgb, shiftedColor, vignette);

    fragColor = vec4(finalColor, color.a);
}