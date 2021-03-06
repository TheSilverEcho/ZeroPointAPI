#version 430 compatibility

precision highp float;

uniform vec2 CenterPosition;
uniform vec2 Radius;

smooth in vec2 position;
smooth in vec4 vertexColor;

out vec4 fragColor;


void main() {
	if (vertexColor.a == 0.0) {
		discard;
	}

	//Distance from position to center
	float dist = length(position - CenterPosition);
	//1 - interpelated value from u_Radius - feather to u_Radius where v is source
	float alpha = 1.0 - smoothstep(Radius.x - Radius.y, Radius.x, dist);
	if (alpha == 0)
	discard;
	fragColor = vertexColor * vec4(1.0, 1.0, 1.0, alpha);
}