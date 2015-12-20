package lesson1.our.com.gllesson1;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class MainGLRenderer implements GLSurfaceView.Renderer {

    float arr[][] = {{1.0f, 0.0f, 0.0f}, {1.0f, 1.0f, 0.0f}, {0.0f, 1.0f, 0.0f}, {0.0f, 1.0f, 1.0f}, {0.0f, 0.0f, 1.0f}, {1.0f, 0.0f, 1.0f}};

    int factor = 0;
    long prevTime = 0;
    long frames = 0;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        factor = 0;
        prevTime = System.currentTimeMillis();
        System.out.println(prevTime);
    }

    /**
     * On rotation of the device, on changing size of the gl view
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    /**
     * On every frame drawing
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {

        if (System.currentTimeMillis() - prevTime > 1000/3) {
            System.out.println("FPS: " + frames);
            prevTime = System.currentTimeMillis();
            frames = 0;
            factor++;
            float r = arr[factor%6][0];
            float g = arr[factor%6][1];
            float b = arr[factor%6][2];
            glClearColor(r, g, b, 1.0f);
        }
        glClear(GL_COLOR_BUFFER_BIT);

        frames++;
    }
}
