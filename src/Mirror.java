import java.awt.event.KeyEvent;
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
public class Mirror extends Scene {
    private static final double[] MIRROR_PLANE = { 0.0, 1.0, 0.0, 0.0 };
    // animation state
    private float myFloorSize;
    private float myObjectAngle;
    private boolean myRotateObject;
    private boolean myMirrorOn;


    /**
     * Create the scene with the given arguments.
     */
    public Mirror (String[] args) {
        super("Mirror");
        myFloorSize = 1;
        myObjectAngle = 0;
        myRotateObject = true;
        myMirrorOn = true;
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        if (myMirrorOn) {
            renderScene(gl, glu, glut);
        }
        else {
            renderObjects(gl, glu, glut);
            renderMirror(gl, glu, glut);
        }
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0, 2, 4,  // from position
                      0, 0, 0,  // to position
                      0, 1, 0); // up direction
    }

    /**
     * Animate the scene by changing its state slightly.
     */
    @Override
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        if (myRotateObject) {
            myObjectAngle += 5;
        }
    }

    /**
     * Establish the lights in the scene.
     */
    @Override
    public void setLighting (GL2 gl, GLU glu, GLUT glut) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
    }

    /**
     * Called when any key is pressed within the canvas.
     */
    @Override
    public void keyPressed (int keyCode) {
        switch (keyCode) {
        case KeyEvent.VK_R:
            myRotateObject = !myRotateObject;
            break;
        case KeyEvent.VK_M:
            myMirrorOn = !myMirrorOn;
            break;
        case KeyEvent.VK_PLUS:
        case KeyEvent.VK_EQUALS:
            myFloorSize += 0.1;
            break;
        case KeyEvent.VK_MINUS:
        case KeyEvent.VK_UNDERSCORE:
            myFloorSize -= 0.1;
            break;
        }
    }


    public void renderScene (GL2 gl, GLU glu, GLUT glut) {
        // get the current color buffer being drawn into
        int[] currentBuffer = { GL2.GL_NONE };
        gl.glGetIntegerv(GL2.GL_DRAW_BUFFER, currentBuffer, 0);

        // clear stencil buffer
        gl.glClear(GL2.GL_STENCIL_BUFFER_BIT);

        // draw mirror into stencil buffer not color buffer (create a stencil of floor)
        gl.glEnable(GL2.GL_STENCIL_TEST);
        gl.glStencilFunc(GL2.GL_ALWAYS, 1, 1);
        gl.glStencilOp(GL2.GL_REPLACE, GL2.GL_REPLACE, GL2.GL_REPLACE);
        // draw only the mirror plane
        gl.glDrawBuffer(GL2.GL_NONE);
        renderMirror(gl, glu, glut);

        // reset for next draw - clear the color and depth buffers
        gl.glDrawBuffer(currentBuffer[0]);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // draw only where the stencil buffer == 1 (where we earlier drew the floor)
        gl.glStencilFunc(GL2.GL_EQUAL, 1, 1);
        gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);
        // draw objects in reverse (i.e., reflected in mirror)
        gl.glPushMatrix(); {
            gl.glScalef(1, -1, 1);
            // clip to the mirror, so not to reflect objects behind the mirror
            gl.glEnable(GL2.GL_CLIP_PLANE0);
            gl.glClipPlane(GL2.GL_CLIP_PLANE0, MIRROR_PLANE, 0);
            renderObjects(gl, glu, glut);
            gl.glDisable(GL2.GL_CLIP_PLANE0);
        }
        gl.glPopMatrix();
        gl.glDisable(GL2.GL_STENCIL_TEST);

        // draw objects normally into color buffer (without stencil test)
        renderObjects(gl, glu, glut);

        // draw floor into color buffer, blending with mirror image
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        // fight standard Z-clipping
        gl.glDepthMask(false);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        renderMirror(gl, glu, glut);
        // reset everything back to defaults
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glDepthMask(true);
        gl.glDisable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_LIGHTING);
    }

    private void renderObjects (GL2 gl, GLU glu, GLUT glut) {
        gl.glColor3f(0, 0.8f, 1);
        gl.glPushMatrix(); {
            gl.glTranslatef(0, 0.35f, 0);
            gl.glRotatef(myObjectAngle, 0, 1, 0);
            glut.glutSolidTeapot(0.5);
        }
        gl.glPopMatrix();

        gl.glColor3f(0.6f, 0.8f, 0);
        gl.glPushMatrix(); {
            gl.glRotatef(myObjectAngle, 1, 1, 0);
            gl.glTranslatef(-1, 1.5f, 0);
            glut.glutSolidCube(0.5f);
        }
        gl.glPopMatrix();
    }

    private void renderMirror (GL2 gl, GLU glu, GLUT glut) {
        gl.glColor4f(0, 0, 1, 0.5f);
        gl.glBegin(GL2.GL_QUADS); {
            gl.glNormal3f(0, 1, 0);
            gl.glVertex3f(-myFloorSize, 0,  myFloorSize);
            gl.glVertex3f( myFloorSize, 0,  myFloorSize);
            gl.glVertex3f( myFloorSize, 0, -myFloorSize);
            gl.glVertex3f(-myFloorSize, 0, -myFloorSize);
        }
        gl.glEnd();
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Mirror(args));
    }
}
