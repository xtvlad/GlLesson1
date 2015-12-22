package lesson1.our.com.gllesson1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class MainGLRenderer implements GLSurfaceView.Renderer {

    private final int MAT_SIZE              = 16;
    private final int COORDINATE_COUNT      = 3;
    private final int COLOR_COUNT           = 3;
    private final int BYTES_PER_FLOAT       = 4;
    private final int BUFFERS_COUNT         = 1;
    private final int TRIANGLE_VERTEX_COUNT = 3;

    private final int TRIANGLE_INDEX = 0;

    private final float triangleVertices[] = {
            // vertex 1 x y z
            0.0f, 1.0f, 0.0f,
            // vertex 2 x y z
            -0.8f, -0.3f, 0.0f,
            // vertex 3 x y z
            0.8f, -0.3f, 0.0f
    };

    private final float triangleColors[] = {
            // color for vertex 1 r g b
            0.9f, 0.5f, 0.0f,
            // color for vertex 2 r g b
            0.9f, 0.9f, 0.0f,
            // color for vertex 3 r g b
            0.5f, 0.9f, 0.0f
    };

    private Context context;

    private int mainShaderProgram;
    private int idxMvp;
    private int idxPosition;
    private int idxColor;
    private int[] idxVertBuffers;
    private int[] idxColorBuffers;

    public MainGLRenderer(Context cntxt)
    {
        context = cntxt;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Configure shaders programs which will be run on video card
        // To force matrix and vector calculations by this action
        configureShaders();

        // Configure gl setting color to clear the screen
        configureGL();

        // Configure model drawing materials such as vertex coordinates and triangleColors
        configureModels();
    }

    private void configureModels() {
        generateVertexBuffers();

        generateColorBuffers();
    }

    private void generateColorBuffers() {
        idxColorBuffers = new int[BUFFERS_COUNT];
        glGenBuffers(BUFFERS_COUNT, idxColorBuffers, 0);

        // Load triangle triangleVertices triangleColors to openGL
        generateColorBuffer(TRIANGLE_INDEX, triangleColors);
    }

    private void generateColorBuffer(int index, float[] colors) {
        int size = colors.length * BYTES_PER_FLOAT;

        glBindBuffer(GL_ARRAY_BUFFER, idxColorBuffers[index]);
        glBufferData(GL_ARRAY_BUFFER, size, FloatBuffer.wrap(colors), GL_STATIC_DRAW);
    }

    private void generateVertexBuffers() {
        idxVertBuffers = new int[BUFFERS_COUNT];
        glGenBuffers(BUFFERS_COUNT, idxVertBuffers, 0);

        // Load triangle triangleVertices coordinates to openGL
        generateVertexBuffer(TRIANGLE_INDEX, triangleVertices);
    }

    private void generateVertexBuffer(int index, float[] vertices) {
        int size = vertices.length * BYTES_PER_FLOAT;

        glBindBuffer(GL_ARRAY_BUFFER, idxVertBuffers[index]);
        glBufferData(GL_ARRAY_BUFFER, size, FloatBuffer.wrap(vertices), GL_STATIC_DRAW);
    }

    private void configureGL() {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void configureShaders() {
        int vertexShader;
        int fragmentShader;

        // Create and compile vertex shader with source from "main.vert" assets file
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, ShaderParser.getShader(context, "main.vert"));
        glCompileShader(vertexShader);

        // Create and compile fragment shader with source from "main.frag" assets file
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, ShaderParser.getShader(context, "main.frag"));
        glCompileShader(fragmentShader);

        // Create full shader program and attach to it vertex and fragment shaders
        mainShaderProgram = glCreateProgram();
        glAttachShader(mainShaderProgram, vertexShader);
        glAttachShader(mainShaderProgram, fragmentShader);

        // Link program
        glLinkProgram(mainShaderProgram);

        // Get indexes pointed to uniform and attribute values used in shaders
        idxMvp = glGetUniformLocation(mainShaderProgram, "u_MVPMatrix");
        idxPosition = glGetAttribLocation(mainShaderProgram, "a_Position");
        idxColor = glGetAttribLocation(mainShaderProgram, "a_Color");

        // Set using from this moment this created shader programs
        glUseProgram(mainShaderProgram);
    }

    /**
     * On rotation of the device, on changing size of the gl view
     *
     * @param gl - unused gl context version 1.0
     * @param width - width of the drawing surface
     * @param height - height of the drawing surface
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    /**
     * On every frame drawing
     *
     * @param gl - unused gl context version 1.0
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO: Calculate next scene objects states.

        glClear(GL_COLOR_BUFFER_BIT);

        drawTriangle();

        glFlush();
    }

    private void drawTriangle() {
        float model[]   = new float[MAT_SIZE];
        int vertSize    = COORDINATE_COUNT;
        int vertStride  = vertSize * BYTES_PER_FLOAT;
        int colorSize   = COLOR_COUNT;
        int colorStride = colorSize * BYTES_PER_FLOAT;

        Matrix.setIdentityM(model, 0);

        glEnableVertexAttribArray(idxPosition);
        glBindBuffer(GL_ARRAY_BUFFER, idxVertBuffers[TRIANGLE_INDEX]);

        // How to run over currently binded buffer
        // variable in shader (on videocard) will receive variables from buffer - idxPosition variable
        // how much elements pass to this variable - vertSize
        // how much elements to skip to next set of variables - vertStride
        // start from element - 0 (offset)
        glVertexAttribPointer(idxPosition, vertSize, GL_FLOAT, false, vertStride, 0);

        glEnableVertexAttribArray(idxColor);
        glBindBuffer(GL_ARRAY_BUFFER, idxColorBuffers[TRIANGLE_INDEX]);
        glVertexAttribPointer(idxColor, colorSize, GL_FLOAT, false, colorStride, 0);

        // Pass model array as model-view-projection matrix to shader (on videocard program)
        glUniformMatrix4fv(idxMvp, 1, false, FloatBuffer.wrap(model));

        glDrawArrays(GL_TRIANGLES, 0, TRIANGLE_VERTEX_COUNT);
    }
}
