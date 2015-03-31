import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.gl2.GLUT;

import framework.JOGLFrame;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL2.
 * 
 * @author Robert C. Duvall
 */
public class Shadow extends Scene {
    private static final float[] LIGHT_LOC = { 2, 2, 2, 1 };
    // color of pixels in shadow
    private static final ByteBuffer SHADOW_COLOR = Buffers.newDirectByteBuffer(0x7f7f7f7f);
    static {
        SHADOW_COLOR.putInt(0x7f7f7f7f);
    }
    // animation state
    private float myObjectAngle;
    private float myAngle;
    private float[] myLightPos;
    private boolean myShadowsOn;
    private boolean myCameraView;
    private boolean myRotateObject;


    /**
     * Create the scene with the given arguments.
     */
    public Shadow (String[] args) {
        super("Shadow");
        myAngle = 0;
        myObjectAngle = 0;
        myShadowsOn = true;
        myCameraView = true;
        myRotateObject = true;
        myLightPos = LIGHT_LOC;
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // rotate entire scene
        gl.glRotatef(myAngle, 0, 1, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, myLightPos, 0);
        if (myShadowsOn) {
            shadows(gl, glu, glut, myLightPos);
        } else {
            renderScene(gl, glu, glut);
        }
    }

    /**
     * Set the camera's view of the scene.
     */
    @Override
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        if (myCameraView) {
            glu.gluLookAt(0, 0, 5,  // from position
                          0, 0, 0,  // to position
                          0, 1, 0); // up direction
        }
        else {
            glu.gluLookAt(myLightPos[0], myLightPos[1], myLightPos[2], 
                          0, 0, 0, 
                          0, 1, 0);
        }
    }

    /**
     * Animate the scene by changing its state slightly.
     */
    @Override
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        if (myRotateObject) {
            myAngle += 5;
            myObjectAngle += 1;
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
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    /**
     * Called when any key is pressed within the canvas.
     */
    @Override
    public void keyPressed (int keyCode) {
        switch (keyCode) {
          case KeyEvent.VK_S:
            myShadowsOn = !myShadowsOn;
            break;
          case KeyEvent.VK_L:
            myCameraView = !myCameraView;
            break;
          case KeyEvent.VK_R:
              myRotateObject = !myRotateObject;
              break;
          case KeyEvent.VK_PERIOD:
          case KeyEvent.VK_GREATER:
            myLightPos[1] += 0.1;
            break;
          case KeyEvent.VK_COMMA:
          case KeyEvent.VK_LESS:
            myLightPos[1] -= 0.1;
            break;
        }
    }

    private void renderScene (GL2 gl, GLU glu, GLUT glut) {
        gl.glColor3f(0.4f, 1.0f, 0.4f);
        glut.glutSolidSphere(0.6, 24, 24);

        gl.glPushMatrix(); {
            gl.glTranslatef(0.6f, 0.35f, 0.6f);
            gl.glColor3f(1.0f, 0.7f, 0.7f);
            glut.glutSolidCube(0.2f);
        }
        gl.glPopMatrix();

        // set the line width for wire models
        gl.glLineWidth(2);
        gl.glColor3f(1.0f, 1.0f, 0.4f);
        gl.glPushMatrix(); {
            gl.glTranslatef(0.7f, 0.85f, 0.7f);
            gl.glRotatef(myObjectAngle, 0, 1, 0);
            gl.glTranslatef(0.0f, -0.2f, 0.0f);
            gl.glRotatef(-90, 1, 0, 0);
            glut.glutWireCone(0.2, 0.4, 8, 8);
        }
        gl.glPopMatrix();

        // set the line width for wire models
        gl.glLineWidth(3);
        gl.glColor3f(1.0f, 0.4f, 1.0f);
        gl.glPushMatrix(); {
            gl.glTranslatef(-0.9f, -0.9f, -0.1f);
            gl.glRotatef(90, -0.5f, 0.5f, 0.15f);
            gl.glRotatef(myObjectAngle, 0, 0, 1);
            glut.glutWireTorus(0.2, 0.5, 8, 8);
        }
        gl.glPopMatrix();
    }

    private void shadows (GL2 gl, GLU glu, GLUT glut, float[] lightPos) {
        double[] modelMat = new double[16];
        double[] projectionMat = new double[16];
        int[] viewport = new int[4];
        double[] lightMat = new double[16];
        double[] objPt = new double[4];
        double[] winPt = new double[4];

        // get model, projection, and viewport matrices
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, modelMat, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projectionMat, 0);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);

        // save current draw buffer type
        int[] currentBuffer = new int[1];
        gl.glGetIntegerv(GL2.GL_DRAW_BUFFER, currentBuffer, 0);

        // draw scene from viewpoint of light
        Dimension size = getWindowSize();
        FloatBuffer lightDepthBuffer = Buffers.newDirectFloatBuffer(size.width * size.height);
        gl.glPushMatrix(); {
            // disable drawing into color buffer
            gl.glDrawBuffer(GL2.GL_NONE);
            // set the camera to the viewpoint of the light
            gl.glLoadIdentity();            
            glu.gluLookAt(lightPos[0], lightPos[1], lightPos[2], 0, 0, 0, 0, 1, 0);
            gl.glRotatef(myAngle, 0, 1, 0);
            // get transformation from light's view
            gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, lightMat, 0);
            renderScene(gl, glu, glut);
            // save depth buffer values of the light
            gl.glReadPixels(0, 0, size.width, size.height, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, lightDepthBuffer);
        }
        gl.glPopMatrix();

        // enable drawing into color buffer
        gl.glDrawBuffer(currentBuffer[0]);
        // clear depth buffer and redraw with shadows
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
        // draw scene normally
        renderScene(gl, glu, glut);

        // get depth buffer of the camera
        FloatBuffer viewDepthBuffer = Buffers.newDirectFloatBuffer(size.width * size.height);
        gl.glReadPixels(0, 0, size.width, size.height, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, viewDepthBuffer);

        // set projection matrix to orthographic to see exactly where shadows fall
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix(); {
            gl.glLoadIdentity();
            glu.gluOrtho2D(0, size.width, 0, size.height);
            // switch to modelview matrix stack
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPushMatrix(); {
                gl.glLoadIdentity();
                // go through every pixel in frame buffer
                for (int y = 0; y < size.height; y++) {
                    for (int x = 0; x < size.width; x++) {
                        // depth at this pixel
                        float viewDepth = viewDepthBuffer.get(y * size.width + x);
                        // do not calculate points past the far plane of frustrum
                        if (viewDepth < 1) {
                            // get world coordinate from x, y, depth
                            glu.gluUnProject(x, y, viewDepth, modelMat, 0, 
                                             projectionMat, 0, viewport, 0, objPt, 0);
                            // get light view screen coordinate and depth
                            glu.gluProject(objPt[0], objPt[1], objPt[2], lightMat, 0,
                                           projectionMat, 0, viewport, 0, winPt, 0);

                            int ix = (int)(winPt[0] + 0.5);
                            int iy = (int)(winPt[1] + 0.5);
                            // do not calculate if outside the screen
                            if (ix >= 0 && iy >= 0 && ix < size.width && iy < size.height) {
                                // get the depth value from the light
                                float lightDepth = lightDepthBuffer.get(iy * size.width + ix);
                                // is something between the light and the pixel?
                                if (winPt[2] - lightDepth > 0.005) {
                                    // finally draw over color pixel with shadow
                                    gl.glRasterPos2i(x, y);
                                    gl.glDrawPixels(1, 1, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, SHADOW_COLOR);
                                }
                            }
                        }
                    }
                }
            }
            gl.glPopMatrix();
            // restore to projection matrix stack (for next pop)
            gl.glMatrixMode(GL2.GL_PROJECTION);
        }
        gl.glPopMatrix();
        // restore modelview matrix stack (for next operation)
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Shadow(args));
    }
}
