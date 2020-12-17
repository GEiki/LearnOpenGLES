## 基本概念
* PipeLine:渲染管道，GPU处理图形信号的并行处理单元，也叫渲染流程
* Shader：着色器，用于描述如何绘制图形，分为：**顶点着色器**和**片段着色器**
* GLSL：OpenGL编写着色器实现的语言


## OpenGL顶点坐标系
* GL坐标起始位置在屏幕中心
* X轴从左到右取值范围[-1,1]
* Y轴从下到上取值范围[-1,1]

![OpenGL 坐标系](https://upload-images.jianshu.io/upload_images/33019-f6aa55abd8a3cebe.png?imageMogr2/auto-orient/strip|imageView2/2/w/524/format/webp)

## GLSurfaceView
GLSurfaceView是Android提供的用于显示Opengl的视图
* 继承自SurfaceView
* 渲染绘制在单独的线程中
* 通过Renderer接口来回调生命周期和渲染结果

## Renderer接口

    onSurfaceCreate(GL10 gl,EGLConfig config)//Surface创建时回调
    
    onSurfaceChange(GL10 gl,int width,int height)//Surface尺寸改变时回调
    
    onDrawFrame(GL10 gl)//每渲染一帧回调
    
## GLSurfaceView常用API

    setEGLContextClientVersion//设置OpenGL ES版本
    
    onPause//暂停渲染，最好跟随Android的生命周期的onPause调用
    
    setRenderer//设置渲染器
    
    requestRender//异步请求渲染，开始渲染的标志是回调onDrawFrame
    
    queueEvent//插入Runnable到渲染线程中执行

## GLSurfaceView渲染模式

    RENDERMODE_CONTINUOUSLY //不停地渲染
    
    RENDERMODE_WHEN_DIRTY //调用requestRender才会触发渲染回调


## 简单的GLSL

    /**
     * 顶点着色器
     */
    private static final String VERTEX_SHADER = "" +
            // vec4：4个分量的向量：x、y、z、w
            "attribute vec4 a_Position;\n" +
            "void main()\n" +
            "{\n" +
            // gl_Position：GL中默认定义的输出变量，决定了当前顶点的最终位置
            "    gl_Position = a_Position;\n" +
            // gl_PointSize：GL中默认定义的输出变量，决定了当前顶点的大小
            "    gl_PointSize = 40.0;\n" +
            "}";
    
    /**
     * 片段着色器
     */
    private static final String FRAGMENT_SHADER = "" +
            // 定义所有浮点数据类型的默认精度；有lowp、mediump、highp 三种，但只有部分硬件支持片段着色器使用highp。(顶点着色器默认highp)
            "precision mediump float;\n" +
            "uniform mediump vec4 u_Color;\n" +
            "void main()\n" +
            "{\n" +
            // gl_FragColor：GL中默认定义的输出变量，决定了当前片段的最终颜色
            "    gl_FragColor = u_Color;\n" +
            "}";

## 编译着色器

    /**
     * 编译顶点着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }
    
    /**
     * 编译片段着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }
    
    /**
     * 编译片段着色器
     *
     * @param type       着色器类型
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    private static int compileShader(int type, String shaderCode) {
        // 1.创建一个新的着色器对象
        final int shaderObjectId = GLES20.glCreateShader(type);
    
        // 2.获取创建状态
        if (shaderObjectId == 0) {
            // 在OpenGL中，都是通过整型值去作为OpenGL对象的引用。之后进行操作的时候都是将这个整型值传回给OpenGL进行操作。
            // 返回值0代表着创建对象失败。
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader.");
            }
            return 0;
        }
    
        // 3.将着色器代码上传到着色器对象中
        GLES20.glShaderSource(shaderObjectId, shaderCode);
    
        // 4.编译着色器对象
        GLES20.glCompileShader(shaderObjectId);
    
        // 5.获取编译状态：OpenGL将想要获取的值放入长度为1的数组的首位
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
    
        if (LoggerConfig.ON) {
            // 打印编译的着色器信息
            Log.v(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:"
                    + GLES20.glGetShaderInfoLog(shaderObjectId));
        }
    
        // 6.验证编译状态
        if (compileStatus[0] == 0) {
            // 如果编译失败，则删除创建的着色器对象
            GLES20.glDeleteShader(shaderObjectId);
    
            if (LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed.");
            }
    
            // 7.返回着色器对象：失败，为0
            return 0;
        }
    
        // 7.返回着色器对象：成功，非0
        return shaderObjectId;
    }