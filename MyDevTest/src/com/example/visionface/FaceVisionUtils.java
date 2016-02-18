package com.example.visionface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.utils.Xyz;
import com.google.android.gms.vision.face.Landmark;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

public class FaceVisionUtils {
	
	private static Xyz xy;
	private final static float eulerYMax=10f, eulerZMax=5f, smileChange=0.4f;
//	public final static String EYE_L="eye_left", EYE_R="eye_right",NOSE_BASE="nose_base", MOUTH_CENTER="mouth_center";
	public final static Integer EYE_L = Landmark.LEFT_EYE, EYE_R=Landmark.RIGHT_EYE,
							MOUTH_L = Landmark.LEFT_MOUTH, MOUTH_R = Landmark.RIGHT_MOUTH,
							MOUTH_B = Landmark.BOTTOM_MOUTH, NOSE = Landmark.NOSE_BASE;
	
	private static List<Float> smileList;
	private static float startSmile=0, endSmile, topSmile=0;
	private static Bitmap btm;
	private static HashMap<Integer, PointF> bioScore;
	
	/**
	 * return default screen size 480x640(width, height) if real size unknown
	 * @return
	 */
//	public static Xyz getXy() {
//		if(xy==null) return new Xyz(480, 640);
//		return xy;
//	}
//	public static void setXy(Xyz xy) {
//		FaceVisionUtils.xy = xy;
//	}
//
//	public static List<Float> getSmile() {
//		return smileList;
//	}
//	public static void setSmile(List<Float> smile) {
//		FaceVisionUtils.smileList = smile;
//	}
	
	/**
	 * add new smile value (s) and update a highest value
	 * @param s
	 */
	public static void addSmile(float s){
		if(FaceVisionUtils.smileList==null) {
			smileList = new ArrayList<Float>();
			startSmile = s;
		}
		smileList.add(s);
		if(topSmile<s) topSmile = s;
	}
	
	/**
	 * detetc is smile changing
	 * @param smilleStart
	 * @param smilleNow
	 * @return
	 */
	public static boolean isMakeSmile(){
		if(startSmile == 0f) return false;
		if(smileList.size()==0) return false;
//		float end = smileList.get(smileList.size()-1);
//		return isSmile(startSmile, topSmile);
		return(smileChange< Math.abs(startSmile-topSmile));
	}
	
	public static void createLandmark(int landmark, PointF value){
		if(FaceVisionUtils.bioScore == null) bioScore = new HashMap<Integer, PointF>();
		switch(landmark){
		case Landmark.LEFT_EYE:
			bioScore.put(Integer.valueOf(Landmark.LEFT_EYE), value); break;
		case Landmark.RIGHT_EYE:
			bioScore.put(Landmark.RIGHT_EYE, value); break;
		case Landmark.NOSE_BASE:
			bioScore.put(Landmark.NOSE_BASE, value); break;
		case Landmark.RIGHT_MOUTH:
			bioScore.put(Landmark.RIGHT_MOUTH, value); break;
		case Landmark.LEFT_MOUTH:
			bioScore.put(Landmark.LEFT_MOUTH, value); break;
		case Landmark.BOTTOM_MOUTH:
			bioScore.put(Landmark.BOTTOM_MOUTH, value); break;
		}
	}
	
	private static boolean isSmile(float s1, float s2){
		if(smileChange > Math.abs(s1-s2)) return false;
		return true;
	}
	
	public static boolean isPoseCorrect(float eulerY, float eulerZ){
		
		if(eulerZ<eulerZMax && eulerY<eulerYMax) return true;
		return false;
	}
	
	public static PointF getMouthCenter(PointF mouthLeftCorner, PointF mouthRightCorner, PointF mouthBottom){
		return new PointF((mouthLeftCorner.x+mouthRightCorner.x+mouthBottom.x)/3,(mouthLeftCorner.y+mouthRightCorner.y+mouthBottom.y)/3);
	}
	
	private static PointF getMouthCenter(HashMap<Integer, PointF> landmark){
		PointF mL, mR, mB;
		mL = landmark.get(Landmark.LEFT_MOUTH);
		mR = landmark.get(Landmark.RIGHT_MOUTH);
		mB = landmark.get(Landmark.BOTTOM_MOUTH);
		return new PointF((mL.x+mR.x+mB.x)/3,(mL.y+mR.y+mB.y)/3);
	}
	
	/**
	 * extract sensi classic  biometric as double[] from google Landmark map 
	 * @param land HashMap(Integer, PointF) key are Landmark type constants, value landmarks coordinates
	 * @return double[] 
	 * biometric result are normalized for distance between eyes = 1
	 */
	public static double[] extracSensiBio(HashMap<Integer, PointF> land){
		
		double unit=0;
		ArrayList<PointF> face = new ArrayList<PointF>();

		face.add(new PointF(land.get(EYE_L).x, land.get(EYE_L).y));// eye left
		face.add(new PointF(land.get(EYE_R).x, land.get(EYE_R).y));// eye right
		face.add(new PointF(land.get(NOSE).x,land.get(NOSE).y)); //nose
		face.add(getMouthCenter(land));//mouth
		
		List<Double> biometric = new ArrayList<Double>();
		
		for (int i = 0; i < face.size(); i++) {
			PointF p1 = face.get(i);
			for (int j = i + 1; j < face.size(); j++) {
				PointF p2 = face.get(j);
				double dist = Math.sqrt(Math.pow(p1.x - p2.x, 2)+ Math.pow(p1.y - p2.y, 2));
				Log.i("BIO", "point["+i+","+j+"]: "+dist);
				if (i == 0 && j == 1) {
					unit = dist;
				} else {
					biometric.add(dist / unit);
				}
			}
		}

		Log.d("EXTRACT BIO is {}", "" + biometric.size() + " " + biometric);
		double[] ds = new double[biometric.size()];
		for (int i = 0; i < ds.length; i++) {
			ds[i] = biometric.get(i);
		}
		
		return ds;
	}

}
