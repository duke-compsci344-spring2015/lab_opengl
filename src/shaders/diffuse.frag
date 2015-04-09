// define material colors
const vec4 AmbientColor = vec4(0.2, 0.2, 0.2, 1.0);
const vec4 DiffuseColor = vec4(1.0, 1.0, 0.0, 1.0);
 
varying vec3 normal;
varying vec3 vertex_to_light_vector;
 
void main()
{
    // calculate diffuse term and clamp it to [0;1]
    float DiffuseTerm = clamp(dot(normal, vertex_to_light_vector), 0.0, 1.0); 
    // calculate final color
    gl_FragColor = AmbientColor + DiffuseColor * DiffuseTerm;
}
