package com.huya.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:guoyiqu
 * date:2019/9/18
 **/
public class MyGLSurfaceView extends GLSurfaceView {
    private Triangle mTriangle;
    private float angle = 0.0f;
    private Handler handler;
    private Icosahedron mIcosahedron;
    static float triangleCoords[] = { //顶点坐标
            0.0f,0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.5f,0.0f,
    };
    float color[] = {255,0,0,1.0f};//R,G,B,透明度
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    public MyGLSurfaceView(final Context context) {
        super(context);
        handler = new Handler(context.getMainLooper());
        setEGLContextClientVersion(2);
        setRenderer(new Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
                GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);
                mTriangle = new Triangle(triangleCoords,color);

            }

            @Override
            public void onSurfaceChanged(GL10 gl10, int width, int height) {
                GLES20.glViewport(0,0,width,height);
                float ratio = (float)width/height;
                Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
            }

            @Override
            public void onDrawFrame(GL10 gl10) {
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
                long time = SystemClock.uptimeMillis() % 4000L;
                float angle = 0.090f * ((int) time);
                // Set the camera position (View matrix)
                Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

                // Calculate the projection and view transformation
                Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
                Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
                Matrix.multiplyMM(mMatrix, 0, mMatrix, 0, mRotationMatrix, 0);
                mTriangle.setMatrix(mMatrix);
                mTriangle.onDraw();

            }
        });
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
