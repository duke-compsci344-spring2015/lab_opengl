import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.util.gl2.GLUT;

import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate differences between rotation models.
 *
 * @author Robert C. Duvall
 */
public class Rotation extends Scene {
    // animation state
    private float myRollDelta = 0;
    private float myPitchDelta = 0;
    private float myYawDelta = 0;    
    // euler angle state
    private float myRoll = 0;
    private float myPitch = 0;
    private float myYaw = 0;
    // quaternion state
    private Quaternion myRotation = new Quaternion();
    private float[] myRotMatrix = new float[16];


    /**
     * Create the scene with the given arguments.
     *
     * For example, the number of shapes to display.
     *
     * @param args command-line arguments
     */
    public Rotation (String[] args) {
        // do nothing
    }

    /**
     * @return title for this scene
     */
    @Override
    public String getTitle () {
        return "Spinning Shapes";
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // rotate object based on euler angles
        gl.glPushMatrix(); {
            gl.glTranslatef(-1, 0, 0);
            gl.glRotatef(myYaw,   0, 1, 0);
            gl.glRotatef(myRoll,  0, 0, 1);
            gl.glRotatef(myPitch, 1, 0, 0);
            drawShape(gl, glut, 1, 0, 1);
        }
        gl.glPopMatrix();

        // rotate object based on quaternions
        gl.glPushMatrix(); {
            gl.glTranslatef(1, 0, 0);
            //myRotation.setFromEuler((float)Math.toRadians(myPitch), (float)Math.toRadians(myYaw), (float)Math.toRadians(myRoll));
            myRotation.toMatrix(myRotMatrix, 0);
            gl.glMultMatrixf(myRotMatrix, 0);
            drawShape(gl, glut, 0, 0, 1);
        }
        gl.glPopMatrix();
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
        myPitch += myPitchDelta;
        myYaw += myYawDelta;
        myRoll += myRollDelta;
        myRotation.rotateByEuler((float)Math.toRadians(myPitchDelta), (float)Math.toRadians(myYawDelta), (float)Math.toRadians(myRollDelta));
    }

    /**
     * Called when any key is pressed within the canvas. Turns each part of the arm separately.
     */
    @Override
    public void keyReleased (int keyCode) {
        switch (keyCode) {
          case KeyEvent.VK_1:
            myPitchDelta = -1;
            break;
          case KeyEvent.VK_2:
            myPitchDelta = 0;
            break;
          case KeyEvent.VK_3:
            myPitchDelta = 1;
            break;

          case KeyEvent.VK_4:
            myYawDelta = -1;
            break;
          case KeyEvent.VK_5:
            myYawDelta = 0;
            break;
          case KeyEvent.VK_6:
            myYawDelta = 1;
            break;

          case KeyEvent.VK_7:
            myRollDelta = -1;
            break;
          case KeyEvent.VK_8:
            myRollDelta = 0;
            break;
          case KeyEvent.VK_9:
            myRollDelta = 1;
            break;

          // reset
          case KeyEvent.VK_0:
            myYaw = 0;
            myPitch = 0;
            myRoll = 0;
            myYawDelta = 0;
            myPitchDelta = 0;
            myRollDelta = 0;
            myRotation.setIdentity();
            break;
        }
    }


    // draw an outlined shape
    private void drawShape (GL2 gl, GLUT glut, float r, float g, float b) {
        gl.glColor3f(r, g, b);
        glut.glutSolidCube(1);
        gl.glColor3f(1, 1, 1);
        glut.glutWireCube(1);
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Rotation(args));
    }
}
