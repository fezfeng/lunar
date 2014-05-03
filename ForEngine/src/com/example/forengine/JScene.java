package com.example.forengine;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;




public class JScene {
	boolean 	m_bMesh;
	int 		m_nWidth;
	int		 	m_nHeight;
	JMesh 		m_pCurrentMesh;
	Vector3D 	m_vEyePoint;
	Vector3D	m_vGazePoint;
	float		m_dDistance;
	Vector3D	m_vUp;
	Vector3D	m_vFront;
	
	Vector3D 	upN;
	float 		upD;
	Vector3D 	downN;
	float 		downD;
	Vector3D 	leftN;
	float		leftD;
	Vector3D 	rightN;
	float       rightD;
	
	JLabelPoint[] LPoints;
	
	float 		beta;
	float  		gama;
	
	float[] 	res;
	int 		length_res;
	
	float[]		res1;
	int 		length_res1;
	
	float[]		res2;
	int 		length_res2;
	
	float[]		res3;
	int			length_res3;
	
	float[]     res4;
	int			length_res4;
	
	int[]		textures;
	int 		notinanytexture;
	Context     context;
	
	Camera      myCamera;
	
	
	public JScene(Context context) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		this.context  =  context;
		m_vEyePoint = new Vector3D(0.0f,0.0f,-4f);//m_vEyePoint = Vector3D(0.0,0.0,-5.5); -5.5,0.0,0
		m_dDistance = 4.0f;//5.5
		m_vGazePoint = new Vector3D(0.0f,0.0f,0.0f);
		m_vUp = new Vector3D(0.0f,1.0f,0.0f);//(0.0,1.0,0.0);   (  1.0,0.0,0.0); 
		m_vFront = new Vector3D(0.0f,0.0f,-1.0f);////  0 0 -1    -1.0,0.0,0.0
		
		
		//jpct-camera
		/*
		myCamera.setPosition(0.0f, 0.0f, -4.0f);
		myCamera.lookAt(new SimpleVector(0.0f,0.0f,0.0f));
		myCamera.setOrientation(new SimpleVector(0.0f,0.0f,0.0f), new SimpleVector(0.0f,1.0f,0.0f));
		*/
		beta = 0.0f;
		gama = 0.0f;
		this.LPoints = new JLabelPoint[468];
		JMesh Mesh = new JMesh(context);
		Mesh.LoadMesh(this);
		Mesh.InitializeMesh();
		Mesh.ConstructCodeTree();
		Mesh.ConstructCodeTree();
		Mesh.ConstructCodeTree();
		Mesh.ConstructCodeTree();
		Mesh.ConstructCodeTree();
		//Mesh.ConstructCodeTree();
		//Mesh.ConstructCodeTree();
		AddMesh(Mesh);
	}
	public boolean AddMesh(JMesh pNewMesh){
		m_pCurrentMesh = pNewMesh;
		m_bMesh = true;
		return true;
		
	}
	//绘制网格函数
	public void DrawMesh(JMesh pMesh){
		int vIndex1,vIndex2,vIndex3,vIndex4;
		int jetMatIndex;
		int topoIndex;
		/*
		res = new float[385680];
		length_res = 0;
		res1 = new float[416700];
		length_res1 = 0;
		res2 = new float[336900];
		length_res2 = 0;
		res3 = new float[335280];
		length_res3 = 0;
		res4 = new float[1];
		length_res4 = 0;
		*/
		
		//res = new float[200620];
		res = new float[100620];
		length_res = 0;
		res1 = new float[108270];
		length_res1 = 0;
		res2 = new float[80010];
		length_res2 = 0;
		res3 = new float[79740];
		length_res3 = 0;
		res4 = new float[1];
		length_res4 = 0;
		
		
		for(int k = 0;k < 48;k++){
			//if(pMesh.m_Face48[k].m_bInview==false) continue;
			for(int i = 0;i < 256;i++){
				boolean fenge = false;
				int mapId = 0;
				float Max = 0.0f;
				float Min = 1.0f;
				
				vIndex1 = pMesh.m_FaceList11[(256*k+i)*4];
				vIndex2 = pMesh.m_FaceList11[(256*k+i)*4+1];
				vIndex3 = pMesh.m_FaceList11[(256*k+i)*4+2];
				vIndex4 = pMesh.m_FaceList11[(256*k+i)*4+3];
				
				if(pMesh.VertexTopoListList11[vIndex1].eastrestU<1.001f&&pMesh.VertexTopoListList11[vIndex1].eastrestV<1.001f
				   &&pMesh.VertexTopoListList11[vIndex2].eastrestU<1.001f&&pMesh.VertexTopoListList11[vIndex2].eastrestV<1.001f
				   &&pMesh.VertexTopoListList11[vIndex3].eastrestU<1.001f&&pMesh.VertexTopoListList11[vIndex3].eastrestV<1.001f
				   &&pMesh.VertexTopoListList11[vIndex4].eastrestU<1.001f&&pMesh.VertexTopoListList11[vIndex4].eastrestV<1.001f)
				{
					mapId = 1;
				}
				else if(pMesh.VertexTopoListList11[vIndex1].westrestU<1.001f&&pMesh.VertexTopoListList11[vIndex1].westrestV<1.001f
						&&pMesh.VertexTopoListList11[vIndex2].westrestU<1.001f&&pMesh.VertexTopoListList11[vIndex2].westrestV<1.001f
						&&pMesh.VertexTopoListList11[vIndex3].westrestU<1.001f&&pMesh.VertexTopoListList11[vIndex3].westrestV<1.001f
						&&pMesh.VertexTopoListList11[vIndex4].westrestU<1.001f&&pMesh.VertexTopoListList11[vIndex4].westrestV<1.001f)
				{
					mapId = 2;
				}
				else if(pMesh.VertexTopoListList11[vIndex1].northU<1.001f&&pMesh.VertexTopoListList11[vIndex1].northV<1.001f
						&&pMesh.VertexTopoListList11[vIndex2].northU<1.001f&&pMesh.VertexTopoListList11[vIndex2].northV<1.001f
						&&pMesh.VertexTopoListList11[vIndex3].northU<1.001f&&pMesh.VertexTopoListList11[vIndex3].northV<1.001f
						&&pMesh.VertexTopoListList11[vIndex4].northU<1.001f&&pMesh.VertexTopoListList11[vIndex4].northV<1.001f)
				{
					mapId = 3;
				}
				else if(pMesh.VertexTopoListList11[vIndex1].southU<1.001f&&pMesh.VertexTopoListList11[vIndex1].southV<1.001f
						&&pMesh.VertexTopoListList11[vIndex2].southU<1.001f&&pMesh.VertexTopoListList11[vIndex2].southV<1.001f
						&&pMesh.VertexTopoListList11[vIndex3].southU<1.001f&&pMesh.VertexTopoListList11[vIndex3].southV<1.001f
						&&pMesh.VertexTopoListList11[vIndex4].southU<1.001f&&pMesh.VertexTopoListList11[vIndex4].southV<1.001f)
				{
					mapId = 4;
				}
				
				
				
				if(mapId ==1 ){
				res[length_res] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
				res[length_res+1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
				res[length_res+2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
				res[length_res+3] = pMesh.VertexTopoListList11[vIndex1].eastrestV;
				res[length_res+4] = pMesh.VertexTopoListList11[vIndex1].eastrestU;
				
				res[length_res+5] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.x;
				res[length_res+6] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.y;
				res[length_res+7] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.z;
				res[length_res+8] = pMesh.VertexTopoListList11[vIndex2].eastrestV;
				res[length_res+9] = pMesh.VertexTopoListList11[vIndex2].eastrestU;
				
				res[length_res+10] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
				res[length_res+11] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
				res[length_res+12] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
				res[length_res+13] = pMesh.VertexTopoListList11[vIndex3].eastrestV;
				res[length_res+14] = pMesh.VertexTopoListList11[vIndex3].eastrestU;
				
				res[length_res+15] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
				res[length_res+16] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
				res[length_res+17] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
				res[length_res+18] = pMesh.VertexTopoListList11[vIndex3].eastrestV;
				res[length_res+19] = pMesh.VertexTopoListList11[vIndex3].eastrestU;
				
				res[length_res+20] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.x;
				res[length_res+21] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.y;
				res[length_res+22] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.z;
				res[length_res+23] = pMesh.VertexTopoListList11[vIndex4].eastrestV;
				res[length_res+24] = pMesh.VertexTopoListList11[vIndex4].eastrestU;
				
				res[length_res+25] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
				res[length_res+26] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
				res[length_res+27] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
				res[length_res+28] = pMesh.VertexTopoListList11[vIndex1].eastrestV;
				res[length_res+29] = pMesh.VertexTopoListList11[vIndex1].eastrestU;
				
				length_res+=30;
				}
				if(mapId == 2){
					res1[length_res1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res1[length_res1+1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res1[length_res1+2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res1[length_res1+3] = pMesh.VertexTopoListList11[vIndex1].westrestV;
					res1[length_res1+4] = pMesh.VertexTopoListList11[vIndex1].westrestU;
					
					res1[length_res1+5] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.x;
					res1[length_res1+6] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.y;
					res1[length_res1+7] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.z;
					res1[length_res1+8] = pMesh.VertexTopoListList11[vIndex2].westrestV;
					res1[length_res1+9] = pMesh.VertexTopoListList11[vIndex2].westrestU;
					
					res1[length_res1+10] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res1[length_res1+11] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res1[length_res1+12] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res1[length_res1+13] = pMesh.VertexTopoListList11[vIndex3].westrestV;
					res1[length_res1+14] = pMesh.VertexTopoListList11[vIndex3].westrestU;
					
					res1[length_res1+15] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res1[length_res1+16] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res1[length_res1+17] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res1[length_res1+18] = pMesh.VertexTopoListList11[vIndex3].westrestV;
					res1[length_res1+19] = pMesh.VertexTopoListList11[vIndex3].westrestU;
					
					res1[length_res1+20] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.x;
					res1[length_res1+21] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.y;
					res1[length_res1+22] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.z;
					res1[length_res1+23] = pMesh.VertexTopoListList11[vIndex4].westrestV;
					res1[length_res1+24] = pMesh.VertexTopoListList11[vIndex4].westrestU;
					
					res1[length_res1+25] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res1[length_res1+26] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res1[length_res1+27] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res1[length_res1+28] = pMesh.VertexTopoListList11[vIndex1].westrestV;
					res1[length_res1+29] = pMesh.VertexTopoListList11[vIndex1].westrestU;
					
					length_res1+=30;
						
				}
				if(mapId ==3){
					res2[length_res2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res2[length_res2+1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res2[length_res2+2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res2[length_res2+3] = pMesh.VertexTopoListList11[vIndex1].northV;
					res2[length_res2+4] = pMesh.VertexTopoListList11[vIndex1].northU;
					
					res2[length_res2+5] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.x;
					res2[length_res2+6] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.y;
					res2[length_res2+7] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.z;
					res2[length_res2+8] = pMesh.VertexTopoListList11[vIndex2].northV;
					res2[length_res2+9] = pMesh.VertexTopoListList11[vIndex2].northU;
					
					res2[length_res2+10] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res2[length_res2+11] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res2[length_res2+12] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res2[length_res2+13] = pMesh.VertexTopoListList11[vIndex3].northV;
					res2[length_res2+14] = pMesh.VertexTopoListList11[vIndex3].northU;
					
					res2[length_res2+15] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res2[length_res2+16] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res2[length_res2+17] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res2[length_res2+18] = pMesh.VertexTopoListList11[vIndex3].northV;
					res2[length_res2+19] = pMesh.VertexTopoListList11[vIndex3].northU;
					
					res2[length_res2+20] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.x;
					res2[length_res2+21] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.y;
					res2[length_res2+22] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.z;
					res2[length_res2+23] = pMesh.VertexTopoListList11[vIndex4].northV;
					res2[length_res2+24] = pMesh.VertexTopoListList11[vIndex4].northU;
					
					res2[length_res2+25] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res2[length_res2+26] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res2[length_res2+27] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res2[length_res2+28] = pMesh.VertexTopoListList11[vIndex1].northV;
					res2[length_res2+29] = pMesh.VertexTopoListList11[vIndex1].northU;
					
					length_res2+=30;
					
				}
				if(mapId == 4){
					res3[length_res3] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res3[length_res3+1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res3[length_res3+2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res3[length_res3+3] = pMesh.VertexTopoListList11[vIndex1].southV;
					res3[length_res3+4] = pMesh.VertexTopoListList11[vIndex1].southU;
					
					res3[length_res3+5] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.x;
					res3[length_res3+6] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.y;
					res3[length_res3+7] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.z;
					res3[length_res3+8] = pMesh.VertexTopoListList11[vIndex2].southV;
					res3[length_res3+9] = pMesh.VertexTopoListList11[vIndex2].southU;
					
					res3[length_res3+10] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res3[length_res3+11] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res3[length_res3+12] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res3[length_res3+13] = pMesh.VertexTopoListList11[vIndex3].southV;
					res3[length_res3+14] = pMesh.VertexTopoListList11[vIndex3].southU;
					
					res3[length_res3+15] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res3[length_res3+16] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res3[length_res3+17] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res3[length_res3+18] = pMesh.VertexTopoListList11[vIndex3].southV;
					res3[length_res3+19] = pMesh.VertexTopoListList11[vIndex3].southU;
					
					res3[length_res3+20] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.x;
					res3[length_res3+21] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.y;
					res3[length_res3+22] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.z;
					res3[length_res3+23] = pMesh.VertexTopoListList11[vIndex4].southV;
					res3[length_res3+24] = pMesh.VertexTopoListList11[vIndex4].southU;
					
					res3[length_res3+25] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res3[length_res3+26] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res3[length_res3+27] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res3[length_res3+28] = pMesh.VertexTopoListList11[vIndex1].southV;
					res3[length_res3+29] = pMesh.VertexTopoListList11[vIndex1].southU;
					
					length_res3+=30;
					
				}
				if(mapId!=1&&mapId!=2&&mapId!=3&&mapId!=4)
				{
					notinanytexture++;
					/*
					Log.v("fyf", String.valueOf(pMesh.VertexTopoListList11[vIndex1].eastrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex1].eastrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex2].eastrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex2].eastrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex3].eastrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex3].eastrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex4].eastrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex4].eastrestV)+"\n"+"eastrestException!"
							+"\n"+String.valueOf(pMesh.VertexTopoListList11[vIndex1].westrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex1].westrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex2].westrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex2].westrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex3].westrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex3].westrestV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex4].westrestU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex4].westrestV)+"\n"+"westrestException!"+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex1].northU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex1].northV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex2].northU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex2].northV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex3].northU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex3].northV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex4].northU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex4].northV)+"\n"+"northException"+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex1].southU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex1].southV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex2].southU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex2].southV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex3].southU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex3].southV)+"\n"
							+String.valueOf(pMesh.VertexTopoListList11[vIndex4].southU)+" "+String.valueOf(pMesh.VertexTopoListList11[vIndex4].southV)+"\n"+"southException");
					*/
					/*
					res4[length_res4] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res4[length_res4+1] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res4[length_res4+2] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res4[length_res4+3] = 1.0f;
					res4[length_res4+4] = 0.0f;
					res4[length_res4+5] = 0.0f;
					res4[length_res4+6] = 1.0f;
					res4[length_res4+7] = 0.0f;
					res4[length_res4+8] = 0.0f;
					
					res4[length_res4+9] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.x;
					res4[length_res4+10] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.y;
					res4[length_res4+11] = pMesh.VertexTopoListList11[vIndex2].m_vPosition.z;
					res4[length_res4+12] = 0.0f;
					res4[length_res4+13] = 1.0f;
					res4[length_res4+14] = 0.0f;
					res4[length_res4+15] = 1.0f;
					res4[length_res4+16] = 1.0f;
					res4[length_res4+17] = 1.0f;
					
					res4[length_res4+18] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res4[length_res4+19] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res4[length_res4+20] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res4[length_res4+21] = 0.0f;
					res4[length_res4+22] = 0.0f;
					res4[length_res4+23] = 1.0f;
					res4[length_res4+24] = 1.0f;
					res4[length_res4+25] = 0.0f;
					res4[length_res4+26] = 1.0f;
					
					res4[length_res4+27] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.x;
					res4[length_res4+28] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.y;
					res4[length_res4+29] = pMesh.VertexTopoListList11[vIndex3].m_vPosition.z;
					res4[length_res4+30] = 1.0f;
					res4[length_res4+31] = 0.0f;
					res4[length_res4+32] = 0.0f;
					res4[length_res4+33] = 1.0f;
					res4[length_res4+34] = 0.0f;
					res4[length_res4+35] = 1.0f;
					
					res4[length_res4+36] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.x;
					res4[length_res4+37] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.y;
					res4[length_res4+38] = pMesh.VertexTopoListList11[vIndex4].m_vPosition.z;
					res4[length_res4+39] = 0.0f;
					res4[length_res4+40] = 1.0f;
					res4[length_res4+41] = 0.0f;
					res4[length_res4+42] = 1.0f;
					res4[length_res4+43] = 1.0f;
					res4[length_res4+44] = 0.0f;
					
					res4[length_res4+45] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.x;
					res4[length_res4+46] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.y;
					res4[length_res4+47] = pMesh.VertexTopoListList11[vIndex1].m_vPosition.z;
					res4[length_res4+48] = 0.0f;
					res4[length_res4+49] = 0.0f;
					res4[length_res4+50] = 1.0f;
					res4[length_res4+51] = 1.0f;
					res4[length_res4+52] = 0.0f;
					res4[length_res4+53] = 0.0f;
					
					length_res4+=54;
					*/
				}
			}
		}
		
		
		//Log.v("fyf", "I am not in any of the textures!!!!"+String.valueOf(notinanytexture));
		
		return;
	}
	public void DrawScene(){
		float fps = 0;
		//WhichFaceInview(m_vEyePoint, m_vGazePoint, m_vUp);
		DrawMesh(m_pCurrentMesh);
		//return fps;
	}
	public void Move(int x,int y){
		
	}
	//判断根据手势进行的月球旋转
	public void Rotate(float a,float b){
		
		//Vector3D w=m_vUp^m_vFront;
		Vector3D w = new Vector3D(m_vUp.y*m_vFront.z-m_vUp.z*m_vFront.y,m_vUp.z*m_vFront.x-m_vUp.x*m_vFront.z,m_vUp.x*m_vFront.y-m_vUp.y*m_vFront.x);
		w.normalize();
		//m_vFront=m_vFront*(float)cos(a)+w*(float)sin(a);
		m_vFront = new Vector3D(m_vFront.x*(float)Math.cos(a)+w.x*(float)Math.sin(a),m_vFront.y*(float)Math.cos(a)+w.y*(float)Math.sin(a),m_vFront.z*(float)Math.cos(a)+w.z*(float)Math.sin(a));

		w=new Vector3D(m_vUp.y*m_vFront.z-m_vUp.z*m_vFront.y,m_vUp.z*m_vFront.x-m_vUp.x*m_vFront.z,m_vUp.x*m_vFront.y-m_vUp.y*m_vFront.x);
		w.normalize();
		//m_vFront=m_vFront*(float)cos(b)+m_vUp*(float)sin(b);
		m_vFront = new Vector3D(m_vFront.x*(float)Math.cos(b)+m_vUp.x*(float)Math.sin(b),m_vFront.y*(float)Math.cos(b)+m_vUp.y*(float)Math.sin(b),m_vFront.z*(float)Math.cos(b)+m_vUp.z*(float)Math.sin(b));
		//m_vUp=m_vFront^w;
		m_vUp = new Vector3D(m_vFront.y*w.z-m_vFront.z*w.y,m_vFront.z*w.x-m_vFront.x*w.z,m_vFront.x*w.y-m_vFront.y*w.x);
		m_vEyePoint=new Vector3D(m_vFront.x*(float)m_dDistance,m_vFront.y*(float)m_dDistance,m_vFront.z*(float)m_dDistance);
		
		//Log.v("fy", String.valueOf(m_vEyePoint.x)+" "+String.valueOf(m_vEyePoint.y)+" "+String.valueOf(m_vEyePoint.z));
		
	}
	//引擎控制的旋转
	public void engineRotate(float a, float b){
		
		SimpleVector w = myCamera.getUpVector().calcCross(myCamera.getDirection());
		w.normalize();
		w = myCamera.getDirection().calcCross(myCamera.getUpVector());
		w.normalize();
		myCamera.setPosition(myCamera.getDirection()); 
	}
	
	
	
	
	public void Scale(int d){
		
	}
	//视野判断
	public void WhichFaceInview(Vector3D m_vEyePoint1,Vector3D m_vGazePoint1,Vector3D m_vUp1){
		Vector3D viewDir = new Vector3D(m_vGazePoint1.x-m_vEyePoint1.x,m_vGazePoint1.y-m_vEyePoint1.y,m_vGazePoint1.z-m_vEyePoint1.z);
		viewDir.normalize();
		//m_vEyePoint = new Vector3D(m_vEyePoint1);
		//m_vGazePoint = new Vector3D(m_vGazePoint1);
		//m_vUp = new Vector3D(m_vUp1);
		CaculatemFrustum();
		for(int k = 0;k < 48;k++){
			int inview;
			int inmark;
			inmark = getMark(m_pCurrentMesh.m_Face48[k], viewDir, m_vEyePoint,upN,downN,leftN,rightN,upD,downD,leftD,rightD);
			inview = getInview(m_pCurrentMesh.m_Face48[k], viewDir, m_vEyePoint,upN,downN,leftN,rightN,upD,downD,leftD,rightD);
			if(inmark==0&&inview==0){
				m_pCurrentMesh.m_Face48[k].m_bInview = true;
			}else{
				m_pCurrentMesh.m_Face48[k].m_bInview = false;
			}
		}
		
	}
	public int getMark(JFace48 m_Face48,Vector3D viewDir,Vector3D viewPoint,Vector3D upN,Vector3D downN,Vector3D leftN,Vector3D rightN,float upD,float downD,float leftD,float rightD){
		
		float theta = (float)Math.acos(viewDir.dotProduct(m_Face48.faceNormal));
		float alpha = m_Face48.coneAngle;
		float beta = (float)Math.atan(1/viewPoint.length());
		if(theta+alpha<3.1415926535898/2+beta){
			return 1;
		}
		else {
			return 0;
		}
	}
	public int getInview(JFace48 m_Face48,Vector3D viewDir,Vector3D viewPoint,Vector3D upN,Vector3D downN,Vector3D leftN,Vector3D rightN,float upD,float downD,float leftD,float rightD){
		
		float tmpUp = m_Face48.sphereCentre.dotProduct(upN)+upD;
		float tmpDown = m_Face48.sphereCentre.dotProduct(downN)+downD;
		float tmpLeft = m_Face48.sphereCentre.dotProduct(leftN)+leftD;
		float tmpRight = m_Face48.sphereCentre.dotProduct(rightN)+rightD;
		if((tmpUp<-m_Face48.radius)||(tmpDown<-m_Face48.radius)||(tmpLeft<-m_Face48.radius)||(tmpRight<-m_Face48.radius)){
			return 1;
		}
		else{
			return 0;
		}
	}
	public void CaculatemFrustum(){
		float nearDistance = 0.1f;
		Vector3D viewDir = new Vector3D(m_vGazePoint.x-m_vEyePoint.x,m_vGazePoint.y-m_vEyePoint.y,m_vGazePoint.z-m_vEyePoint.z);
		viewDir.normalize();
		Vector3D midNear = new Vector3D(m_vEyePoint.x+viewDir.x*nearDistance,m_vEyePoint.y+viewDir.y*nearDistance,m_vEyePoint.z+viewDir.z*nearDistance);
		Vector3D upDir = m_vUp;
		upDir.normalize();
		Vector3D rightDir = new Vector3D(viewDir.y*upDir.z-viewDir.z*upDir.y,viewDir.z*upDir.x-viewDir.x*upDir.z,viewDir.x*upDir.y-viewDir.y*upDir.x);
		rightDir.normalize();
		float hHalf = nearDistance*(float)Math.tan(3.1415926535898/9);
		float wHalf = hHalf*1.0f;
		Vector3D lu = new Vector3D(midNear.x+upDir.x*hHalf-rightDir.x*wHalf,midNear.y+upDir.y*hHalf-rightDir.y*wHalf,midNear.z+upDir.z*hHalf-rightDir.z*wHalf);
		Vector3D ld = new Vector3D(midNear.x-upDir.x*hHalf-rightDir.x*wHalf,midNear.y-upDir.y*hHalf-rightDir.y*wHalf,midNear.z-upDir.z*hHalf-rightDir.z*wHalf);
		Vector3D ru = new Vector3D(midNear.x+upDir.x*hHalf+rightDir.x*wHalf,midNear.y+upDir.y*hHalf+rightDir.y*wHalf,midNear.z+upDir.z*hHalf+rightDir.z*wHalf);
		Vector3D rd = new Vector3D(midNear.x-upDir.x*hHalf+rightDir.x*wHalf,midNear.y-upDir.y*hHalf+rightDir.y*wHalf,midNear.z-upDir.z*hHalf+rightDir.z*wHalf);
		Vector3D tmpA = new Vector3D(lu.x-m_vEyePoint.x,lu.y-m_vEyePoint.y,lu.z-m_vEyePoint.z);
		Vector3D tmpB = new Vector3D(ru.x-m_vEyePoint.x,ru.y-m_vEyePoint.y,ru.z-m_vEyePoint.z);
		tmpA.normalize();
		tmpB.normalize();
		Vector3D tmpC = new Vector3D(tmpA.y*tmpB.z-tmpA.z*tmpB.y,tmpA.z*tmpB.x-tmpA.x*tmpB.z,tmpA.x*tmpB.y-tmpA.y*tmpB.x);
		tmpC.normalize();
		upN = new Vector3D(tmpC);
		upD = -1.0f*(m_vEyePoint.x*upN.x+m_vEyePoint.y*upN.y+m_vEyePoint.z*upN.z);
		
		tmpA = new Vector3D(ru.x-m_vEyePoint.x,ru.y-m_vEyePoint.y,ru.z-m_vEyePoint.z);
		tmpB = new Vector3D(rd.x-m_vEyePoint.x,rd.y-m_vEyePoint.y,rd.z-m_vEyePoint.z);
		tmpA.normalize();
		tmpB.normalize();
		tmpC = new Vector3D(tmpA.y*tmpB.z-tmpA.z*tmpB.y,tmpA.z*tmpB.x-tmpA.x*tmpB.z,tmpA.x*tmpB.y-tmpA.y*tmpB.x);
		rightN = new Vector3D(tmpC);
		rightD = -1.0f*(m_vEyePoint.x*rightN.x+m_vEyePoint.y*rightN.y+m_vEyePoint.z*rightN.z);
		
		tmpA = new Vector3D(rd.x-m_vEyePoint.x,rd.y-m_vEyePoint.y,rd.z-m_vEyePoint.z);
		tmpB = new Vector3D(ld.x-m_vEyePoint.x,ld.y-m_vEyePoint.y,ld.z-m_vEyePoint.z);
		tmpA.normalize();
		tmpB.normalize();
		tmpC = new Vector3D(tmpA.y*tmpB.z-tmpA.z*tmpB.y,tmpA.z*tmpB.x-tmpA.x*tmpB.z,tmpA.x*tmpB.y-tmpA.y*tmpB.x);
		downN = new Vector3D(tmpC);
		downD = -1.0f*(m_vEyePoint.x*downN.x+m_vEyePoint.y*downN.y+m_vEyePoint.z*downN.z);
		
		tmpA = new Vector3D(ld.x-m_vEyePoint.x,ld.y-m_vEyePoint.y,ld.z-m_vEyePoint.z);
		tmpB = new Vector3D(lu.x-m_vEyePoint.x,lu.y-m_vEyePoint.y,lu.z-m_vEyePoint.z);
		tmpA.normalize();
		tmpB.normalize();
		tmpC = new Vector3D(tmpA.y*tmpB.z-tmpA.z*tmpB.y,tmpA.z*tmpB.x-tmpA.x*tmpB.z,tmpA.x*tmpB.y-tmpA.y*tmpB.x);
		leftN = new Vector3D(tmpC);
		leftD = -1.0f*(m_vEyePoint.x*leftN.x+m_vEyePoint.y*leftN.y+m_vEyePoint.z*leftN.z);
		
		return;
		
		
		
	}
	//初始化纹理
	public void InitTexture(){
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		
		Log.v("fyf",String.valueOf(GLES20.glGetError())+" Let's begin!");
		
		textures = new int[4];
		GLES20.glGenTextures(4, textures, 0);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		InputStream is = context.getResources().openRawResource(R.drawable.eastrest);
		Bitmap bitmapTmp;
		
		bitmapTmp = BitmapFactory.decodeStream(is);
		
		Log.v("fyf",String.valueOf(bitmapTmp.getHeight())+" "+String.valueOf(bitmapTmp.getWidth()));
		Log.v("fyf",String.valueOf(bitmapTmp.getHeight())+" "+String.valueOf(bitmapTmp.getWidth()));
		Log.v("fyf",String.valueOf(bitmapTmp.getHeight())+" "+String.valueOf(bitmapTmp.getWidth()));
		Log.v("fyf",String.valueOf(bitmapTmp.getHeight())+" "+String.valueOf(bitmapTmp.getWidth()));
		Log.v("fyf",String.valueOf(bitmapTmp.getHeight())+" "+String.valueOf(bitmapTmp.getWidth()));
		
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
		bitmapTmp.recycle();
		
		
		/*
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		is = context.getResources().openRawResource(R.drawable.westrest);
		bitmapTmp = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
		bitmapTmp.recycle();
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[2]);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		is = context.getResources().openRawResource(R.drawable.northpole);
		bitmapTmp = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
		bitmapTmp.recycle();
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[3]);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		is = context.getResources().openRawResource(R.drawable.southpole);
		bitmapTmp = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmapTmp,0);
		bitmapTmp.recycle();
		*/
		Log.v("fyf",String.valueOf(GLES20.glGetError())+" Did you find it?");
	}
	
	
}
