package com.example.utils;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class MySd {
	
	public static String getSd(){
		
		boolean result=false;
		String myPath="N/A";
		final String TAG="SD";
		final String state = Environment.getExternalStorageState();

		if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...           
		    //Retrieve the primary External Storage:
		    final File primaryExternalStorage = Environment.getExternalStorageDirectory();

		    //Retrieve the External Storages root directory:
		    final String externalStorageRootDir;
		    if ( (externalStorageRootDir = primaryExternalStorage.getParent()) == null ) {  // no parent...
		        Log.d(TAG, "External Storage: " + primaryExternalStorage + "\n");
		        myPath=primaryExternalStorage.getAbsolutePath();
		    }
		    else {
		    	myPath="path";
		        final File externalStorageRoot = new File( externalStorageRootDir );
		        final File[] files = externalStorageRoot.listFiles();

		        for ( final File file : files ) {
		            if ( file.isDirectory() && file.canRead() && (file.listFiles().length > 0) ) {  // it is a real directory (not a USB drive)...
		                Log.d(TAG, "External Storage: " + file.getAbsolutePath() + "\n");
		                myPath=myPath.concat("\n"+file.getAbsolutePath());
		            }
		        }
		    }
		}return myPath;
	}
	
	public static void getListMnt(){
		String[] dirList = null;
		File storageDir = new File("/mnt/");
		if(storageDir.isDirectory()){
		    dirList = storageDir.list();
		    //TODO some type of selecton method?
		}
		try{
			for(String s:dirList){
				System.out.println("path: "+s);
			}
		}catch(Exception ex){
			System.out.println("there is no subfolders");
		}
	}

}
