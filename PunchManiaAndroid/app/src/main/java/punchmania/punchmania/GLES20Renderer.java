package punchmania.punchmania;

import android.opengl.GLES20;

public class GLES20Renderer extends GLRenderer {
    public void onCreate(int width, int height, boolean contextLost) {
        GLES20.glClearColor(0, 0, 0, 1);
    }

    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
