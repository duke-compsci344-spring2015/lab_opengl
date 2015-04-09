// from the Orange Book
varying float NdotL;
varying vec3  ReflectVec;
varying vec3  ViewVec;


void main() {
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    vec3 ecPos = vec3(gl_ModelViewMatrix * gl_Vertex);
    vec3 normal = normalize(gl_NormalMatrix * gl_Normal);
    vec3 lightVec = normalize(gl_LightSource[0].position.xyz - ecPos);
    ReflectVec = normalize(reflect(-lightVec, normal));
    ViewVec = normalize(-ecPos);
    NdotL = (dot(lightVec, normal) + 1.0) * 0.5;
    
    gl_FrontColor = vec4(vec3(0.75), 1.0);
    gl_BackColor = vec4(0.0);
}
