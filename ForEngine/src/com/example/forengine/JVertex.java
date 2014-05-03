package com.example.forengine;

public class JVertex {
	int[] m_iEdge;
	byte m_nValence;//value of vertex
	byte m_nEdgeValence;//value of edge
	public JVertex() {
		// TODO Auto-generated constructor stub
		m_iEdge = new int[4];
		m_iEdge[0]=m_iEdge[1]=m_iEdge[2]=m_iEdge[3]=-1;
		m_nValence=0; 
		m_nEdgeValence=0;
	}
	
	public void copy(JVertex a, JVertex b){
		a.m_nValence = b.m_nValence;
		for(short i = 0;i < a.m_nValence;i++){
			a.m_iEdge[i] = b.m_iEdge[i];
			
		}
		a.m_nEdgeValence = b.m_nEdgeValence;
		return;
	}
}
