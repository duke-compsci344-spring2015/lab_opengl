import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL.
 *
 * @author Robert C. Duvall
 */
public class Simple extends Scene {
    // animation state
    private float myAngle = 0.0f;

    /**
     * Create the scene with the given arguments.
     *
     * For example, the number of shapes to display.
     *
     * @param args command-line arguments
     */
    public Simple (String[] args) {
        // do nothing
    }

    /**
     * @return title for this scene
     */
    @Override
    public String getTitle () {
        return "A spinning shape!";
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // define object's color
        gl.glColor3f(1.0f, 0.0f, 1.0f);
        // rotate object about y-axis
        gl.glRotatef(myAngle, 0.0f, 1.0f, 0.0f);
        // draw object as wire teapot
        glut.glutSolidCube(1);
        // define object's color
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        // draw object as wire teapot
        glut.glutWireCube(1);
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0, 1, 5,  // from position
                      0, 0, 0,  // to position
                      0, 1, 0); // up direction
    }

    /**
     * Animate the scene by changing its state slightly.
     *
     * For example, the angle of rotation of the shapes.
     */
    @Override
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        // animate model by spinning it a few degrees each time
        myAngle += 4;
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Simple(args));
    }
}
