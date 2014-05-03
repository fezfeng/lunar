package com.example.forengine;

import android.R.integer;

public class JFace48 {

	int[] m_iVertex;//all vertexes
	float radius;
	Vector3D faceNormal;
	Vector3D Bmin;
	Vector3D Bmax;
	Vector3D sphereCentre;
	float coneAngle;
	boolean m_bInview;
	
	
	public JFace48() {
		// TODO Auto-generated constructor stub
		m_iVertex = new int[4];
		m_iVertex[0]=m_iVertex[1]=m_iVertex[2]=m_iVertex[3]=-1;

		radius =  0.0f;
		faceNormal =new Vector3D(0.0f,0.0f,0.0f);
		Bmin= new Vector3D(0.0f,0.0f,0.0f);
		Bmax= new Vector3D(0.0f,0.0f,0.0f);
		sphereCentre=new Vector3D(0.0f,0.0f,0.0f);
		coneAngle=0.0f;
		m_bInview = true;
	}
}
