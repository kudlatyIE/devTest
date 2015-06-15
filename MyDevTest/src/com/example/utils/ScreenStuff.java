package com.example.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class ScreenStuff {
	
	private static Resources res;
	private static int  scrH, scrW;
	

	public static String getScreenSize(Activity ac){
		Display display = ac.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		scrH = size.y;
		scrW = size.x;
		return "H: "+scrH+" | W: "+scrW;
	}
	
	public static int getSystemBarHeight2(Context c){
		res = c.getResources();
		int resId = res.getIdentifier("navigation_bar_height", "dimen", "android");
		if(resId>0) return res.getDimensionPixelSize(resId);
		return 0;
		
	}
	
	public static int getSystemBarHeight(Context c){
		int h=0;
		boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if(!hasMenuKey && !hasBackKey){
			res = c.getResources();
			int orient = res.getConfiguration().orientation;
			int resId;
			if(isTab(c)){
				resId = res.getIdentifier(orient==Configuration.ORIENTATION_PORTRAIT ? 
						"navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
			}else{
				resId = res.getIdentifier(orient==Configuration.ORIENTATION_PORTRAIT ? 
						"navigation_bar_height" : "navigation_bar_width", "dimen", "android");
			}
			if(resId>0) return res.getDimensionPixelSize(resId);
		}
		
		return h;
	}
	
	private static boolean isTab(Context c){
		return (c.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)>=Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	
}
