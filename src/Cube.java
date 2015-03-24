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
public class Cube extends Scene {
    public Cube (String[] args) {
        super("Cube Display");
    }

    public void display (GL2 gl, GLU glu, GLUT glut) {
        double[] mat = new double[16];

        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mat, 0);
        Printer.printMatrix("After glTranslatef:", mat);
        gl.glRotatef(60, 0.0f, 1.0f, 0.0f);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mat, 0);
        Printer.printMatrix("After glRotatef:", mat);
        gl.glScalef(2.0f, 2.0f, 2.0f);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mat, 0);
        Printer.printMatrix("After glScalef:", mat);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        glut.glutWireCube(1);

        Printer.printPoints(gl, glu);
    }

    public void setCamera (GL2 gl, GLU glu, GLUT glut) {
        int[] vec = new int[4];
        gl.glGetIntegerv(GL2.GL_VIEWPORT, vec, 0);
        Printer.printVector("After glViewport:", vec);

        double[] mat = new double[16];
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, mat, 0);
        Printer.printMatrix("After gluPerspective:", mat);

        glu.gluLookAt(0, 2, 4, // from position
                      0, 0, 0, // to position
                      0, 1, 0); // up direction
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mat, 0);
        Printer.printMatrix("After gluLookAt:", mat);
    }


    // allow program to be run from here
    public static void main (String[] args) {
        new JOGLFrame(new Cube(args));
    }
}
