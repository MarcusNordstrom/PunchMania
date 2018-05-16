package punchmania.punchmania;

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
    private final float[] mCurrentRotation;
    public volatile float angle;
    public volatile float tempX = 0;
    public volatile float tempY = 0;
    public volatile float tempZ = 0;
<<<<<<< Updated upstream

    private float traveledX;
    private float traveledY;
    private float velocityX = 0;
    private float velocityY = 0;
    private float angleX;
    private float angleY;
    private float radius = 1.3f; // Radius of the punching bag, could also be arbitrary as long as it moves like we want it to
    private float circumference = 2f * (float) Math.PI * radius;
    private float time = 0.01f; // If we can't determine the exact time, we can just modify this until we get the result we want
    private int rotationIndex = 0;

=======
    float mDeltaX = 0, mDeltaY = 0;
>>>>>>> Stashed changes
    public int currentXIndex = 0, currentYIndex = 0, currentZIndex = 0;
    long timeSinceStart;
    private Cube mCube;
    private long mLastUpdateMillis;
    private Random random;
    // private float time = 0;
    private ArrayList<ArrayList<Integer>> arrList;
    private ArrayList<Integer> x, y, z;
<<<<<<< Updated upstream
=======
    private int timeIndex = 0;
>>>>>>> Stashed changes


    public PunchRenderer() {
        mMVPMatrix = new float[16];
        mProjectionMatrix = new float[16];
        mViewMatrix = new float[16];
        mCurrentRotation = new float[16];
        random = new Random();


        arrList = MainActivity.getHighScoreDetails();
        x = arrList.get(0);
        y = arrList.get(1);
        z = arrList.get(2);

        Log.i("X Length", "" + x.size());
        Log.i("Y Length", "" + y.size());
        Log.i("Z Length", "" + z.size());

        timeSinceStart = System.nanoTime();

        // Set the fixed camera position (View matrix).
        Matrix.setLookAtM(mViewMatrix, 0, 0.0f, -1.0f, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0f, 0.0f);
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
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 0.5f, 100.0f);
    }

    public void onDrawFrame(GL10 unused) {

        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mCube.mModelMatrix, 0);
<<<<<<< Updated upstream
        rotateCube();
=======
        moveCube();
        //rotateCamera();
>>>>>>> Stashed changes
        mCube.draw(scratch);
        timeIndex++;
    }

    public void rotateCube() {
        if (rotationIndex < x.size() && rotationIndex < y.size()) {
            velocityX += x.get(rotationIndex) * time;
            traveledX = velocityX * time;
            angleX = (traveledX / circumference) * 360f;

            velocityY += y.get(rotationIndex) * time;
            traveledY = velocityY * time;
            angleY = (traveledY / circumference) * 360f; // Neither radians nor degrees, what is this even?

            Matrix.rotateM(mCube.mModelMatrix, 0, angleX, 1f, 0f, 0f); // Rotate in x-axis
            Matrix.rotateM(mCube.mModelMatrix, 0, angleY, 0f, 1f, 0f); // Rotate in y-axis

            rotationIndex++;
        }
    }

    public void moveCube() {

        long time = System.nanoTime();
        int delta_time = (int) ((time - timeSinceStart) / 1000000);
        timeSinceStart = time;

        float currentX, currentY, currentZ;
<<<<<<< Updated upstream
        if (currentXIndex == x.size() && currentYIndex == y.size() && currentZIndex == z.size()) {
            currentXIndex = 0;
            currentYIndex = 0;
            currentZIndex = 0;
        }
=======

>>>>>>> Stashed changes
        if (currentXIndex < x.size() - 1) {
            tempX = (float) x.get(currentXIndex);
            currentX = position(tempX,currentXIndex);
            currentXIndex++;
<<<<<<< Updated upstream
            currentX = tempX + (float) x.get(currentXIndex) * delta_time;
=======
//            currentX = tempX + (float) x.get(currentXIndex);// * delta_time;
>>>>>>> Stashed changes
        } else {
            currentX = 0;
        }
        if (currentYIndex < y.size() - 1) {
            tempY = (float) y.get(currentYIndex);
            currentY = position(tempY,currentYIndex);
            currentYIndex++;
<<<<<<< Updated upstream
            currentY = tempY + (float) y.get(currentYIndex) * delta_time;
=======
//            currentY = tempY + (float) y.get(currentYIndex);// * delta_time;
>>>>>>> Stashed changes
        } else {
            currentY = 0;
        }
        if (currentZIndex < z.size() - 1) {
            tempZ = (float) z.get(currentZIndex);
            currentZ = position(tempZ,currentZIndex);
            currentZIndex++;
<<<<<<< Updated upstream
            currentZ = tempZ + (float) z.get(currentZIndex) * delta_time;
=======
//            currentZ = tempZ + (float) z.get(currentZIndex); //* delta_time;
>>>>>>> Stashed changes
        } else {
            currentZ = 0;
        }
        Matrix.setIdentityM(mCube.mModelMatrix, 0);
<<<<<<< Updated upstream
        Matrix.translateM(mCube.mModelMatrix, 0, currentX / 10000, currentY / 10000, currentZ / 10000);
=======
        Matrix.translateM(mCube.mModelMatrix, 0, currentX, currentY, currentZ);

    }


    public float position(float param, int length){
        float len = (float)Math.sqrt(length)/120;
        float retValue = (param*len)/2;
        return retValue;
    }
    public void rotateCamera() {
//        float mAngle = 30.0f;
//        float mRadius = 2.0f;
//
//        float x = (float) (mRadius * Math.cos(mAngle));
//        float y = (float) (mRadius * Math.sin(mAngle));
//
//        Matrix.translateM(mViewMatrix,0,-x,0,-y);
//        Matrix.setLookAtM(mViewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);
        Matrix.setIdentityM(mCube.mModelMatrix,0);
        Matrix.translateM(mCube.mModelMatrix,0,0f,0.8f,3.5f);

        Matrix.setIdentityM(mCurrentRotation,0);
        Matrix.rotateM(mCurrentRotation,0,mDeltaX,0.0f,1.0f,0.0f);
        Matrix.rotateM(mCurrentRotation,0,mDeltaY,1.0f,0.0f,0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;


>>>>>>> Stashed changes

    }
}
