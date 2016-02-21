package com.example.visionface;

import java.io.IOException;

import com.example.visionface.FaceInterfaces.PicDone;
import com.example.visionface.FaceInterfaces.SavePicture;
import com.example.visionface.FaceInterfaces.SmileEvent;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.CameraSource.PictureCallback;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CameraSourcePreview extends ViewGroup {
	
	private static final String TAG = CameraSourcePreview.class.getSimpleName();
	private Context mContext;
	private SurfaceView mSurfaceView;
	private boolean mStartRequested, mSurfaceAvailable;
	private CameraSource mCameraSource;
	private GraphicOverlay mOverlay;
//	private SavePicture save;
	private PicDone picDone;


	public CameraSourcePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext=context;
		this.mStartRequested=false;
		this.mSurfaceAvailable=false;
		this.mSurfaceView = new SurfaceView(context);
		this.mSurfaceView.getHolder().addCallback(new SurfaceCallback());
		addView(this.mSurfaceView);
	}

	//working shit, just try do the same in FaceMarker
	public void setSave(final PicDone done) {
		this.picDone=done;
//		this.save = save;
		if(mCameraSource!=null){
			Log.w(TAG, "try to save in camerasource preview");
			mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] arg0) {
					Log.d(TAG, "picture callback!");
					if(arg0!=null) Log.d(TAG, "picture callback - byte[] size: "+arg0.length);
					else Log.d(TAG, "picture callback - byte[] is NULL!");
					FaceVisionUtils.setByteFace(arg0);
					done.isSaved(true);
					
				}} );
			
		}else Log.w(TAG, "takePicture(): camera source is null");
	}

	public int getSurfaceWidth(){
		return mSurfaceView.getWidth();
	}
	public int getSurfaceHeight(){
		return mSurfaceView.getHeight();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int width = 240, height = 320;
		
		//TODO: set dimension of real screen!!!!
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
				
				//TODO: set real size of screen here??????.......
//				Display display = ac.getWindowManager().getDefaultDisplay();
//				Point scrSize = new Point();
//				display.getSize(scrSize);
//				int width = scrSize.x; int height = scrSize.y;
				int mini, max;
//				mini = Math.min(width, height);
//				max = Math.max(width, height);
				
				Size size = mCameraSource.getPreviewSize();
				
				mini = Math.min(size.getWidth(), size.getHeight());
				max = Math.max(size.getWidth(), size.getHeight());
				
				if(isPortraitMode()) mOverlay.setCameraInfo(mini, max, mCameraSource.getCameraFacing());
				else mOverlay.setCameraInfo(max, mini, mCameraSource.getCameraFacing());
				mOverlay.clear();
			}
			mStartRequested = false;
		}
	}
	

	
	private class SurfaceCallback implements SurfaceHolder.Callback{ //TODO implement call to save surface view here!

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
	

//	private PictureCallback pictureCallback = new CameraSource.PictureCallback() {
//		
//		@Override
//		public void onPictureTaken(byte[] arg0) {
//			if(arg0==null){
//				Log.d(TAG, "picture callback - byte[] is NULL: "+(arg0==null));
//			}else{
//				Log.d(TAG, "picture callback!");
//				Log.d(TAG, "picture callback - byte[] size: "+arg0.length);
////				new SavePic().execute(arg0);
//				FaceVisionUtils.setByteFace(arg0);
//				savePic.isSaved(true);
//			}
//			
//		}
//	};

//	@Override
//	public void isSaved(boolean status) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void saveIt(boolean readyToDo) {
//		if(readyToDo){
//			if(mCameraSource!=null){
//				try{
//					mCameraSource.start(mSurfaceView.getHolder());
//					mCameraSource.takePicture(null,pictureCallback );
//				}catch(RuntimeException | IOException ex){
//					Log.e(TAG, "take pic: "+ex.getMessage());
//					ex.printStackTrace();
//				}finally{
//					Log.d(TAG, "bye, bye from saveIt");
//				}
//			}else Log.w(TAG, "takePicture(): camera source is null");
//		}
//		
//	}



//	@Override
//	public void smiling(boolean isSmile) {
//		// TODO Auto-generated method stub
//				if(isSmile) {  //create bitmap from camera source, release camera source and finish activity
////					Toast.makeText(getApplicationContext(), "it is smiling!", Toast.LENGTH_SHORT).show();
//					Log.d(TAG, "is smiling!");
//					try {
//						mCameraSource.start(mSurfaceView.getHolder());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if(mCameraSource!=null){
//						mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
//							
//							@Override
//							public void onPictureTaken(byte[] arg0) {
//								Log.d(TAG, "picture callback!");
//								if(arg0!=null) Log.d(TAG, "picture callback - byte[] size: "+arg0.length);
//								else Log.d(TAG, "picture callback - byte[] is NULL!");
//								FaceVisionUtils.setByteFace(arg0);
//								savePic.isSaved(true);
//								
//							}} );
//					}else Log.w(TAG, "takePicture(): camera source is null");
//					mCameraSource.release();
//					mCameraSource=null;
//					
//				}
//		
//	}

//	@Override
//	public void saveIt(boolean readyToDo) {
//
//		if(readyToDo) {  //create bitmap from camera source, release camera source and finish activity
////			Toast.makeText(getApplicationContext(), "it is smiling!", Toast.LENGTH_SHORT).show();
//			Log.d(TAG, "is smiling!");
//			try {
//				mCameraSource.start();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(mCameraSource!=null){
//				mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
//					
//					@Override
//					public void onPictureTaken(byte[] arg0) {
//						Log.d(TAG, "picture callback!");
//						if(arg0!=null) Log.d(TAG, "picture callback - byte[] size: "+arg0.length);
//						else Log.d(TAG, "picture callback - byte[] is NULL!");
//						FaceVisionUtils.setByteFace(arg0);
//						done.isSaved(true);
//						
//					}} );
//				
//			}else Log.w(TAG, "takePicture(): camera source is null");
//			mCameraSource.release();
//			mCameraSource=null;
//			
//		}
//	
//	}
	
	
	
	

}
