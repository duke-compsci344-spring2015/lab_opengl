varying float specular_intensity;
varying float diffuse_intensity;


void main(void) {
    // compute final vertex position (using standard vertex transform)
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    // calculate light values for next shader
	vec3 normal = normalize(gl_NormalMatrix * gl_Normal);
    vec3 vertex_in_modelview_space = vec3(gl_ModelViewMatrix * gl_Vertex);
	vec3 vertex_to_light_vector = normalize(gl_LightSource[0].position.xyz - vertex_in_modelview_space);
	vec3 light_vector_reflected = normalize(reflect(vertex_to_light_vector, normal));

	// diffuse light intensity values
    diffuse_intensity = clamp(dot(normal, vertex_to_light_vector), 0.0, 1.0); 
	specular_intensity = pow(max(dot(light_vector_reflected, normalize(vertex_in_modelview_space)), 0.0), 6.0);
}
