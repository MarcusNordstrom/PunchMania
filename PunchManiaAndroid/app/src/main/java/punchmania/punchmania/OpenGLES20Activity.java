package punchmania.punchmania;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20Activity extends Activity {
    private GLSurfaceView mView;
    private PunchRenderer mRenderer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new GLSurfaceView(this);
        mRenderer = new PunchRenderer(this);
        mView.setRenderer(mRenderer);
        setContentView(mView);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return mRenderer.onTouchEvent(event);
    }
}