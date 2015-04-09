const vec4 diffuse_color = vec4(1.0, 0.0, 0.0, 1.0);
const vec4 specular_color = vec4(1.0, 1.0, 1.0, 1.0);
const vec4 ambient_color = vec4(0.2, 0.2, 0.2, 1.0);

varying float specular_intensity;
varying float diffuse_intensity;


void main () {
    // calculate final color
	gl_FragColor = ambient_color + 
	               diffuse_color * diffuse_intensity + 
	               specular_color * specular_intensity;
}
