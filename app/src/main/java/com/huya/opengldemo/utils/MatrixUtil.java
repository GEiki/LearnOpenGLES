package com.huya.opengldemo.utils;

import android.opengl.Matrix;

/**
 * author:guoyiqu
 * date:2021/1/26
 **/
public class MatrixUtil {
    //透视投影矩阵
    private static final float[] mProjMatrix = new float[16];
    //摄像机矩阵
    private static final float[] mVMatrix = new float[16];
    //物体变换矩阵
    private static final float[] mMVPMatrix = new float[16];


    //设置摄像机
    public static void setCamera(float cx,float cy,float cz, // 摄像机位置
                                 float dx,float dy,float dz, // 摄像机目标位置
                                 float ux,float uy,float uz) //摄像机up向量分量
    {
        Matrix.setLookAtM(mVMatrix,0,cx,cy,cz,dx,dy,dz,ux,uy,uz);
    }

    //设置透视投影矩阵
    public static void setProjectFrustum(float left,float right,float bottom,float top,//near面的大小
                                         float near,//到near面的距离
                                         float far) //到far面的距离
    {
        Matrix.frustumM(mProjMatrix,0,left,right,bottom,top,near,far);
    }


    public static float[] getFinalMatrix() {
        Matrix.multiplyMM(mMVPMatrix,0,mProjMatrix,0,mVMatrix,0);
        return mMVPMatrix;
    }

}
