package com.huya.opengldemo.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * author:guoyiqu
 * date:2019/9/29
 **/
public class Util {
    public static final String VERTEX_SHADER = //顶点着色器代码
            "uniform mat4 vMatrix;"+
            "attribute vec4 vPosition;"+
                    "void main(){"+
                    "gl_Position = vMatrix * vPosition;"+
                    "}";
    public static final String FRAGMENT_SHADER =//片段着色器代码
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main(){"+
                    "gl_FragColor = vColor;" +
                    "}";

    public static float[] mMatrix =
            {1,0,0,0,

            0,1,0,0,

            0,0,1,0,

            0,0,0,1};

    public static float[] rotate(float angle,float x,float y,float z){
        float[] matrix = Arrays.copyOf(mMatrix,16);
        Matrix.rotateM(matrix,0,angle,x,y,z);
        return matrix;
    }


    public static ShortBuffer getShortBuffer(short[] array){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length *2);
        byteBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(array);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public static FloatBuffer getFloatBuffer(float[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length *4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }

    /**
     * 加载着色器（shader）
     * */
    public static int loadShader(int type,String shaderCode){
        int shader = GLES20.glCreateShader(type); //创建着色器类型
        GLES20.glShaderSource(shader,shaderCode);//添加着色器代码
        GLES20.glCompileShader(shader);//编译着色器代码
        return shader;
    }
}
