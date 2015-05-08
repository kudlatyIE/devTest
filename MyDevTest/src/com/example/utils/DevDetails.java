package com.example.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DevDetails {
	
	public static String getDeviceName() {
		   String manufacturer = Build.MANUFACTURER;
		   String model = Build.MODEL;
		      return "manufacturer: "+(manufacturer) + 
		    		  "\nmodel: " + model+"\nproduct: "+Build.PRODUCT+"\nbrand: "+Build.BRAND+"\ndevice: "+
				   Build.DEVICE+"\ndisplay "+Build.DISPLAY+"\ntags: "+Build.TAGS+"\nhardware: "+Build.HARDWARE+
				   "\nboard: "+Build.BOARD+"\nuser: "+Build.USER;
		}

	public static String getHardware() {
		   String hw= Build.HARDWARE;
		   return hw==null ?"":hw;
		}
	
	public  static String getDensity(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
		display.getMetrics(dm);
		
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int dens = dm.densityDpi;
		
		return "height: "+height+"\nwidth: "+width+"\ndensity: "+dens;
	}

}
