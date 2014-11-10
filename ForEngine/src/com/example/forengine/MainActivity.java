package com.example.forengine;



import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings.ZoomDensity;

import java.io.File;
import java.io.FileNotFoundException;


public class MainActivity extends Activity {

    	private BasicGLSurfaceView mView;
    	float x1=0.0f,x2=0.0f;
	float y1=0.0f,y2=0.0f;
	float difx=0.0f,dify=0.0f;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	float oldDist = 1.0f;
	

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        try {
			mView = new BasicGLSurfaceView(getApplication());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setContentView(mView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }
    
    private float spacing(MotionEvent e){
    	float x = e.getX(0)-e.getX(1);
    	float y = e.getY(0)-e.getY(1);
    	return FloatMath.sqrt(x*x+y*y);
    }
    
    
    public boolean onTouchEvent(MotionEvent e){
		//float x1=0.0f,x2=0.0f;
		//float y1=0.0f,y2=0.0f;
		//float difx=0.0f,dify=0.0f;
		if(e.getAction()==MotionEvent.ACTION_DOWN){
			//Log.v("fyf","ACTION_DOWN");
		
			x1 = e.getX();
			y1 = e.getY();
			//Log.v("fyf",String.valueOf(x1));
		}
		if(e.getAction()==MotionEvent.ACTION_UP){
			//Log.v("fyf","ACTION_UP");
			/*x2 = e.getX();
			y2 = e.getY();
			difx = x2-x1;
			dify = y2-y1;
			renderer.scene.Rotate(difx/1000, dify/1000);
			mGLSurfaceView.requestRender();*/
		}
		if(e.getAction()==MotionEvent.ACTION_MOVE){
			//Log.v("fyf","ACTION_MOVE");
			x2 = e.getX();
			y2 = e.getY();
			difx = x2-x1;
			dify = y2-y1;
			/*if(difx>0){
				//Log.v("fyf", "Positive");
				//Log.v("fyf",String.valueOf(x2)+" "+String.valueOf(x1));
				
			}
			else{
				//Log.v("fyf","Negtive");
			}*/
			mView.myRenderer.scene.Rotate(-difx/9000, dify/9000);
			mView.requestRender();
			
			if(mode == ZOOM){
				float newDist = spacing(e);
				float Scale = (oldDist/newDist);
				Log.v("fyf",String.valueOf(mView.myRenderer.scene.m_dDistance));
				
				if(mView.myRenderer.scene.m_dDistance*Scale<=7&&mView.myRenderer.scene.m_dDistance*Scale>=1.2)
				{
					Log.v("fyf",String.valueOf(mView.myRenderer.scene.m_dDistance*Scale));
					mView.myRenderer.scene.m_vEyePoint.x*=Scale;
					mView.myRenderer.scene.m_vEyePoint.y*=Scale;
					mView.myRenderer.scene.m_vEyePoint.z*=Scale;
					mView.myRenderer.scene.m_dDistance*=Scale;
					mView.requestRender();
				}
			}
		}
		
		if(e.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN)
		{
			/*
			float x = e.getX(0)-e.getX(1);
			float y = e.getY(0)-e.getY(1);
			float space = FloatMath.sqrt(x*x+y*y);
			Log.v("fyf",String.valueOf(space));*/
			oldDist = this.spacing(e);
			mode = ZOOM;
		}
		
		if(e.getActionMasked()==MotionEvent.ACTION_POINTER_UP){
			mode = NONE;
		}
		
		return true;
	}
}
