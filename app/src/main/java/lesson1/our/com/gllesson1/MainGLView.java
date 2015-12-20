package lesson1.our.com.gllesson1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MainGLView extends GLSurfaceView {

    private void construct()
    {
        setEGLContextClientVersion(2);
        // Number of bits for rgba depth and stencil for open gl configuration
        setEGLConfigChooser(8, 8, 8, 8, 8, 0);

        setRenderer(new MainGLRenderer());
    }

    public MainGLView(Context context) {
        super(context);

        construct();
    }

    public MainGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        construct();
    }
}
