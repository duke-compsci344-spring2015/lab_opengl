import java.awt.event.KeyEvent;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import framework.JOGLFrame;
import framework.Scene;
import framework.Spline;


/**
 * Display an example of a curve and moving along it.
 * 
 * @author Robert C. Duvall
 */
public class Curve extends Scene {
    private float[] CONTROL_POINTS = {
         -1,  0,  0,
          0, -2,  0,
          3,  0,  0,
          0,  4,  0,
         -5,  0,  0,
          0, -6,  0,
          6, -3,  0,
         50,  5,  0,
         94, -3,  0,
        100, -6,  0,
        105,  0,  0,
        100,  4,  0,
         97,  0,  0,
        100, -2,  0,
        101,  0,  0
    };

    private int myAngle;
    private Spline myCurve;
    private boolean showControlPoints;

    
    /**
     * Create the scene with the given arguments.
     */
    public Curve (String[] args) {
        super("Drawing Curves");
    }

    /**
     * Initialize general OpenGL values once (in place of constructor).
     */
    @Override
    public void init (GL2 gl, GLU glu, GLUT glut) {
        myAngle = 0;
        showControlPoints = false;
        gl.glEnable(GL2.GL_MAP1_VERTEX_3);
        myCurve = new Spline();
        for (int k = 0; k < CONTROL_POINTS.length; k += 3) {
            myCurve.addPoint(CONTROL_POINTS[k], CONTROL_POINTS[k+1], CONTROL_POINTS[k+2]);
        }
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // some basic transforms to animate it
        gl.glRotatef(myAngle, 0, 1, 0);
        gl.glScalef(0.02f, 0.02f, 0.02f);
        gl.glTranslatef(-50, 0, 0);

        // lines from rod to hanging object
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_LINES); {
            gl.glVertex3f(50, 5 - (8.0f / 3.0f), 0);
            gl.glVertex3f(50, 20, 0);
            gl.glVertex3f(0, -4.0f / 3.0f, 0);
            gl.glVertex3f(0, -20, 0);
            gl.glVertex3f(100, -4.0f / 3.0f, 0);
            gl.glVertex3f(100, -20, 0);
        }
        gl.glEnd();
        // draw spline
        myCurve.draw(gl, 0.2f);
        // draw control points
        if (showControlPoints) {
            gl.glPointSize(5);
            gl.glColor3f(1, 0, 0);
            myCurve.drawControlPoints(gl);
        }
        // TODO: move an object along the spline
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0, 0, 3,  // from position
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
        myAngle += 2;
    }

    /**
     * Called when any key is pressed within the canvas. Turns each part of the arm separately.
     */
    @Override
    public void keyReleased (int keyCode) {
        switch (keyCode) {
          case KeyEvent.VK_C:
            showControlPoints = ! showControlPoints;
            break;
        }
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Curve(args));
    }
}
