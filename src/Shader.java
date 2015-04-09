import java.awt.event.KeyEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

import framework.JOGLFrame;
import framework.Scene;
import framework.ShaderProgram;


/**
 * Display a simple scene to demonstrate OpenGL.
 *
 * @author Robert C. Duvall
 */
public class Shader extends Scene {
    private static final String SHADERS_DIR = "/shaders/";
    private static final String[] SHADERS = {
        "fixed",
        "diffuse",
        "gouraud",
        "gooch",
        "pulse"
    };
    private float[] myLightPos = { 0, 1, 1, 1 };

    // animation state
    private float myTime;
    private float myAngle;
    private boolean isShaderOn;
    private String myShaderFile;
    private ShaderProgram myShader;


    /**
     * Create the scene with the given arguments.
     */
    public Shader (String[] args) {
        super("Shader example");
        myTime = 0;
        myAngle = 0;
        isShaderOn = true;
        myShaderFile = args.length > 0 ? args[0] : "fixed";
    }

    /**
     * Initialize general OpenGL values once (in place of constructor).
     */
    @Override
    public void init (GL2 gl, GLU glu, GLUT glut) {
        // load shaders from disk ONCE
        myShader = makeShader(gl, myShaderFile);
    }

    /**
     * Draw all of the objects to display.
     */
    @Override
    public void display (GL2 gl, GLU glu, GLUT glut) {
        // usually not necessary
        myShader = makeShader(gl, myShaderFile);
        gl.glRotatef(myAngle, 0, 1, 0);
        if (isShaderOn) {
            myShader.bind(gl); {
                glut.glutSolidTeapot(1);
            }
            myShader.unbind(gl);
        }
        else {
            gl.glColor3d(1, 0, 1);
            glut.glutSolidTeapot(1);
        }
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
     * Establish the lights in the scene.
     */
    public void setLighting (GL2 gl, GLU glu, GLUT glut) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // position light and make it a spotlight
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, myLightPos, 0);
    }

    /**
     * Animate the scene by changing its state slightly.
     */
    @Override
    public void animate (GL2 gl, GLU glu, GLUT glut) {
        myAngle += 4;
        myTime += 1.0f / JOGLFrame.FPS;
        myShader.bind(gl); {
            // animate shader values
            if (myShaderFile == "pulse") {
                myShader.setUniform(gl, "time", new float[] { myTime }, 1);
            }
        }
        myShader.unbind(gl);
    }

    /**
     * Called when any key is pressed within the canvas.
     */
    @Override
    public void keyReleased (int keyCode) {
        switch (keyCode) {
          // turn light on/off
          case KeyEvent.VK_S:
            isShaderOn = ! isShaderOn;
            break;
          case KeyEvent.VK_1:
          case KeyEvent.VK_2:
          case KeyEvent.VK_3:
          case KeyEvent.VK_4:
          case KeyEvent.VK_5:
            myShaderFile = SHADERS[keyCode - KeyEvent.VK_1];
            break;
        }
    }

    // create a program from the corresponding vertex and fragment shaders
    private ShaderProgram makeShader (GL2 gl, String filename) {
        ShaderProgram result = new ShaderProgram();
        result.attachVertexShader(gl, SHADERS_DIR+filename+".vert");
        result.attachFragmentShader(gl, SHADERS_DIR+filename+".frag");
        result.link(gl);
        return result;
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Shader(args));
    }
}
