/*
This shader is licensed under CC BY-SA 4.0 (see here: https://creativecommons.org/licenses/by-sa/4.0/deed.en)
Made by GoldenGate9 for Roundabout in 2025
*/

// == World Space Calculation Code == //
// This is the original source that can be copied from shader to shader--but cannot be an include--
// as Minecraft does not support the #moj_import <> directive in post shaders.
// See below for the minified version.

// These are from 1.21, might require tweaking depending on the version!
const float _near = 0.1;
const float _far = 1000.;

float linearizeDepth(float depth)
{
    float z = depth * 2.0 - 1.0;
    return (_near * _far) / (_far + _near - z * (_far - _near));
}

vec3 rotateByQuat(vec3 v, vec4 q)
{
    vec3 t = 2.0 * cross(q.xyz, v);
    return v + q.w * t + cross(q.xyz, t);
}

vec3 getWorldCoord(vec2 uv, float aspect, float fov, vec3 cameraPos, vec4 cameraRot, sampler2D depthSampler, out vec3 rayDir, out float worldDist)
{
    vec2 ndc = vec2(
    uv.x * 2.0 - 1.0,
    1.0 - uv.y * 2.0
    );

    float fovTan = tan(radians(fov)/2.);
    vec3 forward = normalize(rotateByQuat(vec3(0., 0., 1.), cameraRot));
    vec3 up = normalize(rotateByQuat(vec3(0., 1., 0.), cameraRot));
    vec3 right = normalize(cross(forward, up));

    rayDir = normalize(
        forward +
        right * ndc.x * fovTan * aspect +
        up * (-ndc.y) * fovTan
    );

    float depth = linearizeDepth(texture(depthSampler, uv).r);
    worldDist = length(vec3(1., (2.*uv - 1.) * vec2(aspect, 1.) * tan(radians(fov / 2.))) * depth);
    // Cast a ray from our camera to pixel, see where it lands in the world. Not a perfect replication of screenspace -> worldspace, but a good enough one at that! (and also cheap!)
    return cameraPos+rayDir*worldDist;
}
// == End == //

// Minified Version, this should be copied from source to source with a comment saying its from lib/worldspace.fsh
const float _near=.1;const float _far=1000.;float linearizeDepth(float b){float c=b*2.-1.;return(_near*_far)/(_far+_near-c*(_far-_near));}vec3 rotateByQuat(vec3 d,vec4 e){vec3 f=2.*cross(e.xyz,d);return d+e.w*f+cross(e.xyz,f);}vec3 getWorldCoord(vec2 g,float h,float i,vec3 j,vec4 k,sampler2D l,out vec3 m,out float n){vec2 o=vec2(g.x*2.-1.,1.-g.y*2.);float p=tan(radians(i)/2.);vec3 q=normalize(rotateByQuat(vec3(0.,0.,1.),k));vec3 s=normalize(rotateByQuat(vec3(0.,1.,0.),k));vec3 t=normalize(cross(q,s));m=normalize(q+t*o.x*p*h+s*(-o.y)*p);float u=linearizeDepth(texture(l,g).r);n=length(vec3(1.,(2.*g-1.)*vec2(h,1.)*tan(radians(i/2.)))*u);return j+m*n;}