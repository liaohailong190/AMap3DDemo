precision highp float;
attribute vec3 aVertex;
uniform vec4 aColor;
uniform mat4 aMVPMatrix;
varying vec4 color;
void main(){
    gl_Position = aMVPMatrix * vec4(aVertex, 1.0);
    color = aColor;
}