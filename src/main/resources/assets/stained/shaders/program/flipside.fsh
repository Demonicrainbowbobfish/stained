#version 150

uniform sampler2D DiffuseSampler;
uniform float Progress;

in vec2 texCoord;

out vec4 fragColor;

void main() {

    vec2 uv = texCoord;

    // Effect strength: 0 -> 1 -> 0
    float effect = sin(Progress * 3.14159265);

    // Slight horizontal distortion
    float wave = sin(uv.y * 35.0 + Progress * 20.0);
    uv.x += wave * 0.015 * effect;

    // Separate RGB channels
    float separation = 0.008 * effect;

    float red = texture(
    DiffuseSampler,
    uv + vec2(separation, 0.0)
    ).r;

    float green = texture(
    DiffuseSampler,
    uv
    ).g;

    float blue = texture(
    DiffuseSampler,
    uv - vec2(separation, 0.0)
    ).b;

    vec3 color = vec3(red, green, blue);

    // Slight brightness pulse
    color *= 1.0 + effect * 0.15;

    fragColor = vec4(color, 1.0);
}