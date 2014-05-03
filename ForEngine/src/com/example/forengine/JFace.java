package com.example.forengine;

public class JFace {
	
	byte m_nType;
	int[] m_iVertex;
	int[] m_iEdge;
	int m_fPositionIndex;
	public JFace() {
		// TODO Auto-generated constructor stub
		m_nType = 4;
		m_iVertex = new int[4];
		m_iEdge = new int[4];
		m_iVertex[0]=m_iVertex[1]=m_iVertex[2]=m_iVertex[3]=-1;
		m_iEdge[0]=m_iEdge[1]=m_iEdge[2]=m_iEdge[3]=-1;
		m_fPositionIndex=-1;
	}
	public void copy(JFace a, JFace b){
		a.m_nType = b.m_nType;//¼¸±ßÐÎ
		for(short i=0;i<m_nType;i++)
		{
			a.m_iVertex[i] = b.m_iVertex[i];
			a.m_iEdge[i] = b.m_iEdge[i];
		}
		a.m_fPositionIndex = b.m_fPositionIndex;
	}
}
