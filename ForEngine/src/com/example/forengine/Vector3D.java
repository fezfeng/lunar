package com.example.forengine;

public class Vector3D {
	
	public float x,y,z;
	public Vector3D() {
		// TODO Auto-generated constructor stub
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	public Vector3D(float xx,float yy,float zz){
		x = xx;
		y = yy;
		z = zz;
	}
	public Vector3D(Vector3D v){
		x = v.x;
		y = v.y;
		z = v.z;
	}
	public float length(){
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	public float normalize(){
		float len = length();
		if((Math.abs(len))>=1e-16){
			x/=len;
			y/=len;
			z/=len;
		}
		return len;
	}
	public float dotProduct(Vector3D v){
		return x*v.x+y*v.y+z*v.z;
	}
}
