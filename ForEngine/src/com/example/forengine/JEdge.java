
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
		a.m_iVertex[0] = b.m_iVertex[0];//边的两端点，Vertex0－>Vertex1
		a.m_iVertex[1] = b.m_iVertex[1];
		a.m_iTwinEdge = b.m_iTwinEdge;//与该边方向相反的另一条边，如果为-1则该边为边界
		//m_iNextEdge = e.m_iNextEdge;//沿逆时针方向的下一条边
		a.m_iFace = b.m_iFace;//该边所属的面，应该在它的左边

		a.m_ePositionIndex = b.m_ePositionIndex;
		a.m_bFirstIterator = b.m_bFirstIterator;
		return;
	}
}
