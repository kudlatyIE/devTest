package com.example.visionface;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.utils.Xyz;
import com.google.android.gms.vision.CameraSource;

public class GraphicOverlay extends View{
	
	private final Object mLock = new Object();
	private int mPreviewWidth, mPreviewHeight;
	private float widthScaleFactor = 1f,heightScaleFactor=1f;
	
	private int mFacing = CameraSource.CAMERA_FACING_FRONT; // try if front camera support face detect!
	
	private Set<Graphic> mGraphic = new HashSet<Graphic>();
	
	public static abstract class Graphic{
		private GraphicOverlay mOverlay;
//		private int offX;
		
		public Graphic(GraphicOverlay overlay){
			this.mOverlay=overlay;
//			this.offX=x;
		}
		public abstract void draw(Canvas canvas);
		
		public float scaleX(float horizontal){
			return horizontal*mOverlay.widthScaleFactor;
		}
		
		public float scaleY(float vertical){
			return vertical*mOverlay.heightScaleFactor;
		}
		
		public float translateY(float y){
			return scaleY(y);
		}
		
		public float translateX(float x){
//			if(mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT){
//				return mOverlay.getWidth() - scaleX(x);
//			}
			//return mirror view
			return scaleX(x);
		}
		
		public float translateMirrorX(float x){
			float frog = mOverlay.getWidth() - x;
//			Log.i("TRANSLATE_X", "X: "+x+ "mX: "+frog);
			return frog;
		}
		public float translateMirrorY(float y){
			float frog = mOverlay.getHeight() - y;
//			Log.i("TRANSLATE_Y", "Y: "+y+ "mY: "+frog);
			return frog;
		}
		
		public void postInvalidate(){
			mOverlay.postInvalidate();
		}
		
	}
	
	public GraphicOverlay(Context context, AttributeSet attribs){
		super(context,attribs);
	}
	
	public void clear(){
		synchronized(mLock){
			mGraphic.clear();
		}
		postInvalidate();
	}
	
	public void add(Graphic graphic){
		synchronized(mLock){
			mGraphic.add(graphic);
		}
		postInvalidate();
	}
	
	public void remove(Graphic graphic){
		synchronized(mLock){
			mGraphic.remove(graphic);
		}
		postInvalidate();
	}

	
	public void setCameraInfo(int previewWidth, int previewHeight, int facing){
		synchronized(mLock){
			this.mPreviewHeight = previewHeight;
			this.mPreviewWidth=previewWidth;
			this.mFacing=facing;
		}
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		synchronized(mLock){
			if((mPreviewWidth!=0) &&(mPreviewHeight!=0)){
				widthScaleFactor = canvas.getWidth();// /mPreviewWidth;// may be need to cast to float?
				heightScaleFactor = canvas.getHeight();// /mPreviewHeight;
			}
			for(Graphic g: mGraphic) g.draw(canvas);
		}
	}
	
}
