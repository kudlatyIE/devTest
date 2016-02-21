package com.example.visionface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class FaceLandmarker {
	
	private FaceDetector detector;
	private Paint landmarkPaint;
	private Context c;
	private Bitmap b=null;
	
	public FaceLandmarker(Context context){
		this.c=context;
	}
	
	public  Bitmap addMarks(Bitmap bitmap) throws Exception{
		this.b=bitmap;
		if(bitmap==null) throw new Exception("addMarks(): bitmap is null!");
		landmarkPaint = new Paint();
        landmarkPaint.setStrokeWidth(10);
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.STROKE);
        Canvas  canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
		detector = new FaceDetector.Builder(c).setTrackingEnabled(false).setProminentFaceOnly(true).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
		Frame frame = new Frame.Builder().setBitmap(bitmap).build();
		SparseArray<Face> faces = detector.detect(frame);
		Face f;
		if(faces.size()>0) {
			f = faces.valueAt(0);
			for(Landmark l: f.getLandmarks()){
				PointF p = l.getPosition();
				canvas.drawPoint(p.x, p.y, landmarkPaint);
			}
		}else throw new Exception("No face found!");
		
		return bitmap;
	}

}
