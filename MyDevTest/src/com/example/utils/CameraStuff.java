package com.example.utils;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Sensor;

public class CameraStuff {
	
	private Context context;
	private Sensor sensor;
	private Camera camera;
	private CameraInfo info;
	private int camNumber, CameraId, height, width;
	private String cameraName, sensorInfo;
	private ArrayList<CameraStuff> myList;
	
	public CameraStuff(Context context){
		this.context=context;
	}
//	CameraStuff(int id, String name, Camera cam){
//		this.CameraId=id;
//		this.cameraName=name;
//		this.camera=cam;
//	}
	CameraStuff(int id, String name, int h, int w){
		this.CameraId=id;
		this.cameraName=name;
		this.width=w;
		this.height=h;
	}
	
	@SuppressWarnings("deprecation")
	public ArrayList<CameraStuff> getCamerasList(){
		ArrayList<CameraStuff> list = new ArrayList<CameraStuff>();
		camNumber = Camera.getNumberOfCameras();
		System.out.println("Camera num: "+camNumber);
		for (int i=0;i<camNumber;i++){
			camera=null;
			cameraName="unknown";
			camera = Camera.open(i);
			info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if( info.facing==CameraInfo.CAMERA_FACING_BACK) cameraName="BACK";
			if( info.facing==CameraInfo.CAMERA_FACING_FRONT) cameraName="FRONT";
//			list.add(new CameraStuff(i,cameraName, camera));
			try{
				width = camera.getParameters().getPictureSize().width;
				height = camera.getParameters().getPictureSize().height;
			}catch(Exception ex){
				System.out.println("Error get pisture size.....");
			}
			list.add(new CameraStuff(i,cameraName, height, width));
			camera.release(); //camera=null;
		}
		return list;
	}
	
	@SuppressWarnings("deprecation")
	public static int getFrontCameraId() throws Exception{
		Camera camera;
		CameraInfo info;
		int id=0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		int camNumber = Camera.getNumberOfCameras();
		System.out.println("Camera num: "+camNumber);
		for (int i=0;i<camNumber;i++){
			camera=null;
			camera = Camera.open(i);
			info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if( info.facing==CameraInfo.CAMERA_FACING_FRONT) id=i;
		}
		if (id!=0) return id;
		else throw new Exception("No front camera detected!");
	}
	
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	public int getCameraId() {
		return CameraId;
	}
	public void setCameraId(int cameraId) {
		CameraId = cameraId;
	}
	public String getCameraName() {
		return cameraName;
	}
	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	

}
