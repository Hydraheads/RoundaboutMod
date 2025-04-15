/* Shader by NotSoGreeeen for Roundabout */

/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <https://unlicense.org/>

That being said; if you'd be willing to credit me, that'd be very nice :D
*/

#version 150

uniform sampler2D DiffuseSampler;

in vec4 vertexColor;
in vec2 texCoord0;
in vec4 worldCoordinate;

out vec4 fragColor;

void main()
{
    vec2 uv = texCoord0;

    //times it will iterate
    int iterations = 5;

    //set up a base color to "subtract" further iteratsion from
    vec3 col = vec3(1.);

    for (int i = 1; i < iterations; i++) {
        //break every "square" into 9 smaller squares
        uv *= 3.;
        //calculate a "every other" patter using the "ids" of the uv cells
        col *= mod(floor(uv.xyy + 2.), 3.);
        //make that white
        col = vec3(length(col) * 2.);
    }

    // Output to screen
    fragColor = vec4(col, 1.0);
}