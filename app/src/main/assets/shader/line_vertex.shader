precision highp float;
attribute vec3 aVertex;
uniform mat4 aMVPMatrix;
uniform vec4 aColor;
varying vec4 color;
void main(){
    gl_Position = aMVPMatrix * vec4(aVertex, 1.0);
    color = aColor;
}