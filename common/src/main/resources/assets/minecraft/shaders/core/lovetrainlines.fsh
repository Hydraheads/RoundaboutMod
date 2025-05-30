#version 150

uniform sampler2D DiffuseSampler;
uniform float FrameCount;

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 worldCoordinate;

out vec4 fragColor;

void main() {
    vec2 uv = worldCoordinate.xy * 0.5;

    float speed = 0.005;
    vec2 offset = vec2(FrameCount * speed);

    uv = uv + offset;

    float angle = radians(2.0);
    mat2 rot = mat2(cos(angle), -sin(angle),
    sin(angle),  cos(angle));
    uv = rot * uv;

    vec2 uvScaled = uv * 20.0;

    float stripeCoord = fract(uvScaled.x / 2.0);
    float distanceFromStripeCenter = abs(stripeCoord - 0.5);

    float stripeWidth = 0.4;
    float stripeMask = smoothstep(0.5 - stripeWidth, 0.5, 0.5 - distanceFromStripeCenter);

    vec3 baseStripeColor = vec3(0.94, 0.85, 0.32);
    float brightness = mix(0.8, 1.5, stripeMask);
    vec3 stripeColor = baseStripeColor * brightness;

    float stripeAlpha = stripeMask;

    vec3 bgColor = vec3(0.95, 0.76, 0.31);
    float bgAlpha = 0.3;

    vec3 finalColor = mix(bgColor, stripeColor, stripeMask);
    float finalAlpha = mix(bgAlpha, 1.0, stripeMask);

    fragColor = vec4(finalColor, finalAlpha);
}