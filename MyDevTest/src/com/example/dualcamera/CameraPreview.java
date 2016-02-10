package com.example.dualcamera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
	
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private final static String TAG = CameraPreview.class.getSimpleName();

	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.mCamera=camera;
		this.mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	}
	
	public static Camera getCamera(int cameraId){
		
		Camera c = null;
		try{
			c = Camera.open(cameraId);
		}catch(Exception e){
			Log.e(TAG, "unavaliable camera no: "+cameraId);
		}
		
		return c;
	}
	
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "camera preview, setting error: "+e.getMessage());
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if(mHolder.getSurface()==null) return;
		
		try{
			mCamera.stopPreview();
		}catch(Exception e){}
		
		try{
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		}catch(Exception e){
			Log.e(TAG, "camera preview error: "+e.getMessage());
		}
		
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	public static void lumos(Camera c, int expo){
		Camera.Parameters param = c.getParameters();
		
		Log.i(TAG, "Expo Min: "+param.getMinExposureCompensation());
		Log.i(TAG,"Expo Max: "+param.getMaxExposureCompensation());
		param.setExposureCompensation(expo);
		c.setParameters(param);
	}
	/**
	 * set area for metering and focusing
	 * @param c Camera
	 */
	@SuppressWarnings("deprecation")
	public static void setFaceArea(Camera c){
		CameraInfo ci = new CameraInfo();
		Camera.Parameters param = c.getParameters();
		int areaNum = param.getMaxNumMeteringAreas();
		Log.d(TAG, "metering areas number: "+areaNum);
//		areaNum = param.getMeteringAreas().size();
//		Log.d(TAG, "metering areas number: "+areaNum);
		if(areaNum>0){
			List<Camera.Area> areas = new ArrayList<Camera.Area>();
			
			Rect rec = new Rect(-500, -500, 500, 500);
			areas.add(new Camera.Area(rec, 600));
			param.setMeteringAreas(areas);
			c.setParameters(param);

		}else Log.d(TAG, "New Metering areas not set: "+param.getMaxNumMeteringAreas());
		
	}
	
	public static void setNewWhiteBalance(Camera c){
		
		CameraInfo ci = new CameraInfo();
		Camera.Parameters param = c.getParameters();
		param.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_FLUORESCENT);
		c.setParameters(param);
	}
	
	
	public static void setPortrait(Activity ac, Camera c, int id){
		
		CameraInfo ci = new CameraInfo(); 
		Camera.getCameraInfo(id, ci);
		
		int rotation  = ac.getWindowManager().getDefaultDisplay().getRotation();
		int dgr=0;
		switch(rotation){
		case Surface.ROTATION_0: dgr=0;
			break;
		case Surface.ROTATION_90: dgr=90;
			break;
		case Surface.ROTATION_180: dgr=180;
			break;
		case Surface.ROTATION_270: dgr=270;
			break;
		}
		int result;
		if(ci.facing==CameraInfo.CAMERA_FACING_FRONT) {
			result = (ci.orientation+dgr)%360;
			result = (360-result)%360;
		}else{
			result = (ci.orientation - dgr +360)%360;
		}
		c.setDisplayOrientation(result);
		
	}
	
	

}
