package com.example.dualcamera;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.util.Log;
import android.widget.FrameLayout;

@SuppressWarnings("deprecation")
public class FaceDetectListener implements Camera.FaceDetectionListener{
	
	private final static String TAG = FaceDetectListener.class.getSimpleName();
	private Activity ac;
	private FrameLayout fr;
	
	public FaceDetectListener(Activity activity, FrameLayout frame){
		this.ac=activity;
		this.fr=frame;
	}

	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		
		if(faces.length>0){
			Log.d(TAG, "detected "+faces.length+" faces\n Face_1 location X: "+faces[0].rect.centerX()+" Y: "+faces[0].rect.centerY());
		}
		
	}

}
