package com.example.visionface;

import com.google.android.gms.vision.face.Face;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FaceMarkers extends GraphicOverlay.Graphic{
	
	private final static float FACE_POSITION_RADIUS = 10f, ID_TEXT_SIZE= 40f, ID_X_OFFSET=50f, ID_Y_OFFSET=50f, BOX_STROKE = 5f;
	private final static int [] COLORS = {Color.RED, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.WHITE, Color.YELLOW};
	private static int colorIndex = 0;
	private Paint mFacePositionPaint, mIdPaint, mBoxPaint;
	private volatile Face mFace;
	private int mFaceId;
	private float mFaceHappiness;
	

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
		
		
		//mark a face and eyes
		
		float x,y;
		x = translateX(face.getPosition().x + face.getWidth());
		y = translateY(face.getPosition().y + face.getHeight());
		canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
		canvas.drawText("ID: "+mFaceId, x+ID_X_OFFSET, y+ID_Y_OFFSET, mIdPaint);
		canvas.drawText("happiness: "+String.format("%.2f", face.getIsSmilingProbability()), x-ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
		canvas.drawText("R eye: "+String.format("%.2f", face.getIsRightEyeOpenProbability()),x+ID_X_OFFSET*2, y+ID_Y_OFFSET*2, mIdPaint);
		canvas.drawText("L eye: "+String.format("%.2f", face.getIsLeftEyeOpenProbability()), x-ID_X_OFFSET*2, y-ID_Y_OFFSET*2, mIdPaint);
		
		//box on the face
		
		float xOffset, yOffset, left, top, right,bottom;
		
		xOffset = scaleX(face.getWidth()/2f);
		yOffset = scaleY(face.getHeight()/2f);
		left = x - xOffset;
		right = x + xOffset;
		top = y - yOffset;
		bottom = y+ yOffset;
		
		canvas.drawRect(left, top, right, bottom, mBoxPaint);
		
	}
	
	

}
