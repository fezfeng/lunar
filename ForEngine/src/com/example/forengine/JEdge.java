
package com.example.forengine;
import android.R.bool;
import android.R.integer;

public class JEdge {
	int[] m_iVertex;//two vertexes of an edge
	int m_iTwinEdge;
	int m_iFace;
	int m_ePositionIndex;
	boolean m_bFirstIterator;
	
	public JEdge() {
		// TODO Auto-generated constructor stub
		m_iVertex = new int[2];
		m_iVertex[0]=m_iVertex[1]=m_iTwinEdge=m_iFace=-1;
		m_ePositionIndex=-1;
		m_bFirstIterator=false;
	}
	public JEdge(int v0, int v1){
		m_iVertex = new int[2];
		m_iVertex[0]=v0; 
		m_iVertex[1]=v1; 
		m_iTwinEdge=m_iFace=-1;
		m_ePositionIndex=-1;
		m_bFirstIterator=false;
	}
	public void copy(JEdge a,JEdge b){
		a.m_iVertex[0] = b.m_iVertex[0];//�ߵ����˵㣬Vertex0��>Vertex1
		a.m_iVertex[1] = b.m_iVertex[1];
		a.m_iTwinEdge = b.m_iTwinEdge;//��ñ߷����෴����һ���ߣ����Ϊ-1��ñ�Ϊ�߽�
		//m_iNextEdge = e.m_iNextEdge;//����ʱ�뷽�����һ����
		a.m_iFace = b.m_iFace;//�ñ��������棬Ӧ�����������

		a.m_ePositionIndex = b.m_ePositionIndex;
		a.m_bFirstIterator = b.m_bFirstIterator;
		return;
	}
}
