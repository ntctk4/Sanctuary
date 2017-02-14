precision mediump float;
uniform vec2 u_resolution;
uniform sampler2D u_sampler;
varying vec4 v_color;
varying vec2 v_texCoord0;

const float intensity = 0.75, maxRadius = 0.75, minRadius = 0.25;

void main(){
	vec4 color = texture2D(u_sampler, v_texCoord0) * v_color;
	vec2 position = gl_FragCoord.xy / u_resolution - 0.5;
	float vignette = smoothstep(maxRadius, minRadius, length(position));
	color.rgb = mix(color.rgb, color.rgb * vignette, intensity);
	gl_FragColor = color;
}
