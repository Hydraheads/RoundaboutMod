#version 150
uniform sampler2D SceneColor;
uniform sampler2D SceneDepth;
uniform vec2 ScreenSize;
uniform float WarpTime;
uniform float MaskMode;
uniform mat4 AuraLocalFromView;
uniform mat4 FieldInverseProjection;
uniform mat4 FieldProjection;
uniform vec3 FieldCenterView;
uniform vec3 FieldRadii;
uniform float FieldStrength;
uniform float BoundaryCount;
uniform vec4 BoundarySphere0;
uniform float BoundaryFade0;
uniform vec4 BoundarySphere1;
uniform float BoundaryFade1;
uniform vec4 BoundarySphere2;
uniform float BoundaryFade2;
uniform vec4 BoundarySphere3;
uniform float BoundaryFade3;
uniform vec4 BoundarySphere4;
uniform float BoundaryFade4;
uniform vec4 BoundarySphere5;
uniform float BoundaryFade5;
uniform vec4 BoundarySphere6;
uniform float BoundaryFade6;
uniform vec4 BoundarySphere7;
uniform float BoundaryFade7;
uniform vec4 BoundarySphere8;
uniform float BoundaryFade8;
uniform vec4 BoundarySphere9;
uniform float BoundaryFade9;
uniform vec4 BoundarySphere10;
uniform float BoundaryFade10;
uniform vec4 BoundarySphere11;
uniform float BoundaryFade11;
uniform vec4 BoundarySphere12;
uniform float BoundaryFade12;
uniform vec4 BoundarySphere13;
uniform float BoundaryFade13;
uniform vec4 BoundarySphere14;
uniform float BoundaryFade14;
uniform vec4 BoundarySphere15;
uniform float BoundaryFade15;
in vec3 fieldPosition;
uniform vec3 AuraEye;
in vec4 vertexColor;
in vec2 texCoord;
out vec4 fragColor;
float hash(vec3 p){p=fract(p*.1031);p+=dot(p,p.yzx+33.33);return fract((p.x+p.y)*p.z);}
float cmoonNoise3(vec3 p){vec3 i=floor(p),f=fract(p);f=f*f*(3.-2.*f);return mix(mix(mix(hash(i),hash(i+vec3(1,0,0)),f.x),mix(hash(i+vec3(0,1,0)),hash(i+vec3(1,1,0)),f.x),f.y),mix(mix(hash(i+vec3(0,0,1)),hash(i+vec3(1,0,1)),f.x),mix(hash(i+vec3(0,1,1)),hash(i+vec3(1,1,1)),f.x),f.y),f.z);}
vec3 viewPoint(vec2 uv, float depth) {
    vec4 p = FieldInverseProjection * vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    return p.xyz / p.w;
}
void ownerField(vec2 screen) {
    vec3 ray = normalize(viewPoint(screen, 0.9999));
    vec3 origin = (AuraLocalFromView * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    vec3 direction = mat3(AuraLocalFromView) * ray;
    vec3 o = origin / FieldRadii;
    vec3 d = direction / FieldRadii;
    float a = dot(d, d), b = dot(o, d), c = dot(o, o) - 1.0;
    float disc = b * b - a * c;
    if (disc <= 0.0 || FieldStrength <= 0.0) discard;
    float nearT = max(0.0, (-b - sqrt(disc)) / a);
    float farT = (-b + sqrt(disc)) / a;
    float sceneT = length(viewPoint(screen, texture(SceneDepth, screen).r));
    float endT = min(farT, sceneT);
    if (endT <= nearT) discard;
    float closestT = clamp(-b / a, nearT, endT);
    vec3 p = origin + direction * closestT;
    vec3 q = p / FieldRadii;
    float mask = pow(max(0.0, 1.0 - dot(q, q)), 1.35);
    mask *= smoothstep(0.0, 0.45, endT - nearT);
    mask *= smoothstep(0.6, 1.15, length(o));
    if (mask < 0.001) discard;
    float t = WarpTime * 0.045;
    vec4 centerClip = FieldProjection * vec4(FieldCenterView, 1.0);
    vec2 centerScreen = abs(centerClip.w) > 0.001
            ? centerClip.xy / centerClip.w * 0.5 + 0.5 : vec2(0.5);
    vec2 radialDelta = screen - centerScreen;
    vec2 radial = radialDelta / max(length(radialDelta), 0.0001);
    float pressure = sin(p.y * 3.2 - t) + 0.35 * sin(p.z * 2.4 + t * 0.7);
    vec2 flow = radial * pressure + 0.6 * vec2(sin(p.y * 2.7 + t), cos(p.x * 2.9 - t));
    float angularRadius = abs(FieldProjection[1][1]) * FieldRadii.y
            / max(length(FieldCenterView), 0.001);
    float distanceScale = min(1.0, angularRadius / 0.4);
    float pixels = min(12.0, 8.0 * ScreenSize.y / 720.0) * distanceScale;
    vec2 shifted = clamp(screen + flow * pixels * mask * FieldStrength / ScreenSize,
            vec2(0.5) / ScreenSize, vec2(1.0) - vec2(0.5) / ScreenSize);
    vec4 entryClip = FieldProjection * vec4(ray * nearT, 1.0);
    float entryDepth = nearT > 0.001 ? entryClip.z / entryClip.w * 0.5 + 0.5 : 0.0;
    if (texture(SceneDepth, shifted).r + 0.000002 < entryDepth) shifted = screen;
    vec3 refracted = texture(SceneColor, shifted).rgb;
    vec2 echoUv = clamp(screen + (shifted-screen)*1.6, vec2(0.5)/ScreenSize,
            vec2(1.0)-vec2(0.5)/ScreenSize);
    if (texture(SceneDepth, echoUv).r + 0.000002 < entryDepth) echoUv = shifted;
    refracted = mix(refracted, texture(SceneColor, echoUv).rgb, 0.16);
    float phase = q.y*6.5 + q.x*2.8 + 0.8*sin(q.z*2.2-t*0.4) - t*0.65;
    float crest = pow(0.5+0.5*cos(phase), 20.0);
    float trough = pow(0.5+0.5*cos(phase-0.55), 16.0);
    float chromaticCrest = smoothstep(0.2, 0.9, 0.5 + 0.5 * cos(phase));
    float chromaticPixels = min(5.0, 4.0 * ScreenSize.y / 1080.0)
            * distanceScale * sqrt(mask) * FieldStrength * chromaticCrest;
    vec2 chromaticDirection = flow / max(length(flow), 0.001);
    vec2 chromaticOffset = chromaticDirection * chromaticPixels / ScreenSize;
    vec2 redUv = clamp(shifted + chromaticOffset,
            vec2(0.5) / ScreenSize, vec2(1.0) - vec2(0.5) / ScreenSize);
    vec2 blueUv = clamp(shifted - chromaticOffset,
            vec2(0.5) / ScreenSize, vec2(1.0) - vec2(0.5) / ScreenSize);
    if (texture(SceneDepth, redUv).r + 0.000002 < entryDepth) redUv = shifted;
    if (texture(SceneDepth, blueUv).r + 0.000002 < entryDepth) blueUv = shifted;
    vec3 baseSample = texture(SceneColor, shifted).rgb;
    refracted.r += texture(SceneColor, redUv).r - baseSample.r;
    refracted.b += texture(SceneColor, blueUv).b - baseSample.b;
    float relief = (0.13*crest - 0.045*trough)*mask*FieldStrength;
    fragColor = vec4(clamp(refracted+vec3(relief), 0.0, 1.0), 0.9 * FieldStrength);
}
void boundaryField(vec2 screen) {
    vec3 ray = normalize(viewPoint(screen, 0.9999));
    vec4 spheres[16] = vec4[16](BoundarySphere0, BoundarySphere1, BoundarySphere2, BoundarySphere3, BoundarySphere4, BoundarySphere5, BoundarySphere6, BoundarySphere7, BoundarySphere8, BoundarySphere9, BoundarySphere10, BoundarySphere11, BoundarySphere12, BoundarySphere13, BoundarySphere14, BoundarySphere15);
    float fades[16] = float[16](BoundaryFade0, BoundaryFade1, BoundaryFade2, BoundaryFade3, BoundaryFade4, BoundaryFade5, BoundaryFade6, BoundaryFade7, BoundaryFade8, BoundaryFade9, BoundaryFade10, BoundaryFade11, BoundaryFade12, BoundaryFade13, BoundaryFade14, BoundaryFade15);
    int count = int(BoundaryCount);
    float nearest = 1e20;
    int selected = -1;
    float sceneT = length(viewPoint(screen, texture(SceneDepth, screen).r));
    for (int i = 0; i < 16; i++) {
        if (i >= count) break;
        vec3 center = spheres[i].xyz;
        float radius = spheres[i].w;
        float along = dot(ray, center);
        float disc = along * along - dot(center, center) + radius * radius;
        if (disc <= 0.0 || fades[i] < 0.001) continue;
        float root = sqrt(disc);
        for (int face = 0; face < 2; face++) {
            float t = along + (face == 0 ? -root : root);
            if (t < 0.08 || t >= nearest || t > sceneT) continue;
            vec3 point = ray * t;
            bool covered = false;
            for (int j = 0; j < 16; j++) {
                if (j >= count) break;
                if (j == i || fades[j] < 0.01) continue;
                float innerRadius = max(0.0, spheres[j].w - 0.04);
                vec3 other = point - spheres[j].xyz;
                if (dot(other, other) < innerRadius * innerRadius) { covered = true; break; }
            }
            if (!covered) { nearest = t; selected = i; }
        }
    }
    if (selected < 0) discard;
    vec3 point = ray * nearest;
    vec3 normal = normalize(point - spheres[selected].xyz);
    float rim = pow(1.0 - abs(dot(normal, ray)), 1.6);
    float t = WarpTime * 0.025;
    float noise = cmoonNoise3(point * 0.22 + vec3(t * 0.13, -t * 0.08, t * 0.1));
    float strength = fades[selected] * (0.45 + 0.55 * rim) * (0.75 + 0.25 * noise);
    float angular = abs(FieldProjection[1][1]) * spheres[selected].w / max(length(spheres[selected].xyz), 1.0);
    float distanceScale = min(1.0, angular / 0.35);
    float pixels = min(10.0, 8.0 * ScreenSize.y / 1080.0) * distanceScale;
    vec2 flow = normal.xy * 0.65 + vec2(sin(point.y * 0.23 + t), cos(point.x * 0.19 - t * 0.8)) * 0.55;
    vec2 texel = vec2(0.5) / ScreenSize;
    vec2 shifted = clamp(screen + flow * pixels * strength / ScreenSize, texel, vec2(1.0) - texel);
    vec4 boundaryClip = FieldProjection * vec4(point, 1.0);
    float boundaryDepth = boundaryClip.z / boundaryClip.w * 0.5 + 0.5;
    if (texture(SceneDepth, shifted).r + 0.000002 < boundaryDepth) shifted = screen;
    vec2 split = normalize(flow + vec2(0.00001)) * min(3.2, 2.4 * ScreenSize.y / 1080.0) * strength * distanceScale / ScreenSize;
    vec2 redUv = clamp(shifted + split, texel, vec2(1.0) - texel);
    vec2 blueUv = clamp(shifted - split, texel, vec2(1.0) - texel);
    if (texture(SceneDepth, redUv).r + 0.000002 < boundaryDepth) redUv = shifted;
    if (texture(SceneDepth, blueUv).r + 0.000002 < boundaryDepth) blueUv = shifted;
    vec3 color = vec3(texture(SceneColor, redUv).r, texture(SceneColor, shifted).g, texture(SceneColor, blueUv).b);
    float sheen = (0.008 + 0.02 * rim) * strength;
    fragColor = vec4(clamp(color + vec3(sheen), 0.0, 1.0), 0.88 * fades[selected]);
}
void main() {
 vec2 screen = gl_FragCoord.xy / ScreenSize;
 if (MaskMode > 3.5) { boundaryField(screen); return; }
 if (MaskMode > 2.5) { ownerField(screen); return; }
 float edge = sin(clamp(texCoord.x,0.0,1.0)*3.14159265) * sin(clamp(texCoord.y,0.0,1.0)*3.14159265);
 if (MaskMode > 1.5) {
   vec3 n=normalize(cross(dFdx(fieldPosition),dFdy(fieldPosition)));
   vec3 v=normalize(AuraEye-fieldPosition);
   float rim=pow(1.-abs(dot(n,v)),2.5);
   float t=WarpTime*6.2831853/110.;
   vec3 drift=vec3(.16*sin(t),.12*cos(t),.14*sin(t+.7));
   vec3 p=fieldPosition*vec3(3.,2.5,3.)+drift;
   float pattern=.72*cmoonNoise3(p)+.28*cmoonNoise3(p*2.05+vec3(4.1,1.3,2.8));
   float veil=smoothstep(.43,.61,pattern);
   float alpha=min(.31,vertexColor.a*(.13+.18*rim)*veil);
   if(alpha<.0005)discard;
   vec2 offset=vec2(n.x,n.y)*1.8*vertexColor.a*veil/ScreenSize;
   vec2 shifted=clamp(screen+offset,vec2(.001),vec2(.999));
   if(texture(SceneDepth, shifted).r+.00002<gl_FragCoord.z) shifted = screen;
   vec3 scene=texture(SceneColor,shifted).rgb;
   fragColor=vec4(mix(scene,vertexColor.rgb,.64),alpha);
 } else {
   float mask = mix(edge*edge, 1.0, MaskMode);
   float wave = sin(gl_FragCoord.y*.075+WarpTime*.19);
   vec2 offset = vec2(wave,cos(gl_FragCoord.x*.05-WarpTime*.14))*(1.4+MaskMode)*mask/ScreenSize;
   vec3 sampleColor = texture(SceneColor,clamp(screen+offset,vec2(.001),vec2(.999))).rgb;
   fragColor = vec4(sampleColor,clamp(vertexColor.a*mask,0.0,.65));
 }
}
