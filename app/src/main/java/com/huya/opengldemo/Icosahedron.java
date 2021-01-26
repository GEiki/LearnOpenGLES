package com.huya.opengldemo;

import com.huya.opengldemo.utils.Util;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * author:guoyiqu
 * date:2019/9/29
 **/
public class Icosahedron {
    private int mProgram;


    static final float X=.525731112119133606f;
    static final float Z=.850650808352039932f;
    static float vertices[] = new float[]{
            -X, 0.0f, Z, X, 0.0f, Z, -X, 0.0f, -Z, X, 0.0f, -Z,
            0.0f, Z, X, 0.0f, Z, -X, 0.0f, -Z, X, 0.0f, -Z, -X,
            Z, X, 0.0f, -Z, X, 0.0f, Z, -X, 0.0f, -Z, -X, 0.0f
    };
    static short indices[] = new short[]{
            0,4,1, 0,9,4, 9,5,4, 4,5,8, 4,8,1,
            8,10,1, 8,3,10, 5,3,8, 5,2,3, 2,7,3,
            7,10,3, 7,6,10, 7,11,6, 11,0,6, 0,1,6,
            6,1,10, 9,0,11, 9,11,2, 9,2,5, 7,2,11 };

    float[] colors = {
            0f, 0f, 0f, 1f,
            0f, 0f, 1f, 1f,
            0f, 1f, 0f, 1f,
            0f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,
            1f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f,
            1f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f,
            1f, 0f, 1f, 1f
    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer ;
    private ShortBuffer indexBuffer ;

    public Icosahedron() {
        vertexBuffer = Util.getFloatBuffer(vertices);
        colorBuffer = Util.getFloatBuffer(colors);
        indexBuffer = Util.getShortBuffer(indices);

//        int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER,Util.VERTEX_SHADER);//加载顶点着色器
//        int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER,Util.FRAGMENT_SHADER);//加载片段着色器
//        mProgram = GLES20.glCreateProgram();//创建openGL es程序
//        GLES20.glAttachShader(mProgram,vertexShader);//连接顶点着色器
//        GLES20.glAttachShader(mProgram,fragmentShader);//连接片段着色器
//        GLES20.glLinkProgram(mProgram);//创建openGL es可执行文件
    }

    public void onDraw(GL10 gl) {
//        GLES20.glUseProgram(mProgram);//加载opengl es 程序
        gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -4);

        float angle=0;
        gl.glRotatef(angle, 0, 1, 0);

        gl.glFrontFace(GL10.GL_CCW);

        gl.glEnable(GL10.GL_CULL_FACE);

        gl.glCullFace(GL10.GL_BACK);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glDisable(GL10.GL_CULL_FACE);
        angle++;
    }

}
