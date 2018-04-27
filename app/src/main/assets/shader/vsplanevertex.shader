precision highp float;

attribute vec3 aVertex;
attribute vec2 vCoordinate;

uniform mat4 aMVPMatrix;

varying vec2 aCoordinate;
void main(){
    aCoordinate = vCoordinate;
    gl_Position = aMVPMatrix * vec4(aVertex, 1.0);
}