package punchmania.punchmania;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;

public class OpenGLES20Activity extends Activity {
    private GLSurfaceView mGLSurfaceView;
    private PunchRenderer renderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;
    private float mPreviousX, mPreviousY;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer = new PunchRenderer();
        mGLSurfaceView = new GLSurfaceView(this);

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;


        if(supportsEs2){
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(renderer);
        }
        else{
            return;
        }

        setContentView(mGLSurfaceView);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        switch(event.getAction()){
//            case MotionEvent.ACTION_MOVE:
//                float dx = x - mPreviousX;
//                float dy = y - mPreviousY;
//                renderer.setAngle(renderer.getAngle()+((dx+dy)*TOUCH_SCALE_FACTOR));
//        }
//        mPreviousY = y;
//        mPreviousX = x;
//        return true;
//    }

    protected void onResume(){
        super.onResume();
        mGLSurfaceView.onResume();
    }
    protected void onPause(){
        super.onPause();
        mGLSurfaceView.onPause();
    }

}
