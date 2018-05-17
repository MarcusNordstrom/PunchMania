package punchmania.punchmania;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class PunchRenderer implements GLSurfaceView.Renderer {
    private final float TOUCH_SCALE_FACTOR = 0.6f;
    public float mAngleX = 0.0f;
    public float mAngleY = 0.0f;
    public float mAngleZ = 0.0f;
    public int mWidth, mHeight;
    public float prevX = 0, prevY = 0;
    public float xPos, yPos;
    private int mActivePointerID = 0;
    private Context mContext;
    private FloatBuffer mVertexBuffer = null;
    private ShortBuffer mTriangleBorderIndicesBuffer = null;
    private int mNumOfTriangleBorderIndices = 0;
    private ArrayList<ArrayList<Integer>> arrList;
    private ArrayList<Integer> x, y, z;
    private float mPreviousX;
    private float mPreviousY;
    private int size;

    public PunchRenderer(Context context) {
        arrList = MainActivity.getHighScoreDetails();
        x = arrList.get(0);
        y = arrList.get(1);
        z = arrList.get(2);
        mContext = context;
    }

    public void onDrawFrame(GL10 gl) {


        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef( xPos / 500, -(yPos / 500), -3.0f);
//        gl.glRotatef(mAngleX, 1, 0, 0);
//        gl.glRotatef(mAngleY, 0, 1, 0);
//        gl.glRotatef(mAngleZ, 0, 0, 1);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);

        // Draw all lines
//        gl.glDrawElements(GL10.GL_LINE_STRIP, mNumOfTriangleBorderIndices,
//                GL10.GL_UNSIGNED_SHORT, mTriangleBorderIndicesBuffer);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, size);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Get all the buffers ready
        setAllBuffers();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        gl.glViewport(0, 0, width, height);
        float aspect = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-aspect, aspect, -1.0f, 1.0f, 1.0f, 10.0f);
    }

    private void setAllBuffers() {
        // Set vertex buffer
        int arraySize = 0;
        if (x.size() < y.size() && x.size() < z.size()) {
            arraySize = x.size();
        } else if (y.size() < x.size() && y.size() < z.size()) {
            arraySize = y.size();
        } else {
            arraySize = z.size();
        }
        size = arraySize;
        float vertexlist[] = new float[arraySize * 3];
        fillVertexArray(vertexlist);
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexlist.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertexlist);
        mVertexBuffer.position(0);

        // Set triangle border buffer with vertex indices
        short trigborderindexlist[] = {
                4, 0, 4, 1, 4, 2, 4, 3, 0, 1, 1, 3, 3, 2, 2, 0, 0, 3
        };
        mNumOfTriangleBorderIndices = trigborderindexlist.length;
        ByteBuffer tbibb = ByteBuffer.allocateDirect(trigborderindexlist.length * 2);
        tbibb.order(ByteOrder.nativeOrder());
        mTriangleBorderIndicesBuffer = tbibb.asShortBuffer();
        mTriangleBorderIndicesBuffer.put(trigborderindexlist);
        mTriangleBorderIndicesBuffer.position(0);
    }

    public boolean onTouchEvent(MotionEvent e) {
        final int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                int pointerIndex = e.getActionIndex();
                final float x = e.getX(pointerIndex);
                final float y = e.getY(pointerIndex);
                mPreviousX = x;
                mPreviousY = y;

                mActivePointerID = e.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = e.findPointerIndex(mActivePointerID);
                final float x = e.getX(pointerIndex);
                final float y = e.getY(pointerIndex);

                final float dx = x - mPreviousX;
                final float dy = y - mPreviousY;

                xPos += dx;
                yPos += dy;

                mPreviousX = x;
                mPreviousY = y;

                break;
            }
            case MotionEvent.ACTION_POINTER_UP:{
                final int pointerIndex = e.getActionIndex();
                final int pointerID = e.getPointerId(pointerIndex);

                if(pointerID == mActivePointerID){
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPreviousX = e.getX(newPointerIndex);
                    mPreviousY = e.getY(newPointerIndex);
                    mActivePointerID = e.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    public void fillVertexArray(float[] floatArray) {
        float dx = 0, dy = 0, dz = 0;
        int index = 0;
        for (int i = 0; i < (floatArray.length / 3); i++) {
            index = 1 + (i * 3);
            dx += (float) x.get(i);
            dy += (float) y.get(i);
            dz += (float) z.get(i);
            floatArray[index - 1] = dx / 1000;
            floatArray[index] = dy / 1000;
            floatArray[index + 1] = dz / 1000;
        }
        String print = "\n";
        for (int i = 0; i < floatArray.length; i += 3) {
            print += floatArray[i] + " " + floatArray[i + 1] + " " + floatArray[i + 2] + "\n";
        }
        Log.i("Array", print);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

    }
}


