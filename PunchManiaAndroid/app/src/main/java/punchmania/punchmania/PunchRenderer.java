package punchmania.punchmania;

import android.nfc.Tag;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PunchRenderer implements GLSurfaceView.Renderer {

    /**
     * Rotation increment per frame.
     */
    private static final float CUBE_ROTATION_INCREMENT = 0.6f;

    /**
     * The refresh rate, in frames per second.
     */
    private static final int REFRESH_RATE_FPS = 60;

    /**
     * The duration, in milliseconds, of one frame.
     */
    private static final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;
    private final float[] mMVPMatrix;
    private final float[] mProjectionMatrix;
    private final float[] mViewMatrix;
    private final float[] mRotationMatrix;
    private final float[] mFinalMVPMatrix;
    private final float[] mTranslationMatrix;
    private Cube mCube;
    private long mLastUpdateMillis;
    private Random random;

    public volatile float angle;
    public volatile float tempX = 0;
    public volatile float tempY = 0;
    public volatile float tempZ = 0;

    public int currentPosIndex = 0;



    public PunchRenderer() {
        mMVPMatrix = new float[16];
        mProjectionMatrix = new float[16];
        mViewMatrix = new float[16];
        mRotationMatrix = new float[16];
        mFinalMVPMatrix = new float[16];
        mTranslationMatrix = new float[16];
        fetchArrayList();

        random = new Random();

        // Set the fixed camera position (View matrix).
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        mCube = new Cube();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        float ratio = (float) width / height;

        GLES20.glViewport(0, 0, width, height);
        // This projection matrix is applied to object coordinates in the onDrawFrame() method.
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f);
    }

    public void onDrawFrame(GL10 unused) {
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//
//        Matrix.setLookAtM(mViewMatrix,0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);
//
//        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);
//        Matrix.translateM(mCube.mModelMatrix,0,0.5f,0,0);
//        Matrix.rotateM(mCube.mModelMatrix,0,-45f,0,0,-1.0f);
//
//        Matrix.multiplyMM(mMVPMatrix,0,mCube.mModelMatrix,0,mMVPMatrix,0);
//        mCube.draw(mMVPMatrix);

//        Matrix.setLookAtM(mViewMatrix,0,0,0,-6,0f,0f,0f,0f,1.0f,0.0f);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//        Matrix.translateM(mTranslationMatrix,0,tempX,tempY,tempZ);
//        Matrix.multiplyMM(mMVPMatrix,0,mTranslationMatrix,0,mMVPMatrix,0);
//        // Draw cube.
//        mCube.draw(mMVPMatrix);

        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 6f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mCube.mModelMatrix, 0);
        mCube.draw(scratch);

    }

    public volatile float mAngle;

    public ArrayList<ArrayList<Integer>> fetchArrayList(){
        return MainActivity.getHighScoreDetails();
    }

    public float getAngle(){
        return mAngle;
    }
    public void setAngle(float angle){
        mAngle = angle;
    }

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("TAG", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}
