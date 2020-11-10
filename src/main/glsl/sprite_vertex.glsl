#version 300 es

precision mediump float;

uniform float angle;
uniform float scale;
uniform vec2 offset;

out vec2 vTexCoord;

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 aTexCoord;

void main() {

    mat2 rotationMatrix;
    rotationMatrix[0][0] = cos(angle);
    rotationMatrix[0][1] = sin(angle);
    rotationMatrix[1][0] = -sin(angle);
    rotationMatrix[1][1] = cos(angle);

    mat2 scaleMatrix;
    scaleMatrix[0][0] = scale;
    scaleMatrix[0][1] = 0.0;
    scaleMatrix[1][0] = 0.0;
    scaleMatrix[1][1] = scale;

    vec2 newPosition = rotationMatrix * vec2(position.x, position.y);
    newPosition = scaleMatrix * newPosition;
    newPosition = newPosition + offset;

    gl_Position = vec4(newPosition, position.z, 1.0);

    vTexCoord = newPosition;
}
