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
		m_Face48[0].Bmax = new  Vector3D(0.997681f,0.133067f,0.611052f);
		m_Face48[0].Bmin = new  Vector3D(0.699488f,-0.473484f,0.0144055f);
		m_Face48[0].sphereCentre = new  Vector3D(0.848585f,-0.170209f,0.312729f);
		m_Face48[0].radius = 0.45078f;
		m_Face48[0].coneAngle = 0.417399f;
		m_Face48[0].faceNormal = new  Vector3D(0.920052f,-0.177551f,0.349256f);
		m_Face48[1].Bmax = new  Vector3D(0.997681f,0.0495841f,0.133937f);
		m_Face48[1].Bmin = new  Vector3D(0.821683f,-0.473484f,-0.516851f);
		m_Face48[1].sphereCentre = new Vector3D(0.909682f,-0.21195f,-0.191457f);
		m_Face48[1].radius = 0.426644f;
		m_Face48[1].coneAngle = 0.407421f;
		m_Face48[1].faceNormal = new Vector3D(0.949651f,-0.24915f,-0.189966f);
		m_Face48[2].Bmax = new Vector3D(0.997681f,0.543709f,0.0144055f);
		m_Face48[2].Bmin = new Vector3D(0.680975f,-0.051727f,-0.585922f);
		m_Face48[2].sphereCentre = new Vector3D(0.839328f,0.245991f,-0.285758f);
		m_Face48[2].radius = 0.451453f;
		m_Face48[2].coneAngle = 0.416025f;
		m_Face48[2].faceNormal = new Vector3D(0.908324f,0.265106f,-0.323523f);
		m_Face48[3].Bmax = new Vector3D(0.997681f,0.543709f,0.531559f);
		m_Face48[3].Bmin = new Vector3D(0.776568f,0.0495841f,-0.108924f);
		m_Face48[3].sphereCentre = new Vector3D(0.887125f,0.296647f,0.211317f);
		m_Face48[3].radius = 0.419306f;
		m_Face48[3].coneAngle = 0.399108f;
		m_Face48[3].faceNormal = new Vector3D(0.918966f,0.334013f,0.209608f);
		m_Face48[4].Bmax = new Vector3D(0.829881f,0.877845f,-0.0383688f);
		m_Face48[4].Bmin = new Vector3D(0.345831f,0.439537f,-0.585922f);
		m_Face48[4].sphereCentre = new Vector3D(0.587856f,0.658691f,-0.312146f);
		m_Face48[4].radius = 0.426097f;
		m_Face48[4].coneAngle = 0.378833f;
		m_Face48[4].faceNormal = new Vector3D(0.623365f,0.706651f,-0.334754f);
		m_Face48[5].Bmax = new Vector3D(0.474718f,0.995116f,0.0585053f);
		m_Face48[5].Bmin = new Vector3D(-0.031422f,0.781809f,-0.518777f);
		m_Face48[5].sphereCentre = new Vector3D(0.221648f,0.888462f,-0.230136f);
		m_Face48[5].radius = 0.398413f;
		m_Face48[5].coneAngle = 0.387817f;
		m_Face48[5].faceNormal = new Vector3D(0.203297f,0.950513f,-0.234938f);
		m_Face48[6].Bmax = new Vector3D(0.474718f,0.995116f,0.554363f);
		m_Face48[6].Bmin = new Vector3D(-0.0356319f,0.737395f,-0.0383688f);
		m_Face48[6].sphereCentre = new Vector3D(0.219543f,0.866255f,0.257997f);
		m_Face48[6].radius = 0.411767f;
		m_Face48[6].coneAngle = 0.412481f;
		m_Face48[6].faceNormal = new Vector3D(0.236005f,0.928514f,0.286642f);
		m_Face48[7].Bmax = new Vector3D(0.829881f,0.877845f,0.48729f);
		m_Face48[7].Bmin = new Vector3D(0.466554f,0.523367f,-0.108924f);
		m_Face48[7].sphereCentre = new Vector3D(0.648218f,0.700606f,0.189183f);
		m_Face48[7].radius = 0.391514f;
		m_Face48[7].coneAngle = 0.377442f;
		m_Face48[7].faceNormal = new Vector3D(0.677098f,0.712869f,0.182639f);
		m_Face48[8].Bmax = new Vector3D(0.476764f,0.829082f,0.877016f);
		m_Face48[8].Bmin = new Vector3D(-0.0364858f,0.348908f,0.48729f);
		m_Face48[8].sphereCentre = new Vector3D(0.220139f,0.588995f,0.682153f);
		m_Face48[8].radius = 0.401833f;
		m_Face48[8].coneAngle = 0.394792f;
		m_Face48[8].faceNormal = new Vector3D(0.233843f,0.641184f,0.730891f);
		m_Face48[9].Bmax = new Vector3D(0.476764f,0.473401f,0.997106f);
		m_Face48[9].Bmin = new Vector3D(-0.0364858f,-0.171554f,0.804326f);
		m_Face48[9].sphereCentre = new Vector3D(0.220139f,0.150923f,0.900716f);
		m_Face48[9].radius = 0.423248f;
		m_Face48[9].coneAngle = 0.40301f;
		m_Face48[9].faceNormal = new Vector3D(0.209932f,0.187833f,0.959504f);
		m_Face48[10].Bmax = new Vector3D(0.834225f,0.348908f,0.911903f);
		m_Face48[10].Bmin = new Vector3D(0.367423f,-0.365449f,0.531559f);
		m_Face48[10].sphereCentre = new Vector3D(0.600824f,-0.00827034f,0.721731f);
		m_Face48[10].radius = 0.467138f;
		m_Face48[10].coneAngle = 0.407812f;
		m_Face48[10].faceNormal = new Vector3D(0.639405f,-0.0147968f,0.768727f);
		m_Face48[11].Bmax = new Vector3D(0.834225f,0.737395f,0.804326f);
		m_Face48[11].Bmin = new Vector3D(0.466554f,0.133067f,0.347218f);
		m_Face48[11].sphereCentre = new Vector3D(0.650389f,0.435231f,0.575772f);
		m_Face48[11].radius = 0.421112f;
		m_Face48[11].coneAngle = 0.37421f;
		m_Face48[11].faceNormal = new Vector3D(0.676097f,0.461319f,0.574524f);
		m_Face48[12].Bmax = new Vector3D(-0.031422f,0.995116f,0.554363f);
		m_Face48[12].Bmin = new Vector3D(-0.516057f,0.712409f,-0.032117f);
		m_Face48[12].sphereCentre = new Vector3D(-0.273739f,0.853763f,0.261123f);
		m_Face48[12].radius = 0.405818f;
		m_Face48[12].coneAngle = 0.392159f;
		m_Face48[12].faceNormal = new Vector3D(-0.294238f,0.912157f,0.285295f);
		m_Face48[13].Bmax = new Vector3D(-0.0245075f,0.995116f,0.0585053f);
		m_Face48[13].Bmin = new Vector3D(-0.516057f,0.766747f,-0.522597f);
		m_Face48[13].sphereCentre = new Vector3D(-0.270282f,0.880931f,-0.232046f);
		m_Face48[13].radius = 0.39732f;
		m_Face48[13].coneAngle = 0.378512f;
		m_Face48[13].faceNormal = new Vector3D(-0.249612f,0.939651f,-0.233986f);
		m_Face48[14].Bmax = new Vector3D(-0.368049f,0.856656f,-0.032117f);
		m_Face48[14].Bmin = new Vector3D(-0.846611f,0.428192f,-0.585203f);
		m_Face48[14].sphereCentre = new Vector3D(-0.60733f,0.642424f,-0.30866f);
		m_Face48[14].radius = 0.423824f;
		m_Face48[14].coneAngle = 0.3716f;
		m_Face48[14].faceNormal = new Vector3D(-0.644863f,0.686598f,-0.335762f);
		m_Face48[15].Bmax = new Vector3D(-0.511469f,0.856656f,0.480557f);
		m_Face48[15].Bmin = new Vector3D(-0.846611f,0.502111f,-0.118305f);
		m_Face48[15].sphereCentre = new Vector3D(-0.67904f,0.679384f,0.181126f);
		m_Face48[15].radius = 0.386219f;
		m_Face48[15].coneAngle = 0.365282f;
		m_Face48[15].faceNormal = new Vector3D(-0.70669f,0.685462f,0.175305f);
		m_Face48[16].Bmax = new Vector3D(-0.685819f,0.521337f,-0.000307476f);
		m_Face48[16].Bmin = new Vector3D(-1.00025f,-0.0683149f,-0.585203f);
		m_Face48[16].sphereCentre = new Vector3D(-0.843033f,0.226511f,-0.292755f);
		m_Face48[16].radius = 0.444032f;
		m_Face48[16].coneAngle = 0.407875f;
		m_Face48[16].faceNormal = new Vector3D(-0.911352f,0.244621f,-0.331057f);
		m_Face48[17].Bmax = new Vector3D(-0.811391f,0.0257009f,0.123084f);
		m_Face48[17].Bmin = new Vector3D(-1.00025f,-0.495943f,-0.523556f);
		m_Face48[17].sphereCentre = new Vector3D(-0.905819f,-0.235121f,-0.200236f);
		m_Face48[17].radius = 0.426005f;
		m_Face48[17].coneAngle = 0.402597f;
		m_Face48[17].faceNormal = new Vector3D(-0.941583f,-0.270717f,-0.200332f);
		m_Face48[18].Bmax = new Vector3D(-0.708992f,0.120306f,0.597797f);
		m_Face48[18].Bmin = new Vector3D(-1.00025f,-0.495943f,-0.000307476f);
		m_Face48[18].sphereCentre = new Vector3D(-0.85462f,-0.187818f,0.298745f);
		m_Face48[18].radius = 0.45341f;
		m_Face48[18].coneAngle = 0.408524f;
		m_Face48[18].faceNormal = new Vector3D(-0.922617f,-0.195911f,0.332259f);
		m_Face48[19].Bmax = new Vector3D(-0.798665f,0.521337f,0.512181f);
		m_Face48[19].Bmin = new Vector3D(-1.00025f,0.0257009f,-0.118305f);
		m_Face48[19].sphereCentre = new Vector3D(-0.899456f,0.273519f,0.196938f);
		m_Face48[19].radius = 0.413462f;
		m_Face48[19].coneAngle = 0.384157f;
		m_Face48[19].faceNormal = new Vector3D(-0.930521f,0.3111f,0.193254f);
		m_Face48[20].Bmax = new Vector3D(-0.399895f,0.339568f,0.900869f);
		m_Face48[20].Bmin = new Vector3D(-0.852399f,-0.376936f,0.512181f);
		m_Face48[20].sphereCentre = new Vector3D(-0.626147f,-0.0186843f,0.706525f);
		m_Face48[20].radius = 0.466159f;
		m_Face48[20].coneAngle = 0.397507f;
		m_Face48[20].faceNormal = new Vector3D(-0.66275f,-0.023681f,0.748466f);
		m_Face48[21].Bmax = new Vector3D(-0.0221595f,0.473401f,0.997106f);
		m_Face48[21].Bmin = new Vector3D(-0.51545f,-0.171435f,0.786216f);
		m_Face48[21].sphereCentre = new Vector3D(-0.268805f,0.150983f,0.891661f);
		m_Face48[21].radius = 0.419411f;
		m_Face48[21].coneAngle = 0.387008f;
		m_Face48[21].faceNormal = new Vector3D(-0.259275f,0.184643f,0.947989f);
		m_Face48[22].Bmax = new Vector3D(-0.0356319f,0.829082f,0.877016f);
		m_Face48[22].Bmin = new Vector3D(-0.51545f,0.339568f,0.480557f);
		m_Face48[22].sphereCentre = new Vector3D(-0.275541f,0.584325f,0.678787f);
		m_Face48[22].radius = 0.395926f;
		m_Face48[22].coneAngle = 0.373967f;
		m_Face48[22].faceNormal = new Vector3D(-0.293401f,0.628552f,0.720305f);
		m_Face48[23].Bmax = new Vector3D(-0.511469f,0.712409f,0.786216f);
		m_Face48[23].Bmin = new Vector3D(-0.852399f,0.120306f,0.332892f);
		m_Face48[23].sphereCentre = new Vector3D(-0.681934f,0.416358f,0.559554f);
		m_Face48[23].radius = 0.409976f;
		m_Face48[23].coneAngle = 0.352457f;
		m_Face48[23].faceNormal = new Vector3D(-0.704852f,0.440705f,0.555844f);
		m_Face48[24].Bmax = new Vector3D(-0.384495f,-0.376936f,0.597797f);
		m_Face48[24].Bmin = new Vector3D(-0.861472f,-0.854528f,0.0291039f);
		m_Face48[24].sphereCentre = new Vector3D(-0.622984f,-0.615732f,0.31345f);
		m_Face48[24].radius = 0.441308f;
		m_Face48[24].coneAngle = 0.399538f;
		m_Face48[24].faceNormal = new Vector3D(-0.663426f,-0.662648f,0.347511f);
		m_Face48[25].Bmax = new Vector3D(-0.505078f,-0.473259f,0.123084f);
		m_Face48[25].Bmin = new Vector3D(-0.861472f,-0.854528f,-0.503588f);
		m_Face48[25].sphereCentre = new Vector3D(-0.683275f,-0.663893f,-0.190252f);
		m_Face48[25].radius = 0.407769f;
		m_Face48[25].coneAngle = 0.381768f;
		m_Face48[25].faceNormal = new Vector3D(-0.717071f,-0.671754f,-0.185892f);
		m_Face48[26].Bmax = new Vector3D(0.0286591f,-0.703561f,0.0291039f);
		m_Face48[26].Bmin = new Vector3D(-0.519841f,-0.997072f,-0.592688f);
		m_Face48[26].sphereCentre = new Vector3D(-0.245591f,-0.850316f,-0.281792f);
		m_Face48[26].radius = 0.43978f;
		m_Face48[26].coneAngle = 0.430216f;
		m_Face48[26].faceNormal = new Vector3D(-0.264868f,-0.913035f,-0.31018f);
		m_Face48[27].Bmax = new Vector3D(0.0216496f,-0.744489f,0.546345f);
		m_Face48[27].Bmin = new Vector3D(-0.519841f,-0.997072f,-0.0741675f);
		m_Face48[27].sphereCentre = new Vector3D(-0.249096f,-0.87078f,0.236089f);
		m_Face48[27].radius = 0.43071f;
		m_Face48[27].coneAngle = 0.414617f;
		m_Face48[27].faceNormal = new Vector3D(-0.23427f,-0.941164f,0.243574f);
		m_Face48[28].Bmax = new Vector3D(0.544903f,-0.681015f,0.0306619f);
		m_Face48[28].Bmin = new Vector3D(0.0216496f,-0.997072f,-0.592688f);
		m_Face48[28].sphereCentre = new Vector3D(0.283276f,-0.839043f,-0.281013f);
		m_Face48[28].radius = 0.436535f;
		m_Face48[28].coneAngle = 0.428166f;
		m_Face48[28].faceNormal = new Vector3D(0.305455f,-0.900502f,-0.309505f);
		m_Face48[29].Bmax = new Vector3D(0.869394f,-0.453585f,0.133937f);
		m_Face48[29].Bmin = new Vector3D(0.530435f,-0.835951f,-0.504379f);
		m_Face48[29].sphereCentre = new Vector3D(0.699914f,-0.644768f,-0.185221f);
		m_Face48[29].radius = 0.408822f;
		m_Face48[29].coneAngle = 0.390234f;
		m_Face48[29].faceNormal = new Vector3D(0.737032f,-0.651143f,-0.181101f);
		m_Face48[30].Bmax = new Vector3D(0.869394f,-0.365449f,0.611052f);
		m_Face48[30].Bmin = new Vector3D(0.383819f,-0.835951f,0.0306619f);
		m_Face48[30].sphereCentre = new Vector3D(0.626607f,-0.6007f,0.320857f);
		m_Face48[30].radius = 0.445535f;
		m_Face48[30].coneAngle = 0.403905f;
		m_Face48[30].faceNormal = new Vector3D(0.671815f,-0.648974f,0.357067f);
		m_Face48[31].Bmax = new Vector3D(0.544903f,-0.737803f,0.551816f);
		m_Face48[31].Bmin = new Vector3D(0.00942003f,-0.997072f,-0.0741675f);
		m_Face48[31].sphereCentre = new Vector3D(0.277162f,-0.867438f,0.238824f);
		m_Face48[31].radius = 0.431804f;
		m_Face48[31].coneAngle = 0.404773f;
		m_Face48[31].faceNormal = new Vector3D(0.257531f,-0.934561f,0.245505f);
		m_Face48[32].Bmax = new Vector3D(0.699488f,-0.171554f,0.911903f);
		m_Face48[32].Bmin = new Vector3D(-0.00931809f,-0.737803f,0.551816f);
		m_Face48[32].sphereCentre = new Vector3D(0.345085f,-0.454679f,0.73186f);
		m_Face48[32].radius = 0.488033f;
		m_Face48[32].coneAngle = 0.406563f;
		m_Face48[32].faceNormal = new Vector3D(0.386933f,-0.487231f,0.782872f);
		m_Face48[33].Bmax = new Vector3D(0.367423f,0.052096f,0.997106f);
		m_Face48[33].Bmin = new Vector3D(-0.399895f,-0.540238f,0.841606f);
		m_Face48[33].sphereCentre = new Vector3D(-0.016236f,-0.244071f,0.919356f);
		m_Face48[33].radius = 0.490871f;
		m_Face48[33].coneAngle = 0.400601f;
		m_Face48[33].faceNormal = new Vector3D(-0.0170741f,-0.221906f,0.974919f);
		m_Face48[34].Bmax = new Vector3D(-0.00931809f,-0.171435f,0.900869f);
		m_Face48[34].Bmin = new Vector3D(-0.708992f,-0.744489f,0.546345f);
		m_Face48[34].sphereCentre = new Vector3D(-0.359155f,-0.457962f,0.723607f);
		m_Face48[34].radius = 0.485701f;
		m_Face48[34].coneAngle = 0.404473f;
		m_Face48[34].faceNormal = new Vector3D(-0.40232f,-0.490777f,0.772837f);
		m_Face48[35].Bmax = new Vector3D(0.383819f,-0.540238f,0.841606f);
		m_Face48[35].Bmin = new Vector3D(-0.384495f,-0.912197f,0.406664f);
		m_Face48[35].sphereCentre = new Vector3D(-0.000338003f,-0.726217f,0.624135f);
		m_Face48[35].radius = 0.479019f;
		m_Face48[35].coneAngle = 0.400684f;
		m_Face48[35].faceNormal = new Vector3D(-0.000152777f,-0.781044f,0.624476f);
		m_Face48[36].Bmax = new Vector3D(0.852986f,0.439537f,-0.516851f);
		m_Face48[36].Bmin = new Vector3D(0.375828f,-0.274227f,-0.892906f);
		m_Face48[36].sphereCentre = new Vector3D(0.614407f,0.082655f,-0.704879f);
		m_Face48[36].radius = 0.468656f;
		m_Face48[36].coneAngle = 0.403294f;
		m_Face48[36].faceNormal = new Vector3D(0.65209f,0.0970265f,-0.751907f);
		m_Face48[37].Bmax = new Vector3D(0.852986f,-0.051727f,-0.339975f);
		m_Face48[37].Bmin = new Vector3D(0.520805f,-0.681015f,-0.806973f);
		m_Face48[37].sphereCentre = new Vector3D(0.686896f,-0.366371f,-0.573474f);
		m_Face48[37].radius = 0.425568f;
		m_Face48[37].coneAngle = 0.369962f;
		m_Face48[37].faceNormal = new Vector3D(0.721736f,-0.386709f,-0.574066f);
		m_Face48[38].Bmax = new Vector3D(0.530435f,-0.274227f,-0.504379f);
		m_Face48[38].Bmin = new Vector3D(0.0226878f,-0.804447f,-0.908848f);
		m_Face48[38].sphereCentre = new Vector3D(0.276561f,-0.539337f,-0.706613f);
		m_Face48[38].radius = 0.419087f;
		m_Face48[38].coneAngle = 0.391571f;
		m_Face48[38].faceNormal = new Vector3D(0.296239f,-0.584016f,-0.755757f);
		m_Face48[39].Bmax = new Vector3D(0.520805f,0.248073f,-0.806973f);
		m_Face48[39].Bmin = new Vector3D(0.0120691f,-0.413987f,-0.997267f);
		m_Face48[39].sphereCentre = new Vector3D(0.266437f,-0.0829574f,-0.90212f);
		m_Face48[39].radius = 0.428179f;
		m_Face48[39].coneAngle = 0.390291f;
		m_Face48[39].faceNormal = new Vector3D(0.248516f,-0.111743f,-0.962161f);
		m_Face48[40].Bmax = new Vector3D(0.0286591f,-0.28256f,-0.503588f);
		m_Face48[40].Bmin = new Vector3D(-0.505078f,-0.804447f,-0.908848f);
		m_Face48[40].sphereCentre = new Vector3D(-0.238209f,-0.543503f,-0.706218f);
		m_Face48[40].radius = 0.424699f;
		m_Face48[40].coneAngle = 0.407415f;
		m_Face48[40].faceNormal = new Vector3D(-0.254705f,-0.594898f,-0.762379f);
		m_Face48[41].Bmax = new Vector3D(-0.490149f,-0.0683149f,-0.347969f);
		m_Face48[41].Bmin = new Vector3D(-0.846094f,-0.703561f,-0.820078f);
		m_Face48[41].sphereCentre = new Vector3D(-0.668121f,-0.385938f,-0.584023f);
		m_Face48[41].radius = 0.433913f;
		m_Face48[41].coneAngle = 0.380058f;
		m_Face48[41].faceNormal = new Vector3D(-0.704215f,-0.405557f,-0.582757f);
		m_Face48[42].Bmax = new Vector3D(-0.369756f,0.428192f,-0.523556f);
		m_Face48[42].Bmin = new Vector3D(-0.846094f,-0.28256f,-0.896243f);
		m_Face48[42].sphereCentre = new Vector3D(-0.607925f,0.0728161f,-0.7099f);
		m_Face48[42].radius = 0.466627f;
		m_Face48[42].coneAngle = 0.414553f;
		m_Face48[42].faceNormal = new Vector3D(-0.643837f,0.0846563f,-0.760465f);
		m_Face48[43].Bmax = new Vector3D(0.0226878f,0.237176f,-0.820078f);
		m_Face48[43].Bmin = new Vector3D(-0.490149f,-0.413987f,-0.997267f);
		m_Face48[43].sphereCentre = new Vector3D(-0.233731f,-0.0884058f,-0.908673f);
		m_Face48[43].radius = 0.423796f;
		m_Face48[43].coneAngle = 0.398523f;
		m_Face48[43].faceNormal = new Vector3D(-0.220571f,-0.117088f,-0.968318f);
		m_Face48[44].Bmax = new Vector3D(-0.00659073f,0.766747f,-0.522597f);
		m_Face48[44].Bmin = new Vector3D(-0.685819f,0.237176f,-0.896243f);
		m_Face48[44].sphereCentre = new Vector3D(-0.346205f,0.501962f,-0.70942f);
		m_Face48[44].radius = 0.469417f;
		m_Face48[44].coneAngle = 0.384809f;
		m_Face48[44].faceNormal = new Vector3D(-0.381704f,0.539974f,-0.750153f);
		m_Face48[45].Bmax = new Vector3D(0.345831f,0.920193f,-0.38498f);
		m_Face48[45].Bmin = new Vector3D(-0.368049f,0.591123f,-0.80672f);
		m_Face48[45].sphereCentre = new Vector3D(-0.0111086f,0.755658f,-0.59585f);
		m_Face48[45].radius = 0.446031f;
		m_Face48[45].coneAngle = 0.369855f;
		m_Face48[45].faceNormal = new Vector3D(-0.0140732f,0.807687f,-0.589444f);
		m_Face48[46].Bmax = new Vector3D(0.680975f,0.781809f,-0.518777f);
		m_Face48[46].Bmin = new Vector3D(-0.00659073f,0.248073f,-0.892906f);
		m_Face48[46].sphereCentre = new Vector3D(0.337192f,0.514941f,-0.705842f);
		m_Face48[46].radius = 0.473707f;
		m_Face48[46].coneAngle = 0.387778f;
		m_Face48[46].faceNormal = new Vector3D(0.372324f,0.549546f,-0.747913f);
		m_Face48[47].Bmax = new Vector3D(0.375828f,0.591123f,-0.80672f);
		m_Face48[47].Bmin = new Vector3D(-0.369756f,0.0213505f,-0.997267f);
		m_Face48[47].sphereCentre = new Vector3D(0.00303647f,0.306237f,-0.901993f);
		m_Face48[47].radius = 0.47876f;
		m_Face48[47].coneAngle = 0.388994f;
		m_Face48[47].faceNormal = new Vector3D(0.00307454f,0.292173f,-0.95636f);
		
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

