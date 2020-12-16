package com.huya.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.huya.opengldemo.Util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * author:guoyiqu
 * date:2019/9/18
 **/
public class Triangle {
    private final int mProgram;

    private final String vertexShaderCode = //顶点着色器代码
            "uniform mat4 vMatrix;"+
            "attribute vec4 vPosition;"+
            "void main(){"+
            "gl_Position = vMatrix * vPosition;"+
            "}";
    private final String fragmentShaderCode =//片段着色器代码
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main(){"+
            "gl_FragColor = vColor;" +
            "}";

    private FloatBuffer vertexBuffer; //顶点坐标buffer

    static final int COORDS_PER_VERTEX = 2;//每个顶点的坐标数

    static float triangleCoords[];
    private float[] matrix;
    private int hMatrix;

    float[] color = {255,0,0,1.0f};//R,G,B,透明度

    private int colorHandle;
    private int positionHandle;

    private  int vertexCount ;//顶点数
    private final int vertexStride = COORDS_PER_VERTEX * 4;//顶点步幅 float为4字节

    public Triangle(float[] coords,float[] colors) {
        triangleCoords = coords;
        this.color = colors;
        vertexCount =  triangleCoords.length/COORDS_PER_VERTEX;

        //将坐标转换为floatBuffer
        vertexBuffer = Util.getFloatBuffer(triangleCoords);

        int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);//加载顶点着色器
        int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);//加载片段着色器
        mProgram = GLES20.glCreateProgram();//创建openGL es程序
        GLES20.glAttachShader(mProgram,vertexShader);//连接顶点着色器
        GLES20.glAttachShader(mProgram,fragmentShader);//连接片段着色器
        GLES20.glLinkProgram(mProgram);//创建openGL es可执行文件
        hMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        positionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");//获取位置句柄
        colorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");//获取颜色句柄




    }

    public void onDraw() {
        GLES20.glUseProgram(mProgram);//加载opengl es 程序

        GLES20.glEnableVertexAttribArray(positionHandle);//启用位置句柄
        if(matrix!=null){
            GLES20.glUniformMatrix4fv(hMatrix,1,false,matrix,0);
        }
        // 关联顶点坐标属性和缓存数据
        GLES20.glVertexAttribPointer(positionHandle, // 1. 位置索引；
                COORDS_PER_VERTEX,// 2. 每个顶点属性需要关联的分量个数(必须为1、2、3或者4。初始值为4。)；
                GLES20.GL_FLOAT,// 3. 数据类型；
                false,// 4. 指定当被访问时，固定点数据值是否应该被归一化(GL_TRUE)或者直接转换为固定点值(GL_FALSE)(只有使用整数数据时)
                vertexStride,// 5. 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0。
                vertexBuffer);// 6. 数据缓冲区
        GLES20.glUniform4fv(colorHandle,1,color,0);//设置颜色

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vertexCount);//绘制三角形
        GLES20.glDisableVertexAttribArray(positionHandle);//禁用位置句柄
        GLES20.glDisableVertexAttribArray(colorHandle);//禁用颜色句柄

    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }




}
