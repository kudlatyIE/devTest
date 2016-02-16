package com.example.utils;

public class Xyz {
	private float x,y,z;
	private int w,h;
	
	public Xyz(float x, float y, float z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Xyz(int w, int h){
		this.w=w;
		this.h=h;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void setW(int w) {
		this.w = w;
	}

	public void setH(int h) {
		this.h = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

}
