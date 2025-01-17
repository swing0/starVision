#version 330

in vec2 outTextCoord;
out vec4 fragColor;

uniform vec4 diffuse;
uniform sampler2D txtSampler;
uniform int hasTexture;
uniform vec3 emissiveColor; // 自发光颜色

void main()
{
    if (hasTexture == 1) {
        fragColor = texture(txtSampler, outTextCoord) + vec4(emissiveColor, 1.0); // 将自发光颜色叠加到纹理颜色上
    } else {
        fragColor = diffuse + vec4(emissiveColor, 1.0); // 将自发光颜色叠加到漫反射颜色上
    }
}