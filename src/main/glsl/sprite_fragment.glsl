#version 300 es

precision mediump float;

uniform vec4 color;
uniform sampler2D texture;

in vec2 vTexCoord;

out vec4 fragColor;

void main() {

//    fragColor = vec4(1.0, 1.0, 1.0, 1.0);
    if(vTexCoord == vec2(0.0, 0.0)) {
        fragColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
    else {
        fragColor = texture2D(texture, vTexCoord);
    }
}
