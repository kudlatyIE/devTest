package com.example.visionface;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class FaceLandmarker {
	
	private final static String TAG = FaceLandmarker.class.getSimpleName();
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
		detector = new FaceDetector.Builder(c).setTrackingEnabled(false)
							.setProminentFaceOnly(true)
							.setLandmarkType(FaceDetector.ALL_LANDMARKS)
//							.setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
							.setMode(FaceDetector.ACCURATE_MODE).build();
		Frame frame = new Frame.Builder().setBitmap(bitmap).build();
		SparseArray<Face> faces = detector.detect(frame);
		Face f;
		float smile, eulerY, eulerZ;
		
		if(faces.size()>0) {
			f = faces.valueAt(0);
			smile = f.getIsSmilingProbability();
			eulerY = f.getEulerY();
			eulerZ = f.getEulerZ();
			Log.d(TAG, "eulerY: "+eulerY +" eulerZ: "+eulerZ);
			if(!FaceVisionUtils.isPoseCorrect(eulerY, eulerZ)) {
//				FaceVisionUtils.resetFaces();
//				FaceVisionUtils.resetSmile();
				detector.release();
				throw new Exception("Incorrec pose!");
			}
//			for(Landmark l: f.getLandmarks()){
//				
//				PointF p = l.getPosition();
//				canvas.drawPoint(p.x, p.y, landmarkPaint);
//			}
			List<Landmark> myList = f.getLandmarks();
			int[] all = FaceVisionUtils.allLandmarks();
			if(myList.size()!=all.length) {
//				FaceVisionUtils.resetFaces();
//				FaceVisionUtils.resetSmile();
				detector.release();
				throw new Exception("Missing landmark!");
			}else{
				for(Landmark l: myList){
				
					PointF p = l.getPosition();
					canvas.drawPoint(p.x, p.y, landmarkPaint);
				}
			}
		}else {
//			FaceVisionUtils.resetFaces();
//			FaceVisionUtils.resetSmile();
			detector.release();
			throw new Exception("No face found!");
		}
//		FaceVisionUtils.resetFaces();
//		FaceVisionUtils.resetSmile();
		detector.release();
		return bitmap;
	}

}
