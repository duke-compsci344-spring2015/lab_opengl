import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Printer {
    public static void printPoints (GL2 gl, GLU glu) {
        printPoint(gl, glu, 0.5, 0.5, 0.5);
        printPoint(gl, glu, 0.5, 0.5, -0.5);
        printPoint(gl, glu, 0.5, -0.5, -0.5);
        printPoint(gl, glu, 0.5, -0.5, 0.5);
        printPoint(gl, glu, -0.5, -0.5, 0.5);
        printPoint(gl, glu, -0.5, -0.5, -0.5);
        printPoint(gl, glu, -0.5, 0.5, -0.5);
        printPoint(gl, glu, -0.5, 0.5, 0.5);
    }

    public static void printPoint (GL2 gl, GLU glu, double objX, double objY, double objZ) {
        // get current state
        double[] model = new double[16];
        double[] projection = new double[16];
        int[] viewport = new int[4];
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, model, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projection, 0);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        // compute projected coordinates
        double[] winPoint = new double[4];
        glu.gluProject(objX, objY, objZ, model, 0, projection, 0, viewport, 0, winPoint, 0);
        // print screen coordinates
        System.out.printf("[ %6.2f %6.2f %6.2f ] -> [ %4d %4d ]\n", objX, objY, objZ,
                          (int)winPoint[0], viewport[3] - (int)winPoint[1]);
    }

    public static void printVector (String label, int[] vec) {
        System.out.println(label);
        System.out.print("  |");
        for (int v : vec) {
            System.out.printf(" %6d", v);
        }
        System.out.println(" |");
        System.out.println();
    }

    public static void printMatrix (String label, double[] mat) {
        System.out.println(label);
        for (int r = 0; r < 4; r++) {
            System.out.print("  |");
            for (int c = 0; c < 4; c++) {
                System.out.printf(" %6.2f", mat[r + c * 4]);
            }
            System.out.println(" |");
        }
        System.out.println();
    }
}
