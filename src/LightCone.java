import java.awt.event.KeyEvent;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL2.
 * 
 * This program shows a visible cone of light as in a dusty room.
 *
 * @author Robert C. Duvall
 */
public class LightCone extends Scene {
    private static final float[] LIGHT0_DIFFUSE = { 1, 1, 1, 1 };
    private static final float[] LIGHT0_DIR = { 0, -1, 0, 0 };
    private static final float[] LIGHT0_LOC = { 0, 4.5f, 0, 1 };
    private static final float[] NO_EMISSION = { 0.1f, 0.1f, 0.1f, 0 };
    private static final float[] whiteEmission = { 0.4f, 0.4f, 0.4f, 0 };

    // animation state
    private boolean myConeIsOn;
    private float myAngle;
    private float myAngleSpeed;
    private float myBlurSmoothFactor;
    private int myFloorSize;


    /**
     * Create the scene with the given arguments.
     * 
     * For example, the number of strands to display.
     * 
     * @param args command-line arguments
     */
    public LightCone (String[] args) {
        super("Light Cone");
        myConeIsOn = false;
        myAngle = 0;
        myAngleSpeed = 5;
        myBlurSmoothFactor = 20;
        myFloorSize = 128;
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // move the light
        gl.glPushMatrix(); {
            gl.glTranslatef(0, 0, -1);
            gl.glRotatef(myAngle, 0, 1, 0);
            gl.glRotatef(-15, 0, 0, 1);
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LIGHT0_LOC, 0);
        }
        gl.glPopMatrix();

        // render everything that is only supposed to be rendered once
        renderScene(gl, glu, glut);

        if (myConeIsOn) {
            // enable blending
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            for (int k = 0; k < myBlurSmoothFactor; k++) {
                // render the light n number of times always with a slightly different angle
                // this produces this light cone effect
                renderLight(gl, glu, glut, k, false);
            }
            gl.glDisable(GL2.GL_BLEND);
        }
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0, 1, 6,  // from position
                      0, 0, 0,  // to position
                      0, 1, 0); // up direction
    }

    /**
     * Animate the scene by changing its state slightly.
     */
    @Override
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        myAngle += myAngleSpeed;
    }

    /**
     * Establish the lights in the scene.
     */
    @Override
    public void setLighting (GL2 gl, GLU glu, GLUT glut) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LIGHT0_LOC, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, LIGHT0_DIFFUSE, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, LIGHT0_DIR, 0);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 14.0f);
    }

    /**
     * Called when any key is pressed within the canvas.
     */
    @Override
    public void keyPressed (int keyCode) {
        switch (keyCode) {
        case KeyEvent.VK_L:
            myConeIsOn = !myConeIsOn;
            break;
        case KeyEvent.VK_PLUS:
        case KeyEvent.VK_EQUALS:
            myBlurSmoothFactor++;
            break;
        case KeyEvent.VK_MINUS:
        case KeyEvent.VK_UNDERSCORE:
            myBlurSmoothFactor--;
            break;
        }
    }


    private void renderScene (GL2 gl, GLU glu, GLUT glut) {
        gl.glPushMatrix(); {
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, NO_EMISSION, 0);
            renderFloor(gl, glu, glut);
            gl.glTranslatef(0.3f, 0.3f, 0.1f);
            gl.glColor3f(1, 0, 0);
            glut.glutSolidSphere(0.25f, 40, 40);
        }
        gl.glPopMatrix();
    }

    private void renderLight (GL2 gl, GLU glu, GLUT glut, int history, boolean overwrite) {
        float angle = (history / myBlurSmoothFactor) * 360;
        gl.glPushMatrix(); {
            // make the light cone track the light spot
            gl.glRotatef(myAngle, 0, 1, 0);
            gl.glTranslatef(1, 0, 0);
            gl.glRotatef(10, 0, 0, 1);
            // rotate the cone so that it is pointing down
            gl.glRotatef(90, 1, 0, 0);
            gl.glRotatef(myAngle, 0, 0, 1);
            gl.glTranslatef(0, 0, -4.3f);
            if (!overwrite) {
                gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, whiteEmission, 0);
                gl.glColor4f(1, 1, 1, 0.05f);
            } else {
                gl.glColor4f(1, 1, 1, 1);
            }
            gl.glRotatef(angle, 0, 0, 1);
            gl.glRotatef(3, 0, 1, 0);
            glu.gluCylinder(glu.gluNewQuadric(), 0, 1, 5, 20, 1);
        }
        gl.glPopMatrix();
    }

    private void renderFloor (GL2 gl, GLU glu, GLUT glut) {
        gl.glColor3f(0, 0, 0.4f);
        gl.glPushMatrix(); {
            gl.glTranslatef(0, -0.2f, 0);
            gl.glScalef(0.05f, 0.05f, 0.05f);
            for (int i = -(myFloorSize / 2); i < (myFloorSize / 2); i++) {
                for (int j = -(myFloorSize / 2); j < (myFloorSize / 2); j++) {
                    // make one square
                    gl.glBegin(GL2.GL_QUADS); {
                        gl.glNormal3f(0, 1, 0);
                        gl.glVertex3f(i, 0, j);
                        gl.glVertex3f(i, 0, j + 1);
                        gl.glVertex3f(i + 1, 0, j + 1);
                        gl.glVertex3f(i + 1, 0, j);
                    }
                    gl.glEnd();
                }
            }
        }
        gl.glPopMatrix();
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new LightCone(args));
    }
}
