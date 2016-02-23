package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;

public class BitmapUtils {
	
	private static float ratioX, ratioY;
	private static int picH, picW, screenH, screenW, newH, newW;
	private static Bitmap newMap;
	
	public static Bitmap resizeBitmapForBiometric(Activity a, Bitmap b){
		
		Display display = a.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
	
		screenH= Math.round(size.y);
		screenW= Math.round(size.x);
		picH=b.getHeight();
		picW=b.getWidth();
		ratioX=(float)screenW / picW;
		ratioY=(float)screenH / picH;
		newH= Math.round(picH*ratioX);
		newW= Math.round(picW*ratioX);

		Log.i("DIMENS SCREEN", "scr_Width: "+screenW+" scr_Height: "+screenH);
		Log.i("RATIO SCR::PIC", "ratio_X: "+ratioX+" ratio_Y: "+ratioY);
		Log.i("OLD PIC", "pic_Width: "+picW+" pic_Height: "+picH);
		if(screenW==picW) {
			newMap = b;
			Log.d("G_UTILS", "BITMAP: NO RESIZE NEDD");
		}
		else{
			newMap = Bitmap.createScaledBitmap(b,screenW,(int)newH,false);
			Log.d("G_UTILS", "BITMAP: LET'S RESIZE IT");
		}
		return newMap;
	}
	
	public static Bitmap resizeBitmapForGesture(Activity a, Bitmap b){
		
		Display display = a.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
	
		screenH= Math.round(size.y);
		screenW= Math.round(size.x);
		picH=b.getHeight();
		picW=b.getWidth();
		ratioX=(float)screenW / picW;
		ratioY=(float)screenH / picH;
		newH= Math.round(picH*ratioX);
		newW= Math.round(picW*ratioX);

		Log.i("DIMENS SCREEN", "scr_Width: "+screenW+" scr_Height: "+screenH);
		Log.i("RATIO SCR::PIC", "ratio_X: "+ratioX+" ratio_Y: "+ratioY);
		Log.i("OLD PIC", "pic_Width: "+picW+" pic_Height: "+picH);
		if(screenW==picW) {
			newMap = b;
			Log.d("G_UTILS", "BITMAP: NO RESIZE NEDD");
		}
		else{
			newMap = Bitmap.createScaledBitmap(b,screenW,(int)newH,false);
			Log.d("G_UTILS", "BITMAP: LET'S RESIZE IT");
		}
		newMap = overlay(newMap,screenW, screenH);
		Log.i("NEW PIC", "newPic_Width: "+newMap.getWidth()+ " newPic_Height: "+newMap.getHeight());
		return newMap;
	}

	public static Bitmap overlay(Bitmap b, int x, int y){
		Bitmap empty = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_4444);
		Canvas cv = new Canvas(empty);
		cv.drawBitmap(b, 0, 0, null);
		cv.save();
		return empty;
	}

}
