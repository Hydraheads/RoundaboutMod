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

void main() {
// This is a shader for epitaph
// Fair warning, shaders use a lot of confusing math
// (So we're doing a relatively simple one)
// It's essential that the shader doesn't make it hard to see, so that was in mind when adjusting values
// The actual effects of entities appearing in different places will use the entityrenderdispatcher or something

    // Warping
    vec2 warped = texCoord;

    warped.x += sin(texCoord.y * 30.0 + GameTime * 0.1) * 0.001;

    vec4 InTexel = texture(DiffuseSampler, warped);

    vec2 PinUnitCoord = texCoord * Two.xy - One.xy;
    float PincushionR2 = pow(length(PinUnitCoord), 2.0);
    vec2 PincushionCurve = PinUnitCoord * PincushionAmount * PincushionR2;
    vec2 ScanCoord = texCoord;

    ScanCoord *= One.xy - PincushionAmount * 0.2;
    ScanCoord += PincushionAmount * 0.1;
    ScanCoord += PincushionCurve;

    vec2 CurvatureClipCurve = PinUnitCoord * CurvatureAmount * PincushionR2;
    vec2 ScreenClipCoord = texCoord;
    ScreenClipCoord -= Half.xy;
    ScreenClipCoord *= One.xy - CurvatureAmount * 0.2;
    ScreenClipCoord += Half.xy;
    ScreenClipCoord += CurvatureClipCurve;

    // -- Alpha Clipping --
float left   = max(0.0, -ScreenClipCoord.x);
float right  = max(0.0, ScreenClipCoord.x - 1.0);
float top    = max(0.0, -ScreenClipCoord.y);
float bottom = max(0.0, ScreenClipCoord.y - 1.0);

float outside = max(max(left, right), max(top, bottom));
float time = (GameTime + PartialTick) - GameTimeStart;
float appear = clamp(time / 8.0, 0.0, 1.0);
appear = appear * appear * (3.0 - 2.0 * appear);

float threshold = (1.0 - appear) * 0.05;
if (outside > threshold)
    discard;

    // -- Scanline Simulation --
    float offset = sin(GameTime * 0.5) * 2.0;
    float InnerSine = ScanCoord.y * InSize.y * ScanlineScale * 0.25 + offset;
    float ScanBrightMod = sin(InnerSine * Pi + ScanlineOffset * InSize.y * 0.25);
    float ScanBrightness = mix(1.0, (pow(ScanBrightMod * ScanBrightMod, ScanlineHeight) * ScanlineBrightScale + 1.0) * 0.5, ScanlineAmount);
    vec3 ScanlineTexel = InTexel.rgb * ScanBrightness;

    // -- Color Compression (increasing the floor of the signal without affecting the ceiling) --
    ScanlineTexel = Floor + (One.xyz - Floor) * ScanlineTexel;

    ScanlineTexel.rgb = pow(ScanlineTexel.rgb, Power);
// Brightness multiplier.

    // -- Color Compression (increasing the floor of the signal without affecting the ceiling) --
    ScanlineTexel = Floor + (One.xyz - Floor) * ScanlineTexel;
    vec3 redTint = vec3(1.0, 0.0, 1.0);
    ScanlineTexel = mix(ScanlineTexel, redTint, 0.1);

    // Pulsing
    float pulse = sin(GameTime * 0.3) * 0.03 + 1.0;
    ScanlineTexel *= pulse;
    fragColor = vec4(ScanlineTexel, 1.0);
}
