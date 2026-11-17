#version 150
in vec3 Position;
in vec4 Color;
in vec2 UV0;
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 AuraLocalFromView;
uniform float MaskMode;
out vec3 fieldPosition;
out vec4 vertexColor;
out vec2 texCoord;
void main(){gl_Position=MaskMode>2.5?vec4(Position.xy,0.0,1.0):ProjMat*ModelViewMat*vec4(Position,1.0);fieldPosition=(AuraLocalFromView*vec4(Position,1.0)).xyz;vertexColor=Color;texCoord=UV0;}
