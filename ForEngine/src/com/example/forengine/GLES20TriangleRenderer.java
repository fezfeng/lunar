/*
 * Copyright (FengYe) 2013 The Android Open Source Project
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forengine;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.threed.jpct.util.*;
import com.threed.jpct.*;


class GLES20TriangleRenderer implements GLSurfaceView.Renderer {
	 	private static final int FLOAT_SIZE_BYTES = 4;
	    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
	    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
	   // private static final int TRIANGLE_VERTICES_DATA_COLOR_OFFSET = 3; 
	    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
	    private final float[] mTriangleVerticesData = {
	            // X, Y, Z, U, V
	            -1.0f, -0.5f, 0,
	           
	            -0.5f, 0.0f,
	            
	            1.0f, -0.5f, 0,
	            
	            1.5f, -0.0f,
	            
	            0.0f,  1.11803399f, 0, 
	            
	            0.5f,  1.61803399f 
	            };

	    private FloatBuffer mTriangleVertices;
	    private FloatBuffer mTriangleVertices1;
	    private FloatBuffer mTriangleVertices2;
	    private FloatBuffer mTriangleVertices3;
	    private FloatBuffer mTriangleVertices4;
	    

	    private final String mVertexShader =
	        "uniform mat4 uMVPMatrix;\n" +
	       // "uniform vec3 uLightLocation;\n"+
	        "uniform vec3 uCamera;\n"+
	       // "attribute vec4 a_Color; \n"+
	        "attribute vec3 aPosition;\n" +
	        "attribute vec2 aTextureCoord;\n" +
	        //"varying vec4 v_Color;        \n"+
	        "varying float vDiffuse;\n"+
	        "varying float vSpecular;\n"+
	        
	        "varying float vDiffuse1;\n"+
	        "varying float vSpecular1;\n"+
	        
	        "varying vec2 vTextureCoord;\n" +
	        "void main() {\n" +
	        "  gl_Position = uMVPMatrix * vec4(aPosition,1);\n" +
	        //"  v_Color = a_Color;          \n"+
	        "  vTextureCoord = aTextureCoord;\n" +
	        
	        "vec3 normalTarget = aPosition+normalize(aPosition);\n"+
	        "vec3 newNormal = normalTarget-aPosition;\n"+
	        "newNormal = normalize(newNormal);\n"+
	        "vec3 eye = normalize(uCamera-aPosition);\n"+
	        "vec3 vp = normalize(vec3(0.0,0.0,6.0)-aPosition);\n"+
	        "vp = normalize(vp);\n"+
	        "vec3 halfVector = normalize(vp+eye);\n"+
	        "float shininess = 50.0;\n"+
	        "float nDotViewPosition = max(0.0,dot(newNormal,vp));\n"+
	        "vDiffuse = 1.0*nDotViewPosition;\n"+
	        "float nDotViewHalfVector = dot(newNormal,halfVector);\n"+
	        "float powerFactor = max(0.0,pow(nDotViewHalfVector,shininess));\n"+
	        "vSpecular = 1.0*powerFactor;\n"+
	        
	        "vec3 vp1 = normalize(vec3(0.0,0.0,-6.0)-aPosition);\n"+
	        "vp1 = normalize(vp1);\n"+
	        "vec3 halfVector1 = normalize(vp1+eye);\n"+
	        "float nDotViewPosition1 = max(0.0,dot(newNormal,vp1));\n"+
	        "vDiffuse1 = 1.0*nDotViewPosition1;\n"+
	        "float nDotViewHalfVector1 = dot(newNormal,halfVector1);\n"+
	        "float powerFactor1 = max(0.0,pow(nDotViewHalfVector1,shininess));\n"+
	        "vSpecular1 = 1.0*powerFactor1;\n"+
	       // "vSpecular = vSpecular*0.00000001+uCamera.x*1000.0 ;\n"+
	        
	        "}\n";

	    private final String mFragmentShader =
	        "precision mediump float;\n" +
	        "varying vec2 vTextureCoord;\n" +
	        
	        "varying float vDiffuse;\n"+
	        "varying float vSpecular;\n"+
	        "varying float vDiffuse1;\n"+
	        "varying float vSpecular1;\n"+
	        //"varying vec4 v_Color;      \n"+
	        "uniform sampler2D sTexture;\n" +
	        "void main() {\n" +
	        
	       "  gl_FragColor = texture2D(sTexture, vTextureCoord)*(vDiffuse+vSpecular+vDiffuse1+vSpecular1+0.6);\n" +
	       //"    gl_FragColor = v_Color;\n"+
	       // "  gl_FragColor = vec4(0,0,1,1);\n"+
	       // "gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
	       //"  gl_FragColor = texture2D(sTexture, vTextureCoord)*(vec4(0.5,0.5,0.5,1.0));\n" +
	       //" gl_FragColor = vec4(1.0,1.0,1.0,1.0)*vSpecular*1000.0;\n"+
	        "}\n";

	    private float[] mMVPMatrix = new float[16];
	    private float[] mProjMatrix = new float[16];
	    private float[] mMMatrix = new float[16];
	    private float[] mVMatrix = new float[16];

	    private int mProgram;
	    private int mTextureID;
	    private int muMVPMatrixHandle;
	    private int maPositionHandle;
	    private int maTextureHandle;
	    private int maColorHandle;
	    private int muCameraHandle;
	    
	    private int fps = 0;
	    private long time = System.currentTimeMillis();
	    
	    private Context mContext;
	    private static String TAG = "GLES20TriangleRenderer";
    
	    JScene scene;
	    float[] a,b,c,d,e;
	    float theta;
	    int[] textures;
	public GLES20TriangleRenderer(Context context) throws FileNotFoundException {
        mContext = context;
        scene = new JScene(context);
        scene.DrawScene();
        
       
        
        Log.v("fyf",String.valueOf(scene.length_res));
		
		Log.v("fyf",String.valueOf(scene.length_res1));
		
		Log.v("fyf",String.valueOf(scene.length_res2));
		
		Log.v("fyf",String.valueOf(scene.length_res3));
		
		Log.v("fyf",String.valueOf(scene.length_res4));
		
		mTriangleVertices = ByteBuffer.allocateDirect(scene.length_res * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    mTriangleVertices.put(scene.res).position(0);
	    
	    mTriangleVertices1 = ByteBuffer.allocateDirect(scene.length_res1 * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    mTriangleVertices1.put(scene.res1).position(0);
	    
	    mTriangleVertices2 = ByteBuffer.allocateDirect(scene.length_res2 * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    mTriangleVertices2.put(scene.res2).position(0);
	    
	    mTriangleVertices3 = ByteBuffer.allocateDirect(scene.length_res3 * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    mTriangleVertices3.put(scene.res3).position(0);
	    
	    //mTriangleVertices4 = ByteBuffer.allocateDirect(e.length * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    // mTriangleVertices4.put(e).position(0);*/
    }
	//每一帧调用一次的onDraw函数
    public void onDrawFrame(GL10 glUnused) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
    	/*
    	if(System.currentTimeMillis()-time>=1000){
    		Log.v("fyf", "fps:"+String.valueOf(fps));
    		fps = 0;
    		time = System.currentTimeMillis();
    	}
    	
    	fps++;*/
    	
    	
    	
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");

        //theta +=0.005;
    	//scene.m_vEyePoint = new Vector3D(4.0f*(float)Math.sin(theta),0,4.0f*(float)Math.cos(theta));
    	Matrix.setLookAtM(mVMatrix, 0, scene.m_vEyePoint.x, scene.m_vEyePoint.y, scene.m_vEyePoint.z, scene.m_vGazePoint.x, scene.m_vGazePoint.y, scene.m_vGazePoint.z, scene.m_vUp.x, scene.m_vUp.y, scene.m_vUp.z);
        
    	GLES20.glEnable(GLES20.GL_CULL_FACE);
    	
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        drawTriangle(mTriangleVertices, scene.length_res/5);
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        //drawTriangle(mTriangleVertices, scene.length_res/9);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        drawTriangle(mTriangleVertices1,scene.length_res1/5);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[2]);
        drawTriangle(mTriangleVertices2, scene.length_res2/5);
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[3]);
        drawTriangle(mTriangleVertices3, scene.length_res3/5);
        
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        //drawTriangle(mTriangleVertices4, e.length/9);
        
        //Log.v("fyf",String.valueOf(System.currentTimeMillis()-time));
        
    }
	//绘制三角形函数
    public void drawTriangle(final FloatBuffer mTriangleVertices,int vCount)
    {
    	mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
        
       // mTriangleVertices.position(TRIANGLE_VERTICES_DATA_COLOR_OFFSET);
       // GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false,TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
      
        mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,TRIANGLE_VERTICES_DATA_STRIDE_BYTES, mTriangleVertices);
       
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        //GLES20.glEnableVertexAttribArray(maColorHandle);
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        

      
        Matrix.setIdentityM(mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.frustumM(mProjMatrix, 0, -0.16f, 0.16f, -0.09f, 0.09f, 0.1f, 20);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
       // Matrix.orthoM(mProjMatrix, 0, -2.4f, 2.4f, -1.35f, 1.35f, -1f, 20);
       // Matrix.frustumM(mProjMatrix, 0, -2.4f, 2.4f, -1.35f, 1.35f, 0.1f, 20);
       
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniform3f(muCameraHandle, scene.m_vEyePoint.x, scene.m_vEyePoint.y, scene.m_vEyePoint.z);
        
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }
        
        muCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        checkGlError("glGetUniformLocation uCamera");
        if(muCameraHandle == -1){
        	throw new RuntimeException("Could not get attrib location for uCamera");
        }
        
       // maColorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        /*
         * Create our texture. This has to be done each time the
         * surface is created.
         */

        textures = new int[4];
        GLES20.glGenTextures(4, textures, 0);

        mTextureID = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        InputStream is = mContext.getResources().openRawResource(R.drawable.eastrest);
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch(IOException e) {
                // Ignore.
            }
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        
        
        mTextureID = textures[1];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        InputStream it = mContext.getResources().openRawResource(R.drawable.westrest);
        Bitmap bitmap1;
        try {
            bitmap1 = BitmapFactory.decodeStream(it);
        } finally {
            try {
                it.close();
            } catch(IOException e) {
                // Ignore.
            }
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap1, 0);
        bitmap1.recycle();
        
        mTextureID = textures[2];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        InputStream iu = mContext.getResources().openRawResource(R.drawable.northpole);
        Bitmap bitmap2;
        try {
            bitmap2 = BitmapFactory.decodeStream(iu);
        } finally {
            try {
                iu.close();
            } catch(IOException e) {
                // Ignore.
            }
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap2, 0);
        bitmap2.recycle();
        
        mTextureID = textures[3];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

        InputStream iv = mContext.getResources().openRawResource(R.drawable.southpole);
        Bitmap bitmap3;
        try {
            bitmap3 = BitmapFactory.decodeStream(iv);
        } finally {
            try {
                iv.close();
            } catch(IOException e) {
                // Ignore.
            }
        }
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap3, 0);
        bitmap3.recycle();
        
        
       // Matrix.setLookAtM(mVMatrix, 0, 0, 0, -4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
       // Matrix.setLookAtM(mVMatrix, 0, scene.m_vEyePoint.x, scene.m_vEyePoint.y, scene.m_vEyePoint.z, scene.m_vGazePoint.x, scene.m_vGazePoint.y, scene.m_vGazePoint.z, scene.m_vUp.x, scene.m_vUp.y, scene.m_vUp.z);
    }
	//载入shader
    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }
	//初始化程序
    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
    
    //初始化引擎shader
    public void initEngineShader(){
    	
    	GLSLShader myshader = new GLSLShader(mVertexShader, mFragmentShader);
    	
    }
    
    public void initEngineLight(){
    	
    }
    
    //初始化引擎纹理
    public void initEngineTexture(){
    	
    	Bitmap aBitmap = null,bBitmap = null,cBitmap = null,dBitmap = null;
    	
    	BitmapHelper myBitmapHelper = new BitmapHelper();
    	InputStream is = mContext.getResources().openRawResource(R.drawable.eastrest);
    	InputStream it = mContext.getResources().openRawResource(R.drawable.westrest);
    	InputStream iu = mContext.getResources().openRawResource(R.drawable.northpole);
    	InputStream iv = mContext.getResources().openRawResource(R.drawable.southpole);
    	try {
			 aBitmap = myBitmapHelper.loadImage(is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	Texture aTexture = new Texture(aBitmap);
    	
    	
    	try {
			bBitmap = myBitmapHelper.loadImage(it);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Texture bTexture = new Texture(bBitmap);
    	
    	   try {
			cBitmap = myBitmapHelper.loadImage(iu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	Texture cTexture = new Texture(cBitmap);
    	
    	
    	   try {
			dBitmap = myBitmapHelper.loadImage(iv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Texture dTexture = new Texture(dBitmap);
    }
   
}
