package com.example.utils;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class SensiSensors {
	
	private SensorManager seseManager;
	private Sensor sensorLight, sensorAcc,sensorNoise, sensorRotate;
	private Context c;
	private float light, noise,accX, accY, accZ,rotX, rotY, rotZ;
	private double mini=0, max=0;
	private static SensiSensors sensors=null;
	private ArrayList<Float> lightArr,noiseArr;
	private ArrayList<Xyz> accArr, rotateArr;
	private SensorEventListener listenerLight, listenerAcc, listenerRotate, listenerNoise;

	
	private TextView tvLight, tvAcc,tvNoise, tvRotate;
	
	public SensiSensors(Context context){
		this.c=context;
		this.seseManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		for(Sensor sese: seseManager.getSensorList(Sensor.TYPE_ALL)){
			Log.i("SENSORS", sese.getName()+" STATUS: "+sese.getPower());
		}
		if(SensiSensors.sensors ==null) SensiSensors.sensors = new SensiSensors(0,0,0,0);
	}
	
	/*
	 * will use for create final average for each of sensor
	 */
	private SensiSensors(float light, float noise, float vibration, float other){
		this.light=light;
		this.noise=noise;
//		this.vibration=vibration;
//		this.other=other;
	}
	
	public double checkLight(TextView display){
//		sensorLight;
		this.tvLight=display;

		lightArr = new ArrayList<Float>();
		listenerLight = new SensorEventListener(){

			@Override
			public void onSensorChanged(SensorEvent event) {
				float x;
				x=event.values[0];
				Log.d("LIGHT", "light: "+x);
//				miniLight=x; maxLight=x;
				lightArr.add(Float.valueOf(x));
				
				tvLight.setText(String.valueOf(x));
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		sensorLight = seseManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if(sensorLight!=null){
			seseManager.registerListener(listenerLight, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
			
		}
		return 0;
	}
	
	public void checkAcceleration(TextView display){
		this.tvAcc=display;
		accArr = new ArrayList<Xyz>();
		 listenerAcc = new SensorEventListener(){
				float x,y,z;
				@Override
				public void onSensorChanged(SensorEvent event) {
					x=Math.abs(event.values[0]);
					y=Math.abs(event.values[1]);
					z=Math.abs(event.values[2]);
					accArr.add(new Xyz(x,y,z));
					tvAcc.setText("X: "+String.valueOf(x)+"\n"+
									"Y: "+String.valueOf(y)+"\n"+
									"Y: "+String.valueOf(y));
				}
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
				}
			};
		sensorAcc = seseManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(sensorAcc!=null) seseManager.registerListener(listenerAcc, sensorAcc, SensorManager.SENSOR_DELAY_UI);
	}
	
	public void checkRotation(TextView display){
		this.tvRotate=display;
		rotateArr= new ArrayList<Xyz>();
		 listenerRotate = new SensorEventListener(){
				float x,y,z;
				@Override
				public void onSensorChanged(SensorEvent event) {
					// TODO Auto-generated method stub
					x=Math.abs(event.values[0]);
					y=Math.abs(event.values[1]);
					z=Math.abs(event.values[2]);
					rotateArr.add(new Xyz(x,y,z));
					tvRotate.setText("X: "+String.valueOf(x)+"\n"+
									"Y: "+String.valueOf(y)+"\n"+
									"Y: "+String.valueOf(y));
				}
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
				}
			};
		sensorRotate = seseManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		if(sensorRotate!=null) seseManager.registerListener(listenerRotate, sensorRotate, SensorManager.SENSOR_DELAY_UI);
	}
	
	public void checkNoise(TextView display){
		this.tvNoise=display;

		noiseArr = new ArrayList<Float>();
		listenerNoise = new SensorEventListener(){

				float x;
				@Override
				public void onSensorChanged(SensorEvent event) {
					// TODO Auto-generated method stub
					x=event.values[0];
					if(noise < x) noise= x;
					tvNoise.setText(String.valueOf(noise));
				}
				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
				}
			};
//		sensorNoise = seseManager.getDefaultSensor(.....)
			// there will be my custom sound listener....
	}
	

	
	
	
	public float getLight(){
		return light;
	}

	public float getNoise() {
		return noise;
	}

//	public float getVibration() {
//		return vibration;
//	}
//
//	public float getOther() {
//		return other;
//	}

	public static SensiSensors getSensors() {
		return sensors;
	}


	public void setLight(float light) {
		this.light = light;
	}

	public void setNoise(float noise) {
		this.noise = noise;
	}

//	public void setVibration(float vibration) {
//		this.vibration = vibration;
//	}
//
//	public void setOther(float other) {
//		this.other = other;
//	}

	public static void setSensors(SensiSensors sensors) {
		SensiSensors.sensors = sensors;
	}
	
	public boolean disableSensorManager(){
		boolean result=false;
		try{
			this.seseManager.unregisterListener(listenerLight, sensorLight);
			tvLight.setText("average: "+getAverage1D(lightArr));
			result=true;
			Log.i("SENSOR", "light sensor listener disabled: "+result);
		}catch(Exception ex){
			Log.e("SENSOR", "light sensor listener disabled: "+result);
			ex.printStackTrace();
			result=false;
		}
		try{
			this.seseManager.unregisterListener(listenerNoise, sensorNoise);	
			result=true;
			Log.i("SENSOR", "noise sensor listener disabled: "+result);
		}catch(Exception ex){
			Log.e("SENSOR", "noise sensor listener disabled: "+result);
			ex.printStackTrace();
			result=false;
		}
		try{
			this.seseManager.unregisterListener(listenerAcc, sensorAcc);	
			result=true;
			tvAcc.setText(getAverage3D(accArr));
			Log.i("SENSOR", "vibration sensor listener disabled: "+result);
		}catch(Exception ex){
			Log.e("SENSOR", "vibration sensor listener disabled: "+result);
			ex.printStackTrace();
			result=false;
		}
		try{
			this.seseManager.unregisterListener(listenerRotate, sensorRotate);	
			result=true;
			tvRotate.setText(getAverage3D(rotateArr));
			Log.i("SENSOR", "other sensor listener disabled: "+result);
		}catch(Exception ex){
			Log.e("SENSOR", "other sensor listener disabled: "+result);
			ex.printStackTrace();
			result=false;
		}
		
		
		return result;
	}
	
	private String getAverage1D(ArrayList<Float> list){
		double total=0;
		mini=list.get(0);
		max=list.get(0);
		for(float d: list){
			total=total+d;
			if(mini > d) mini = d;
			if(max < d) max= d;
		}
		Log.d("SENSOR", "ARR Light total: "+total);
		Log.d("SENSOR", "ARR Light size: "+list.size());
		return total/list.size()+"\n MINI: "+mini+", MAX: "+max;
	}
	
	private String getAverage3D(ArrayList<Xyz> list){
		int size = list.size();
		double totalX=0, totalY=0, totalZ=0;
		double minX,minY,minZ, maxX,maxY, maxZ;
		minX=list.get(0).getX();
		minY=list.get(0).getY();
		minZ=list.get(0).getZ();
		maxX=list.get(0).getX();
		maxY=list.get(0).getY();
		maxZ=list.get(0).getZ();
		for(Xyz d:list){
			totalX = totalX+d.getX();
			totalY=totalY+d.getY();
			totalZ=totalZ+d.getZ();
			if(minX>d.getX()) minX=d.getX();
			if(maxX<d.getX()) maxX=d.getX();
			if(minY>d.getY()) minY=d.getY();
			if(maxY<d.getY()) maxY=d.getY();
			if(minZ>d.getZ()) minZ=d.getZ();
			if(maxZ<d.getZ()) maxZ=d.getZ();
		}
		Log.d("SENSOR", "ARR ACC X total: "+totalX);
		Log.d("SENSOR", "ARR ACC Y total: "+totalY);
		Log.d("SENSOR", "ARR ACC Y total: "+totalY);
		Log.d("SENSOR", "ARR ACC size: "+list.size());
		
		return "X: "+totalX/size +"\n minX: "+minX+",maxX: "+maxY+"\n"+
				"Y: "+totalY/size +"\n minY: "+minY+",maxY: "+maxY+"\n"+
				"Z: "+totalZ/size +"\n minZ: "+minZ+",maxZ: "+maxZ;
	}



}
