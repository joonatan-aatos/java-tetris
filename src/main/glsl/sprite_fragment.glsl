#version 300 es

precision mediump float;

uniform vec4 color;
uniform sampler2D texture;

in vec2 vTexCoord;

out vec4 fragColor;

void main() {

    fragColor = texture2D(texture, vTexCoord);
}
