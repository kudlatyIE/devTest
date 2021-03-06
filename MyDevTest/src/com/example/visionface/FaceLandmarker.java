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
		float smile, eulerY, eulerZ, faceCenterX, faceCenterY;
		//check point for biometric: bitmap dimensions;
		Log.d(TAG, "bitmap_W: "+bitmap.getWidth()+" bitmap_H: "+bitmap.getHeight());
		if(faces.size()>0) {
			f = faces.valueAt(0);
			smile = f.getIsSmilingProbability();
			eulerY = f.getEulerY();
			eulerZ = f.getEulerZ();
			Log.d(TAG, "eulerY: "+eulerY +" eulerZ: "+eulerZ);
			if(!FaceVisionUtils.isPoseCorrect(eulerY, eulerZ)) {

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
				detector.release();
				throw new Exception("Missing landmark!");
			}else{
				faceCenterX=f.getPosition().x+f.getWidth()/2;
				faceCenterY=f.getPosition().y+f.getHeight()/2;
				Log.d(TAG, "faceCenter X: "+faceCenterX+" Y: "+faceCenterY);
				//add face center point to hashMap of collected landmarks! (check if granted than zero, just in case);
				for(Landmark l: myList){
				
					PointF p = l.getPosition();
					canvas.drawCircle(p.x, p.y, 2f, landmarkPaint);
					FaceVisionUtils.createLandmark(l.getType(), p);// create map of faces landmarks for biometric extractor!
				}
				if(faceCenterX > 0 && faceCenterY > 0 ) FaceVisionUtils.addCenterFace(FaceVisionUtils.FACE_CENTER, faceCenterX, faceCenterY);
				else throw new Exception("not valid face center coordinates: X: "+faceCenterX+" Y: "+faceCenterY);
//				canvas.drawPoint(faceCenterX, faceCenterY, landmarkPaint);
			}
		}else {

			detector.release();
			throw new Exception("No face found!");
		}
		detector.release();
		return bitmap;
	}

}
