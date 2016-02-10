package com.example.serwisy;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class SensorServiceConnection implements ServiceConnection{
	
	private final static String TAG = SensorServiceConnection.class.getSimpleName();
	
	private ServiceMonitor mMonitor;
	private ServiceListener mListener;
	
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		
		Log.d(TAG, "Sensor Service is connected!");
		mMonitor = (ServiceMonitor) service;
		mMonitor.setListener(mListener);
	}
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Sensor service is disconnected!");
		mMonitor = null;
		
	}
	
	public void setSensorServiceListener(ServiceListener listener){
		mListener = listener;
	}
	

}
