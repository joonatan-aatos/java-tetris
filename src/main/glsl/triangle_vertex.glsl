#version 300 es

uniform float angle;

layout (location=0) in vec3 position;

void main() {
    mat2 rotationMatrix;
    rotationMatrix[0][0] = cos(angle);
    rotationMatrix[0][1] = sin(angle);
    rotationMatrix[1][0] = -sin(angle);
    rotationMatrix[1][1] = cos(angle);

    vec2 newPosition = rotationMatrix * vec2(position.x, position.y);
    gl_Position = vec4(newPosition, position.z, 1.0);
} 
