package com.example.visionface;

import java.util.List;

import com.example.mydevtest.VisionFaceTrackerActivity;
import com.example.visionface.FaceInterfaces.PicDone;
import com.example.visionface.FaceInterfaces.SmileEvent;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;
import android.widget.Toast;

public class FaceMarkers extends GraphicOverlay.Graphic{
	
	private final static String TAG = FaceMarkers.class.getSimpleName();
	private final static float FACE_POSITION_RADIUS = 10f, ID_TEXT_SIZE= 40f, ID_X_OFFSET=20f, ID_Y_OFFSET=50f, BOX_STROKE = 5f;
	private final static int [] COLORS = {Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.YELLOW};
	private static int colorIndex = 0;
	private Paint mFacePositionPaint, mIdPaint, mBoxPaint, landmarksPaint, center, msgBoxPaint, msgBoxBorder;
	private volatile Face mFace;
	private int mFaceId;
	private float mFaceHappiness;
	private List<Landmark> landmark;
	private static boolean gotSmile = false;
	private static int smileNum = 0;
	private CameraSource mCameraSource;
	private SmileEvent smileEvent;
//	private PicDone picDone;
	private static Canvas mCanvas = null;
	private Face face;
	private static Bitmap btmFace, temp;
	

	public FaceMarkers(CameraSource source, SmileEvent event, GraphicOverlay overlay) {
		super(overlay);
		this.smileEvent=event;
//		this.done=picDone;
		this.mCameraSource=source;
//		colorIndex = (colorIndex + 1) % COLORS.length;
//		final int selectedColor = COLORS[colorIndex];
		
		final int selectedColor = Color.WHITE;
		mFacePositionPaint = new Paint();
		mFacePositionPaint.setColor(selectedColor);
		
		mIdPaint = new Paint();
		mIdPaint.setColor(selectedColor);
		mIdPaint.setTextSize(ID_TEXT_SIZE);
		
		mBoxPaint = new Paint();
		mBoxPaint.setColor(selectedColor);
		mBoxPaint.setStyle(Paint.Style.STROKE);
		mBoxPaint.setStrokeWidth(BOX_STROKE);
		
		msgBoxPaint = new Paint();
		msgBoxPaint.setColor(Color.BLACK);
		msgBoxPaint.setStyle(Paint.Style.FILL);
		msgBoxBorder = new Paint();
		msgBoxBorder.setColor(Color.RED);
		msgBoxBorder.setStyle(Paint.Style.STROKE);
	
		
		landmarksPaint = new Paint();
        landmarksPaint.setStrokeWidth(10);
        landmarksPaint.setColor(Color.RED);
        landmarksPaint.setStyle(Paint.Style.STROKE);
        
        center = new Paint();
        center.setStrokeWidth(10);
        center.setColor(Color.GREEN);
        center.setStyle(Paint.Style.STROKE);
	}
	

	
	public void setId(int id){
		this.mFaceId=id;
	}
	
	public void updateFace(Face face){
		mFace = face;
		postInvalidate();
	}
	
	
//	public void setSaveFace(final PicDone done) {
//
//		if(mCameraSource!=null){
//			Log.w(TAG, "try to save in face maker");
//			mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
//			
//				@Override
//				public void onPictureTaken(byte[] byteFace) {
//					Log.d(TAG, "picture callback!");
//					if(byteFace!=null) Log.d(TAG, "picture callback - byte[] size: "+byteFace.length);
//					else Log.d(TAG, "picture callback - byte[] is NULL!");
//					FaceVisionUtils.setByteFace(byteFace);
//					done.isSaved(true);
//			}} );
//		
//		}else Log.w(TAG, "takePicture(): camera source is null");
//	}
	
	/**
	 * TODO: when smile detected and face pose is correct, take pic and sent smileCallabck 
	 */
	@Override
	public void draw(final Canvas canvas) {
		this.face = mFace;
		
		if(face==null) return;
		//info position in box
		float x,y,  left, top, right,bottom;		
		x = 0; y = 0;

		left = x;
		right = canvas.getWidth();//x + ID_Y_OFFSET*6;
		top = y;
		bottom = y+ ID_Y_OFFSET+10;
		
		float smile, eulerY, eulerZ;
		smile = face.getIsSmilingProbability();
		eulerY = face.getEulerY();
		eulerZ = face.getEulerZ();
		
		//mark landmarks
		for(Landmark mark: face.getLandmarks()){			
			PointF pos = mark.getPosition();
			canvas.drawPoint(translateMirrorX(pos.x), pos.y, landmarksPaint);
		}
		if(FaceVisionUtils.isPoseCorrect(eulerY, eulerZ)){
			FaceVisionUtils.addSmile(smile);//detect smile when face is posing
			if(FaceVisionUtils.isMakeSmile()) {
				
				if(FaceVisionUtils.getSmileNum()<1){
					Log.v(TAG, "That was a smile, yeah!!!!!");
//					smileEvent.smiling(true);
					FaceVisionUtils.increaseSmileNum();
					//take pic when smile detected!
					if(mCameraSource!=null){
						Log.w(TAG, "try to save camerasource preview in FaceMarkers");
						mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
							
							@Override
							public void onPictureTaken(byte[] byteFace) {
								Log.d(TAG, "picture callback!");
								if(byteFace!=null) Log.d(TAG, "picture callback - byte[] size: "+byteFace.length);
								else Log.d(TAG, "picture callback - byte[] is NULL!");
								FaceVisionUtils.setByteFace(byteFace);
								smileEvent.smiling(true);
							}} );
						
						
					}else {
						Log.w(TAG, "takePicture(): camera source is null");
					}
				}
				//smile callback - time to take a picture! TEST ONLY!
			}else{
				//TODO: text canvas: please smile to camera
				canvas.drawRect(left, top, right, bottom, msgBoxPaint);
				canvas.drawRect(left, top, right, bottom, msgBoxBorder);
				canvas.drawText("Please smile", x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
			}
		}else{
			FaceVisionUtils.resetSmile();
			//TDOD: text canvas: please look straight to camera and smile
			canvas.drawRect(left, top, right, bottom, msgBoxPaint);
			canvas.drawRect(left, top, right, bottom, msgBoxBorder);
			canvas.drawText("Please, look at camera and smile", x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
		}
		

		
//		canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
//		canvas.drawText("ID: "+mFaceId, x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
		
		
//		canvas.drawText("smile: "+String.format("%.2f", smile), x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
//		canvas.drawText("R eye: "+String.format("%.2f", face.getIsRightEyeOpenProbability()),x+ID_X_OFFSET, y + ID_Y_OFFSET*2, mIdPaint);
//		canvas.drawText("L eye: "+String.format("%.2f", face.getIsLeftEyeOpenProbability()), x+ID_X_OFFSET, y + ID_Y_OFFSET*3, mIdPaint);
//		canvas.drawText("Y angle: "+String.format("%.2f", eulerY), x+ID_X_OFFSET, y + ID_Y_OFFSET*4, mIdPaint);
//		canvas.drawText("Z angle: "+String.format("%.2f", eulerZ), x+ID_X_OFFSET, y + ID_Y_OFFSET*5, mIdPaint);
//		
		PointF o = face.getPosition();//top left position!
//		
		float w = face.getWidth(); float h = face.getHeight();
//		Log.i(TAG, "Face Position X: "+o.x+" Y: "+o.y);
//		Log.i(TAG, "Face Width: "+face.getWidth()+" Height: "+face.getHeight());
//		canvas.drawLine(o.x, o.y+h/2,o.x+w, o.y+h/2, landmarksPaint);
//		
//		//track face center
		canvas.drawPoint(translateMirrorX(o.x+w/2),o.y+h/2, center);
//
//		
//		float xOffset, yOffset, left, top, right,bottom;
//
//		xOffset = scaleX(face.getWidth()/2f);
//		yOffset = scaleY(face.getHeight()/2f);
//
//		left = x;
//		right = x + ID_Y_OFFSET*6;
//		top = y;
//		bottom = y+ ID_Y_OFFSET*6+10;
//		
//		canvas.drawRect(left, top, right, bottom, mBoxPaint);
		
	}
	
	

}
