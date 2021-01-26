package com.huya.opengldemo.shape;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.huya.opengldemo.utils.MatrixUtil;
import com.huya.opengldemo.utils.Util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * author:guoyiqu
 * date:2021/1/26
 **/
public class Cube {
    private static final String TAG = "Cube";
    private static final String U_MATRIX = "u_Matrix";
    private static final String V_POSITION = "vPosition";
    private static final String A_COLOR = "a_Color";

    private final int mProgram;

    private final String vertexShaderCode = //顶点着色器代码
            "attribute vec4 vPosition;"+
                    "uniform mat4 u_Matrix;"+
                    "attribute vec4 a_Color;"+
                    "varying vec4 v_Color;"+
                    "void main(){"+
                    "gl_Position = u_Matrix * vPosition;"+
                    "v_Color = a_Color;"+
                    "}";
    private final String fragmentShaderCode =//片段着色器代码
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "void main(){"+
                    "gl_FragColor = v_Color;" +
                    "}";

    private FloatBuffer vertexBuffer; //顶点坐标buffer
    private FloatBuffer colorBuffer;

    static final int COORDS_PER_VERTEX = 3;//每个顶点的坐标数

    float[] color = {255,0,0,1.0f};//R,G,B,透明度

    private int colorHandle;
    private int positionHandle;
    private int uMatrixLocation;

    private  int vertexCount = 12*6;//顶点数
    private final int vertexStride = 0;//顶点步幅 float为4字节




    static float vertices[] = {//前面
            0,0,1.0f,  1.0f,1.0f,1.0f,   -1.0f,1.0f,1.0f,
            0,0,1.0f,  -1.0f,1.0f,1.0f,  -1.0f,-1.0f,1.0f,
            0,0,1.0f,  -1.0f,-1.0f,1.0f,  1.0f,-1.0f,1.0f,
            0,0,1.0f,  1.0f,-1.0f,1.0f,   1.0f,1.0f,1.0f,
            //后面
            0,0,-1.0f,  1.0f,1.0f,-1.0f,   1.0f,-1.0f,-1.0f,
            0,0,-1.0f,  1.0f,-1.0f,-1.0f,  -1.0f,-1.0f,-1.0f,
            0,0,-1.0f,  -1.0f,-1.0f,-1.0f,  -1.0f,1.0f,-1.0f,
            0,0,-1.0f,  -1.0f,1.0f,-1.0f,  1.0f,1.0f,-1.0f,
            //左面
            -1.0f,0,0,  -1.0f,1.0f,1.0f,   -1.0f,1.0f,-1.0f,
            -1.0f,0,0,  -1.0f,1.0f,-1.0f,  -1.0f,-1.0f,-1.0f,
            -1.0f,0,0,  -1.0f,-1.0f,-1.0f, -1.0f,-1.0f,1.0f,
            -1.0f,0,0,  -1.0f,-1.0f,1.0f,  -1.0f,1.0f,1.0f,
            //右面
            1.0f,0,0,  1.0f,1.0f,1.0f,   1.0f,-1.0f,1.0f,
            1.0f,0,0,  1.0f,-1.0f,1.0f,  1.0f,-1.0f,-1.0f,
            1.0f,0,0,  1.0f,-1.0f,-1.0f,  1.0f,1.0f,-1.0f,
            1.0f,0,0,  1.0f,1.0f,-1.0f,  1.0f,1.0f,1.0f,
            //上面
            0,1.0f,0,  1.0f,1.0f,1.0f,   1.0f,1.0f,-1.0f,
            0,1.0f,0,  1.0f,1.0f,-1.0f,  -1.0f,1.0f,-1.0f,
            0,1.0f,0,  -1.0f,1.0f,-1.0f,  -1.0f,1.0f,1.0f,
            0,1.0f,0,  -1.0f,1.0f,1.0f,   1.0f,1.0f,1.0f,
            //下面
            0,-1.0f,0,  1.0f,-1.0f,1.0f,  -1.0f,-1.0f,1.0f,
            0,-1.0f,0,  -1.0f,-1.0f,1.0f,  -1.0f,-1.0f,-1.0f,
            0,-1.0f,0,  -1.0f,-1.0f,-1.0f, 1.0f,-1.0f,-1.0f,
            0,-1.0f,0,  1.0f,-1.0f,-1.0f,  1.0f,-1.0f,1.0f
    };

    static float colors[] = new float[] {
            //前面
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            //后面
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            //左面
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            //右面
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            //上面
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            //下面
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,

    };


    public Cube() {

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        colorBuffer = ByteBuffer.allocateDirect(colors.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
        int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);//加载顶点着色器
        int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);//加载片段着色器
        mProgram = GLES20.glCreateProgram();//创建openGL es程序
        GLES20.glAttachShader(mProgram,vertexShader);//连接顶点着色器
        GLES20.glAttachShader(mProgram,fragmentShader);//连接片段着色器
        GLES20.glLinkProgram(mProgram);//创建openGL es可执行文件
        positionHandle = GLES20.glGetAttribLocation(mProgram,V_POSITION);//获取位置句柄
        colorHandle = GLES20.glGetAttribLocation(mProgram,A_COLOR);//获取颜色句柄
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram,U_MATRIX);//获取矩阵句柄
    }

    public void onDraw() {
        GLES20.glUseProgram(mProgram);//加载opengl es 程序

        GLES20.glEnableVertexAttribArray(positionHandle);//启用位置句柄
        // 关联顶点坐标属性和缓存数据
        GLES20.glVertexAttribPointer(positionHandle, // 1. 位置索引；
                COORDS_PER_VERTEX,// 2. 每个顶点属性需要关联的分量个数(必须为1、2、3或者4。初始值为4。)；
                GLES20.GL_FLOAT,// 3. 数据类型；
                false,// 4. 指定当被访问时，固定点数据值是否应该被归一化(GL_TRUE)或者直接转换为固定点值(GL_FALSE)(只有使用整数数据时)
                vertexStride,// 5. 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0。
                vertexBuffer);// 6. 数据缓冲区

        //传入颜色数据
        GLES20.glEnableVertexAttribArray(colorHandle);
        GLES20.glVertexAttribPointer(colorHandle,4,GLES20.GL_FLOAT,false,0,colorBuffer);

        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false, MatrixUtil.getFinalMatrix(),0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);//绘制三角形
        GLES20.glDisableVertexAttribArray(positionHandle);//禁用位置句柄
        GLES20.glDisableVertexAttribArray(colorHandle);//禁用颜色句柄

    }
}
