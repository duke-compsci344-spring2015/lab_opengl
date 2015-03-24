import java.awt.event.KeyEvent;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import framework.JOGLFrame;
import framework.OBJException;
import framework.OBJModel;
import framework.Scene;


/**
 * Display a simple scene to demonstrate OpenGL.
 * 
 * @author Robert C. Duvall
 */
public class OBJViewer extends Scene {
    private static final float[] X_AXIS = { 1, 0, 0 };
    private static final float[] Y_AXIS = { 0, 1, 0 };
    private static final float[] Z_AXIS = { 0, 0, 1 };

    // animation state
    private String myModelFile;
    private OBJModel myModel;
    private float myScale;
    private float myAngle;
    private float[] myAxis;
    private int myRenderMode;
    private boolean showBoundingBox;

    /**
     * Create the scene with the given arguments.
     */
    public OBJViewer (String[] args) {
        super("Spinning OBJ Models");
        myModelFile = args.length > 0 ? args[0] : "models/war/tommy-gun.obj";
    }

    /**
     * Initialize general OpenGL values once (in place of constructor).
     */
    public void init (GL2 gl, GLU glu, GLUT glut) {
        myScale = 0.1f;
        myAngle = 0;
        myAxis = Y_AXIS;
        myRenderMode = GL2.GL_FILL;
        showBoundingBox = false;
        try {
            myModel = new OBJModel(myModelFile);
            System.out.println(myModel);
        }
        catch (OBJException e) {
            System.out.println("Cannot load " + myModelFile);
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Establish lights in the scene.
     */
    @Override
    public void setLighting (GL2 gl, GLU glu, GLUT glut) {
        //gl.glEnable(GL2.GL_LIGHTING);
        //gl.glEnable(GL2.GL_LIGHT0);
    }

    /**
     * Draw all of the objects to display.
     */
    public void display (GL2 gl, GLU glu, GLUT glut) {
        gl.glRotatef(myAngle, myAxis[0], myAxis[1], myAxis[2]);
        gl.glScalef(myScale, myScale, myScale);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, myRenderMode);
        myModel.render(gl);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        if (showBoundingBox) {
            float[] box = myModel.getDimensions();
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL2.GL_CULL_FACE);
            gl.glColor4f(1, 1, 1, 0.3f);
            gl.glScalef(box[0], box[1], box[2]);
            glut.glutSolidCube(1);
            gl.glDisable(GL2.GL_BLEND);
        }
    }

    /**
     * Set the camera's view of the scene.
     */
    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        glu.gluLookAt(0,  0,  1,   // from position
                      0,  0,  0,   // to position
                      0,  1,  0);  // up direction
    }

    /**
     * Animate the scene by changing its state slightly.
     */
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        myAngle += 3;
    }

    /**
     * Called when any key is pressed within the canvas.
     */
    public void keyPressed (int keyCode) {
        switch (keyCode) {
        case KeyEvent.VK_A:
            myRenderMode = ((myRenderMode == GL2.GL_FILL) ? GL2.GL_LINE : GL2.GL_FILL);
            break;
        case KeyEvent.VK_B:
            showBoundingBox = !showBoundingBox;
            break;
        case KeyEvent.VK_X:
            myAxis = X_AXIS;
            break;
        case KeyEvent.VK_Y:
            myAxis = Y_AXIS;
            break;
        case KeyEvent.VK_Z:
            myAxis = Z_AXIS;
            break;
        case KeyEvent.VK_PERIOD:
            myScale += 0.01f;
            break;
        case KeyEvent.VK_COMMA:
            myScale -= 0.01f;
            break;
        }
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new OBJViewer(args));
    }
}
