package punchmania.punchmania;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20Activity extends Activity {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private GLSurfaceView mGLSurfaceView;
    private PunchRenderer renderer;
    private float mPreviousX, mPreviousY;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new PunchRenderer();
        mGLSurfaceView = new GLSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mDensity = displayMetrics.density;


        if (supportsEs2) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(renderer);
        } else {
            return;
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event != null){
            float x = event.getX();
            float y = event.getY();

            if(event.getAction() == MotionEvent.ACTION_MOVE){
                if(renderer != null){
                    float deltaX = (x - mPreviousX) / mDensity / 2f;
                    float deltaY = (y - mPreviousY) / mDensity / 2f;

                    renderer.mDeltaX += deltaX;
                    renderer.mDeltaY += deltaY;
                }
            }
            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
        else{
            return super.onTouchEvent(event);
        }

    }

    protected void onResume(){
        super.onResume();
        mGLSurfaceView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

}
