package com.example.visionface;

import java.io.IOException;

import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraSourcePreview extends ViewGroup{
	
	private static final String TAG = CameraSourcePreview.class.getSimpleName();
	private Context mContext;
	private SurfaceView mSurfaceView;
	private boolean mStartRequested, mSurfaceAvailable;
	private CameraSource mCameraSource;
	
	private GraphicOverlay mOverlay;

	public CameraSourcePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		this.mStartRequested=false;
		this.mSurfaceAvailable=false;
		this.mSurfaceView = new SurfaceView(context);
		this.mSurfaceView.getHolder().addCallback(new SurfaceCallback());
		addView(this.mSurfaceView);
		
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int width = 320, height = 240;
		if(this.mCameraSource!=null){
			Size size = this.mCameraSource.getPreviewSize();
			if(size!=null){
				width = size.getWidth();
				height = size.getHeight();
			}
		}
		if(isPortraitMode()){
			int temp = width;
			width = height;
			height=temp;
		}
		
		final int layoutWidth = right - left;
		final int layoutHeight = bottom - top;
		
		int childWidth = layoutWidth;
		int childHeight = (int)((float)layoutHeight/(float)width)*height;
		
		if(childHeight > layoutHeight){
			childHeight = layoutHeight;
			childWidth = (int)((float)layoutWidth/(float)height)*width;
		}
		for(int i=0; i<getChildCount(); i++){
			getChildAt(i).layout(0, 0, childWidth, childHeight);
		}
		try {
			startIfReady();
		} catch (IOException e) {
			Log.e(TAG, "Could not start camera source: "+e.getMessage());
		}
		
		
		
	}// END onLayout()------------------
	
	private boolean isPortraitMode(){
		
		int orientation = this.mContext.getResources().getConfiguration().orientation;
		if(orientation==Configuration.ORIENTATION_PORTRAIT) {
			Log.i(TAG, "detected Portrait orientation");
			return true;
		}
		if(orientation==Configuration.ORIENTATION_LANDSCAPE) {
			Log.i(TAG, "detected Landscape orientation");
			return false;
		}
		
		Log.i(TAG, "cano't detecte screen orientation o_O");
		return false;
	}
	
	public void start(CameraSource source) throws IOException{
		if(source==null) stop();
		mCameraSource = source;
		if(mCameraSource!=null){
			this.mStartRequested=true;
			startIfReady();
		}
	}
	
	public void start(CameraSource source, GraphicOverlay overlay) throws IOException{
		this.mOverlay=overlay;
		start(source);
	}
	
	public void stop(){
		if(this.mCameraSource!=null) this.mCameraSource.stop();
	}
	
	public void release(){
		if(this.mCameraSource!=null){
			this.mCameraSource.release();
			this.mCameraSource=null;
		}
	}
	
	public void startIfReady() throws IOException{
		if(this.mStartRequested && this.mSurfaceAvailable){
			this.mCameraSource.start(this.mSurfaceView.getHolder());
			if(mOverlay!=null){
				Size size = mCameraSource.getPreviewSize();
				int mini, max;
				mini = Math.min(size.getWidth(), size.getHeight());
				max = Math.max(size.getWidth(), size.getHeight());
				if(isPortraitMode()) mOverlay.setCameraInfo(mini, max, mCameraSource.getCameraFacing());
				else mOverlay.setCameraInfo(max, mini, mCameraSource.getCameraFacing());
				mOverlay.clear();
			}
			mStartRequested = false;
		}
	}
	

	
	private class SurfaceCallback implements SurfaceHolder.Callback{

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceAvailable = true;

			try {
				startIfReady();
			} catch (IOException e) {
				Log.e(TAG, "Could not start camera source: "+e.getMessage());
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mSurfaceAvailable = false;
		}
		
	}
	
	
	
	

}
