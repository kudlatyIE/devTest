package com.example.visionface;

import java.util.List;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;

public class FaceMarkers extends GraphicOverlay.Graphic{
	
	private final static String TAG = "FACE";
	private final static float FACE_POSITION_RADIUS = 10f, ID_TEXT_SIZE= 30f, ID_X_OFFSET=20f, ID_Y_OFFSET=50f, BOX_STROKE = 5f;
	private final static int [] COLORS = {Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.YELLOW};
	private static int colorIndex = 0;
	private Paint mFacePositionPaint, mIdPaint, mBoxPaint, landmarksPaint, center;
	private volatile Face mFace;
	private int mFaceId;
	private float mFaceHappiness;
	private List<Landmark> landmark;
	

	public FaceMarkers(GraphicOverlay overlay) {
		super(overlay);
		colorIndex = (colorIndex + 1) % COLORS.length;
		final int selectedColor = COLORS[colorIndex];
		
		mFacePositionPaint = new Paint();
		mFacePositionPaint.setColor(selectedColor);
		
		mIdPaint = new Paint();
		mIdPaint.setColor(selectedColor);
		mIdPaint.setTextSize(ID_TEXT_SIZE);
		
		mBoxPaint = new Paint();
		mBoxPaint.setColor(selectedColor);
		mBoxPaint.setStyle(Paint.Style.STROKE);
		mBoxPaint.setStrokeWidth(BOX_STROKE);
		
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
	

	@Override
	public void draw(Canvas canvas) {
		Face face = mFace;
		if(face==null) return;
		
		float smile, eulerY, eulerZ;
		
		//mark landmarks
		for(Landmark mark: face.getLandmarks()){
			
			PointF pos = mark.getPosition();
			
			canvas.drawPoint(translateMirrorX(pos.x), pos.y, landmarksPaint);
			
		}
		
		
		//mark a face and eyes
		
		float x,y;
		

		//info position in box
		x = 0; y = 0;
//		canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
//		canvas.drawText("ID: "+mFaceId, x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
		canvas.drawText("smile: "+String.format("%.2f", face.getIsSmilingProbability()), x+ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
		canvas.drawText("R eye: "+String.format("%.2f", face.getIsRightEyeOpenProbability()),x+ID_X_OFFSET, y + ID_Y_OFFSET*2, mIdPaint);
		canvas.drawText("L eye: "+String.format("%.2f", face.getIsLeftEyeOpenProbability()), x+ID_X_OFFSET, y + ID_Y_OFFSET*3, mIdPaint);
		canvas.drawText("Y angle: "+String.format("%.2f", face.getEulerY()), x+ID_X_OFFSET, y + ID_Y_OFFSET*4, mIdPaint);
		canvas.drawText("Z angle: "+String.format("%.2f", face.getEulerZ()), x+ID_X_OFFSET, y + ID_Y_OFFSET*5, mIdPaint);
		
		PointF o = face.getPosition();//top left position!
		
		float w = face.getWidth(); float h = face.getHeight();
//		Log.i(TAG, "Face Position X: "+o.x+" Y: "+o.y);
//		Log.i(TAG, "Face Width: "+face.getWidth()+" Height: "+face.getHeight());
		canvas.drawLine(o.x, o.y+h/2,o.x+w, o.y+h/2, landmarksPaint);
		
		//track face center
		canvas.drawPoint(translateMirrorX(o.x+w/2),o.y+h/2, center);

		
		float xOffset, yOffset, left, top, right,bottom;

		xOffset = scaleX(face.getWidth()/2f);
		yOffset = scaleY(face.getHeight()/2f);

		left = x;
		right = x + ID_Y_OFFSET*6;
		top = y;
		bottom = y+ ID_Y_OFFSET*6+10;
		
		canvas.drawRect(left, top, right, bottom, mBoxPaint);
		
	}
	
	

}
