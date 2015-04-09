varying vec3 normal;
varying vec3 vertex_to_light_vector;

void main () {
    // compute final vertex position (using standard vertex transform)
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
 
    // calculate values for next shader
    // transform vertex position to modelview-space
    vec3 vertex_in_modelview_space = vec3(gl_ModelViewMatrix * gl_Vertex);
    // transform normal to modelview-space
    normal = vec3(normalize(gl_NormalMatrix * gl_Normal));
    // calculate vector from vertex to light
    vertex_to_light_vector = normalize(gl_LightSource[0].position.xyz - vertex_in_modelview_space);
}
