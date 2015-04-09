uniform float time;

varying vec3 normal;
varying vec3 vertex_to_light_vector;


float scale () {
    return 1.0 + 0.1 * sin(time);
}

void main () {
    // compute final vertex position
    // transform normal to modelview-space
    normal = (vec3(gl_NormalMatrix * gl_Normal)) * scale();
    vec4 scaled_vertex = gl_Vertex + vec4(normalize(normal), 1.0);
    gl_Position = gl_ModelViewProjectionMatrix * scaled_vertex;
 
    // calculate values for next shader
    // transform vertex position to modelview-space
    vec3 vertex_in_modelview_space = vec3(gl_ModelViewMatrix * scaled_vertex);
    // calculate vector from vertex to light
    vertex_to_light_vector = normalize(gl_LightSource[0].position.xyz - vertex_in_modelview_space);
}
