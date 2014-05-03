package com.example.forengine;

import java.util.concurrent.CopyOnWriteArrayList;

import android.R.integer;

public class VertexTopoList {
	Vector3D m_vPosition;//vertex coordinate
	float La,Lg;
	float northU,northV,southU,southV,eastrestU,eastrestV,westrestU,westrestV;
	int[] m_iVertexTopoList; 
	byte m_nValence;
	
	public VertexTopoList() {
		// TODO Auto-generated constructor stub
		m_iVertexTopoList = new int[4];
		m_iVertexTopoList[0]=m_iVertexTopoList[1]=m_iVertexTopoList[2]=m_iVertexTopoList[3]=-1;

		northU = -1.0f;
		northV = -1.0f;
		southU = -1.0f;
		southV = -1.0f;
		eastrestU = -1.0f;
		eastrestV = -1.0f;
		westrestU = -1.0f;
		westrestV = -1.0f;

		m_nValence = 0;
	}
	public VertexTopoList(VertexTopoList v){
		m_iVertexTopoList = new int[4];
		if(v.m_nValence!=0){
			m_nValence = v.m_nValence;
			for(int i = 0; i < m_nValence; i++){
				m_iVertexTopoList[i] = v.m_iVertexTopoList[i];
			}
		}
		else{
			m_nValence = 0;
			for(int i = 0; i<4;i++){
				m_iVertexTopoList[i] = -1;
			}
		}
	}
	public void copy(VertexTopoList a, VertexTopoList b){
		if(b.m_nValence!=0){
			a.m_nValence = b.m_nValence;
			for(int i = 0;i < a.m_nValence;i++){
				a.m_iVertexTopoList[i] = b.m_iVertexTopoList[i];
			}
		}
		else{
			a.m_nValence = 0;
			for(int i = 0;i < 4;i++){
				a.m_iVertexTopoList[i]=-1;
			}
		}
		a.m_vPosition = b.m_vPosition;
		return;
	}
}
