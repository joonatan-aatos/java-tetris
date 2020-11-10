#version 300 es

precision mediump float;

uniform vec4 color1;
uniform vec4 color2;
uniform vec4 color3;
uniform vec4 color4;
uniform vec4 color5;

uniform sampler2D texture;

in vec2 vTexCoord;

out vec4 fragColor;

void main() {

    vec4 mappedColor = texture2D(texture, vTexCoord);

    if(mappedColor == vec4(1.0, 1.0, 1.0, 1.0)) {
        mappedColor = color1;
    }
    else if(mappedColor == vec4(0.8, 0.8, 0.8, 1.0)) {
        mappedColor = color2;
    }
    else if(mappedColor == vec4(0.6, 0.6, 0.6, 1.0)) {
        mappedColor = color3;
    }
    else if(mappedColor == vec4(0.4, 0.4, 0.4, 1.0)) {
        mappedColor = color4;
    }
    else if(mappedColor == vec4(0.2, 0.2, 0.2, 1.0)) {
        mappedColor = color5;
    }

    fragColor = mappedColor;
}
