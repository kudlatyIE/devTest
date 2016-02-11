package com.example.visionface;

import com.google.android.gms.vision.CameraSource;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraSourcePreview extends ViewGroup{
	
	private static final String TAG = CameraSourcePreview.class.getSimpleName();
	private Context context;
	private SurfaceView mSurfaceView;
	private boolean mSurfaceRequest, mSurfaceAvailable;
	private CameraSource mCameraSource;

	public CameraSourcePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		
		TO BE CONTINUE:
			https://github.com/googlesamples/android-vision/blob/master/visionSamples/FaceTracker/app/src/main/java/com/google/android/gms/samples/vision/face/facetracker/ui/camera/CameraSourcePreview.java
				
	}

}
