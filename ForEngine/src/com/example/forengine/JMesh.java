package com.example.forengine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.transform.Templates;

import android.R.integer;
import android.content.Context;
import android.util.Log;



public class JMesh {
	int m_nVertex,m_nFace,m_nEdge; //number of vertex,faces and edges
	JVertex[] m_pVertex;
	JFace[] m_pFace;
	JEdge[] m_pEdge;
	JFace48[] m_Face48;
	
	VertexTopoList[] VertexTopoListList11;
	int[] m_FaceList11;
	int[] m_FaceList22;
	Context context;
	
	public JMesh(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		m_nVertex = m_nFace = m_nEdge = 0;
		m_pVertex = null;
		m_pFace = null;
		m_pEdge = null;
		m_Face48 = new JFace48[48];
	}
	
	public void LoadMesh(JScene m_scene) throws FileNotFoundException{
		//Vector3D[] vertexlist = new Vector3D[14];
		VertexTopoListList11 = new VertexTopoList[12290];
		int cnt = 0;
		InputStream id,northFile,southFile,eastrestFile,westrestFile,labelFile;
		//Log.v("fyf", "Before reading...");
		id = context.getResources().openRawResource(R.raw.position20);//new FileInputStream("./data/position20.txt");
		northFile = context.getResources().openRawResource(R.raw.northmap);
		southFile = context.getResources().openRawResource(R.raw.southmap);
		eastrestFile = context.getResources().openRawResource(R.raw.eastrest);
		westrestFile = context.getResources().openRawResource(R.raw.westrest);
		labelFile = context.getResources().openRawResource(R.raw.labelench);
		//Log.v("fyf", "After reading...");
		InputStreamReader it = new InputStreamReader(id);
		BufferedReader im = new BufferedReader(it);
		String tmp = new String("");
		try {
			while((tmp=im.readLine())!=null&&cnt<12290){
				
				String a[] = tmp.split(" ");
				VertexTopoListList11[cnt] = new VertexTopoList();
				VertexTopoListList11[cnt].m_vPosition = new Vector3D(Float.valueOf(a[0]),Float.valueOf(a[1]),Float.valueOf(a[2]));
				//vertexlist[cnt] = new Vector3D(Float.valueOf(a[0]),Float.valueOf(a[1]),Float.valueOf(a[2]));
				cnt++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader iu = new InputStreamReader(northFile);
		BufferedReader ju = new BufferedReader(iu);
		InputStreamReader iv = new InputStreamReader(southFile);
		BufferedReader jv = new BufferedReader(iv);
		InputStreamReader iw = new InputStreamReader(eastrestFile);
		BufferedReader jw = new BufferedReader(iw);
		InputStreamReader ix = new InputStreamReader(westrestFile);
		BufferedReader jx = new BufferedReader(ix);
		
		InputStreamReader ig = new InputStreamReader(labelFile);
		BufferedReader jg = new BufferedReader(ig);
		
		for(int i = 0;i < 12290;i++){
			try {
				tmp = ju.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String b[] = tmp.split(" ");
			VertexTopoListList11[i].northU = Float.valueOf(b[0]);
			VertexTopoListList11[i].northV = Float.valueOf(b[1]);
			try {
				tmp = jv.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String c[] = tmp.split(" ");
			VertexTopoListList11[i].southU = Float.valueOf(c[0]);
			VertexTopoListList11[i].southV = Float.valueOf(c[1]);
			
			try {
				tmp = jw.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String d[] = tmp.split(" ");
			VertexTopoListList11[i].eastrestU = Float.valueOf(d[0]);
			VertexTopoListList11[i].eastrestV = Float.valueOf(d[1]);
			
			try {
				tmp = jx.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String e[] = tmp.split(" ");
			VertexTopoListList11[i].westrestU = Float.valueOf(e[0]);
			VertexTopoListList11[i].westrestV = Float.valueOf(e[1]);
		}
		//for label points
		//m_scene.LPoints = new JLabelPoint[468];
		for(int j = 0;j<468;j++){
			
			m_scene.LPoints[j] = new JLabelPoint();
			try {
				tmp = jg.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String f[] = tmp.split("\t");
			m_scene.LPoints[j].index = Integer.valueOf(f[0]);
			m_scene.LPoints[j].color = Integer.valueOf(f[1]);
			m_scene.LPoints[j].x = Float.valueOf(f[2]);
			m_scene.LPoints[j].y = Float.valueOf(f[3]);
			m_scene.LPoints[j].z = Float.valueOf(f[4]);
			m_scene.LPoints[j].str = f[5];
			
		}
		
		
		try {
			im.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ju.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jx.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jg.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean Construct() {
		if((m_pVertex == null)||(m_pFace == null)) return false;
		if(m_pEdge != null){ m_pEdge = null;}
		byte nType;
		int[] vListVector;
		vListVector = new int[m_nVertex*4];
		for(int i = 0; i < m_nVertex*4;i++){
			vListVector[i] = -1;
		}
		for(int i = 0;i < m_nVertex;i++){
			m_pVertex[i].m_nValence = 0;
			m_pVertex[i].m_nEdgeValence = 0;
		}
		int iVertex;
		int iVertexBefore;
		int iVertexAfter;
		
		for(int i = 0;i < m_nFace;i++){
			nType = m_pFace[i].m_nType;
			for(int j = 0;j < nType;j++){
				iVertexBefore = m_pFace[i].m_iVertex[(j-1+nType)%nType];
				iVertex = m_pFace[i].m_iVertex[j];
				iVertexAfter = m_pFace[i].m_iVertex[(j+1)%nType];
				for(int k = 0;k < nType;k++){
					if(vListVector[4*iVertex+k]==-1){
						vListVector[4*iVertex+k]=iVertexBefore;
						m_pVertex[iVertex].m_nEdgeValence++;
						break;
					}
					else if(vListVector[4*iVertex+k]==iVertexBefore){
						break;
					}
				}
				for(int k = 0;k < nType;k++){
					if(vListVector[4*iVertex+k]==-1){
						vListVector[4*iVertex+k] = iVertexAfter;
						m_pVertex[iVertex].m_nEdgeValence++;
						break;
					}
					else if(vListVector[4*iVertex+k] == iVertexAfter){
						break;
					}
				}
			}
		}
		
		m_nEdge = 0;
		for(int i = 0;i < m_nVertex;i++){
			m_nEdge+=m_pVertex[i].m_nEdgeValence;
		}
		m_pEdge = new JEdge[m_nEdge];
		
		int iEdge = 0;
		for(int i = 0;i < m_nVertex;i++){
			int j = 0;
			for(int k = 0;k<4&&vListVector[4*i+k]!=-1;k++){
				m_pEdge[iEdge] = new JEdge();
				m_pEdge[iEdge].m_iVertex[0] = i;
				m_pEdge[iEdge].m_iVertex[1] = vListVector[4*i+k];
				m_pVertex[i].m_iEdge[j] = iEdge;
				j++;
				iEdge++;
			}
		}
		//scanning all the faces 
		for(int i = 0;i < m_nFace;i++){
			nType = m_pFace[i].m_nType;
			for(int j = 0; j < nType;j++){
				iVertex = m_pFace[i].m_iVertex[j];
				iVertexAfter = m_pFace[i].m_iVertex[(j+1)%nType];
				for(int k = 0;k < m_pVertex[iVertex].m_nEdgeValence;k++){
					if(iVertexAfter == m_pEdge[m_pVertex[iVertex].m_iEdge[k]].m_iVertex[1]){
						m_pFace[i].m_iEdge[j] = m_pVertex[iVertex].m_iEdge[k];
						m_pEdge[m_pFace[i].m_iEdge[j]].m_iFace = i;
						break;
					}
				}
				iVertex = m_pFace[i].m_iVertex[j];
				m_pVertex[iVertex].m_nValence++;
			}
		}
		//get every edge's twin edge
		int iSrcVertex,iDesVertex;
		for(int i = 0; i < m_nEdge;i++){
			if(m_pEdge[i].m_iTwinEdge!=-1){
				continue;
			}
			iSrcVertex = m_pEdge[i].m_iVertex[0];
			iDesVertex = m_pEdge[i].m_iVertex[1];
			for(int j = 0;j < m_pVertex[iDesVertex].m_nEdgeValence;j++){
				if(m_pEdge[m_pVertex[iDesVertex].m_iEdge[j]].m_iVertex[1]==iSrcVertex)
				{
					m_pEdge[m_pVertex[iDesVertex].m_iEdge[j]].m_iTwinEdge=i;
					m_pEdge[i].m_iTwinEdge = m_pVertex[iDesVertex].m_iEdge[j];
					break;
				}
			}
		}
		return true;
	}
	public void ConstructCodeTree(){
		int i,j,k;
		int[] FaceList;
		FaceList = new int[m_nFace*16];
		int index = m_nVertex;
		for(i = 0;i < m_nFace;i++){
			m_pFace[i].m_fPositionIndex = index++;
		}
		for(i = 0;i < m_nEdge;i++){
			if( m_pEdge[m_pEdge[i].m_iTwinEdge].m_ePositionIndex!=-1)
			{
				m_pEdge[i].m_ePositionIndex = m_pEdge[m_pEdge[i].m_iTwinEdge].m_ePositionIndex;
			}
			else if( (m_pEdge[i].m_iFace!=-1) && (m_pEdge[m_pEdge[i].m_iTwinEdge].m_iFace!=-1) )// 如果非边界边，当前边顶点的m_bFirstIterator改为true
			{
				m_pEdge[i].m_ePositionIndex = index++;
				m_pEdge[i].m_bFirstIterator = true;
			}
		}
		if(m_nFace == 48){
			for (k = 0; k<48;k++){
				m_Face48[k] = new JFace48();
				m_Face48[k].m_iVertex[0] = m_pFace[k].m_iVertex[0];
				m_Face48[k].m_iVertex[1] = m_pFace[k].m_iVertex[1];
				m_Face48[k].m_iVertex[2] = m_pFace[k].m_iVertex[2];
				m_Face48[k].m_iVertex[3] = m_pFace[k].m_iVertex[3];
			}
		}
		int faceNum = 0;
		for( i=0; i<m_nFace; i++ ){
			for( j=0; j<m_pFace[i].m_nType; j++ )
			{
				int i1 = m_pFace[i].m_fPositionIndex;
				int i2 = m_pEdge[m_pFace[i].m_iEdge[j]].m_ePositionIndex; //由于前面边界边未处理，此处可能为-1
				int i3 = m_pEdge[m_pFace[i].m_iEdge[j]].m_iVertex[1];
				int i4 = m_pEdge[m_pFace[i].m_iEdge[(j+1)%m_pFace[i].m_nType]].m_ePositionIndex;
				FaceList[faceNum] =   i1;
				FaceList[faceNum+1] = i2;
				FaceList[faceNum+2] = i3;
				FaceList[faceNum+3] = i4;
				faceNum += 4;
			}
		}
		for(i = 0;i < m_nVertex;i++){
			//VertexTopoList vinfo = new VertexTopoList();//to be modified....
			VertexTopoList vinfo = VertexTopoListList11[i];
			vinfo.m_nValence = m_pVertex[i].m_nEdgeValence;	  //////相关点数为该点的出边数,而不是m_nValence

			if( vinfo.m_iVertexTopoList[00] == -1 ) // 如果是第一次向里面加边 (只可能是第一次分解)
			{
				for( j=0; j<vinfo.m_nValence; j++ )
					vinfo.m_iVertexTopoList[j] = m_pEdge[m_pVertex[i].m_iEdge[j]].m_ePositionIndex;
			}
			else
			{
				int oldIndex;		
				for( j=0; j<vinfo.m_nValence; j++ )
				{
					oldIndex = vinfo.m_iVertexTopoList[j];//当前顶点在老的拓扑中的相关顶点索引
					for( k=0; k<m_pVertex[i].m_nEdgeValence; k++ )//与顶点相关的是边点
					{
						// 如果当前点的一条出边的终点和上次该点所存的index相同
						// 那么改存成现在边所对的m_ePositionIndex
						if( m_pEdge[m_pVertex[i].m_iEdge[k]].m_iVertex[1]==oldIndex )
						{
							vinfo.m_iVertexTopoList[j] = m_pEdge[m_pVertex[i].m_iEdge[k]].m_ePositionIndex;
							break;
						}
					}
				}
			}
		}
		
		for( i=0; i<m_nFace; i++ )  //比较耗费时间
		{
			VertexTopoListList11[m_nVertex + i].m_nValence = m_pFace[i].m_nType;
			for( j=0; j<VertexTopoListList11[m_nVertex + i].m_nValence; j++ )//与面点相关的是边点
				VertexTopoListList11[m_nVertex + i].m_iVertexTopoList[j] = m_pEdge[m_pFace[i].m_iEdge[j]].m_ePositionIndex;
		}
		int pp = 0;	
		for( i=0; i<m_nEdge; i++ )  //相当耗时 VertexTopoListList11耗内存
		{
			if( m_pEdge[i].m_bFirstIterator )//m_bFirstIterator的判定，防止了边点的重复添加
			{
				if( (m_pEdge[i].m_iFace!=-1) && (m_pEdge[m_pEdge[i].m_iTwinEdge].m_iFace!=-1) ) // 如果该edge非边界
				{
					VertexTopoListList11[m_nVertex + m_nFace + pp].m_nValence = 4;
					VertexTopoListList11[m_nVertex + m_nFace + pp].m_iVertexTopoList[0] = m_pEdge[i].m_iVertex[0];
					VertexTopoListList11[m_nVertex + m_nFace + pp].m_iVertexTopoList[1] = m_pEdge[i].m_iVertex[1];
					VertexTopoListList11[m_nVertex + m_nFace + pp].m_iVertexTopoList[2] = m_pFace[m_pEdge[i].m_iFace].m_fPositionIndex;
					VertexTopoListList11[m_nVertex + m_nFace + pp].m_iVertexTopoList[3] = m_pFace[m_pEdge[m_pEdge[i].m_iTwinEdge].m_iFace].m_fPositionIndex;
				}
				pp++;
			}
		}
		/*
		if(m_nVertex == 49154)
		{
			Construct48Face();
			m_FaceList22 = new int[786432];
			for (k=0;k<786432;k++)
			{
				m_FaceList22[k] = FaceList[k];
			}

			FaceList = null;
			m_pVertex = null;
			m_pFace = null;
			m_pEdge = null;
			m_nFace *= 4;
			m_nVertex += 2;
			return;
		}*/

		m_nFace = m_nFace*4;
		m_nVertex = m_nFace + 2;
		m_pVertex = new JVertex[m_nVertex];
		for(i = 0;i < m_nVertex;i++){
			m_pVertex[i] = new JVertex();
		}
		m_pFace = new JFace[m_nFace];
		faceNum = 0;
		int nType;
		for(i=0;i<m_nFace;i++)
		{
			m_pFace[i] = new JFace();
			nType=4;
			//m_pFace1[i].Create(nType);
			for(j=0;j<nType;j++)
			{
				m_pFace[i].m_iVertex[j]=FaceList[faceNum];
				faceNum++;
			}
		}
		/*
		if (m_nFace == 49152)
		{
			m_FaceList11 = new int[196608];
			for (k=0;k<196608;k++)
			{
				m_FaceList11[k] = FaceList[k];
			}
		}*/
		if(m_nFace == 12288){
			m_FaceList11 = new int[49152];
			for(k = 0;k<49152;k++){
				m_FaceList11[k] = FaceList[k];
			}
		}
		FaceList = null;
		Construct();
		return;
	}
	public void Construct48Face(){
		for(int i = 0;i < 48;i++){
			m_Face48[i] = new JFace48();
		}
	}
	public boolean InitializeMesh(){
		m_nVertex = 14;
		m_nFace = 12;
		m_pVertex = new JVertex[m_nVertex];
		m_pFace = new JFace[m_nFace];
		/*if (m_pFace == null)
		{
			return false;
		}*/
		for(int i = 0;i < m_nFace;i++){
			m_pFace[i] = new JFace(); 
			m_pFace[i].m_nType = 4;
		}
		for(int j = 0;j < m_nVertex;j++){
			m_pVertex[j] = new JVertex();
		}
		m_pFace[0].m_iVertex[0] = 4; 
		m_pFace[0].m_iVertex[1] = 9; 
		m_pFace[0].m_iVertex[2] = 3;
		m_pFace[0].m_iVertex[3] = 11;

		m_pFace[1].m_iVertex[0] = 4; 
		m_pFace[1].m_iVertex[1] = 11; 
		m_pFace[1].m_iVertex[2] = 1;
		m_pFace[1].m_iVertex[3] = 8;

		m_pFace[2].m_iVertex[0] = 4; 
		m_pFace[2].m_iVertex[1] = 8; 
		m_pFace[2].m_iVertex[2] = 0;
		m_pFace[2].m_iVertex[3] = 9;

		m_pFace[3].m_iVertex[0] = 5; 
		m_pFace[3].m_iVertex[1] = 8; 
		m_pFace[3].m_iVertex[2] = 1;
		m_pFace[3].m_iVertex[3] = 12;

		m_pFace[4].m_iVertex[0] = 5; 
		m_pFace[4].m_iVertex[1] = 12; 
		m_pFace[4].m_iVertex[2] = 2;
		m_pFace[4].m_iVertex[3] = 10;

		m_pFace[5].m_iVertex[0] = 5; 
		m_pFace[5].m_iVertex[1] = 10; 
		m_pFace[5].m_iVertex[2] = 0;
		m_pFace[5].m_iVertex[3] = 8;

		m_pFace[6].m_iVertex[0] = 6; 
		m_pFace[6].m_iVertex[1] = 10; 
		m_pFace[6].m_iVertex[2] = 2;
		m_pFace[6].m_iVertex[3] = 13;

		m_pFace[7].m_iVertex[0] = 6; 
		m_pFace[7].m_iVertex[1] = 13; 
		m_pFace[7].m_iVertex[2] = 3;
		m_pFace[7].m_iVertex[3] = 9;

		m_pFace[8].m_iVertex[0] = 6; 
		m_pFace[8].m_iVertex[1] = 9; 
		m_pFace[8].m_iVertex[2] = 0;
		m_pFace[8].m_iVertex[3] = 10;

		m_pFace[9].m_iVertex[0] = 7; 
		m_pFace[9].m_iVertex[1] = 11; 
		m_pFace[9].m_iVertex[2] = 3;
		m_pFace[9].m_iVertex[3] = 13;

		m_pFace[10].m_iVertex[0] = 7; 
		m_pFace[10].m_iVertex[1] = 13; 
		m_pFace[10].m_iVertex[2] = 2;
		m_pFace[10].m_iVertex[3] = 12;

		m_pFace[11].m_iVertex[0] = 7; 
		m_pFace[11].m_iVertex[1] = 12; 
		m_pFace[11].m_iVertex[2] = 1;
		m_pFace[11].m_iVertex[3] = 11;

		Construct();
		
		return true;
	}
	
	
}

