package com.example.library;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class LibSrolader {

	private AssetManager asm;
	private Context context;
	private File dir;
	private String parent;
//	private File cacheDir;
	private String lib, pathNative, pathFaceLib, pathCache;;
	private final String TAG_LIB="/lib/", TAG_LIBS="/libs/", TAB_CASCADE="/app_cascade/", TAG_NATIVE="native", TAG_FACE="face";
	private boolean isDataLibNative=true, isDataLibFace=true, isAssetsLibsNative=true, isAssetsLibsFace=true;
	
	private String result;
	
	
	@SuppressLint("NewApi")
	public LibSrolader(Context c) throws Exception{
		
		this.context=c;
		this.lib=getLibFolder();
		this.parent = c.getApplicationInfo().dataDir.toString();
//		this.cacheDir=c.getCodeCacheDir();
		this.pathNative=c.getApplicationInfo().nativeLibraryDir;
//		this.pathCache=c.getCacheDir().getAbsolutePath();
		this.pathCache=parent+getLibFolder();
		
		this.pathFaceLib=parent+TAB_CASCADE;
		this.asm = context.getAssets();
		
		Log.e("NATIVE","NATIVE LIB DIR: "+pathNative );
		Log.e("NATIVE","CODE NATIVE LIB DIR: "+pathNative );

		result = getSecret(pathNative);
		Log.e("NATIVE","FULL RESULT: \n"+result );
		Log.e("CACHE","CACHE DIR: "+pathCache );
		
		if(!checkLibrary(asm, pathFaceLib, TAG_FACE)) {
			this.isDataLibFace=false;
			//try to copy libs from assets
			copyLibrary(asm,TAG_FACE);
		}
		//check if copy success
		if(!checkLibrary(asm, pathFaceLib, TAG_FACE)) this.isAssetsLibsFace=false;
		
		//FOR NATIVE CHECK REAL /lib/arm/ FOLDER !!!!!!
		
		if(!checkLibrary(asm, pathNative, TAG_NATIVE)) {
			this.isDataLibNative=false;
			//try to copy libs from assets
			copyLibrary(asm,TAG_NATIVE);
		}
		//check if success
		if(!checkLibrary(asm, pathNative, TAG_NATIVE)) this.isAssetsLibsNative=false;
		
		if(false==this.isDataLibFace) {
			Log.e("LIB_LOADER", "Missing face detect library???");
		}
		if(false==this.isDataLibNative) {
			Log.e("LIB_LOADER", "Missing native library!???");
		}

	}
	

	private boolean checkLibrary(AssetManager a,String p, String tag){
		boolean found = true;
		String temp="";
		List<String> arr=new ArrayList<String>(); 
		
		switch(tag){
		case TAG_NATIVE: 
			arr= Arrays.asList(nativeList()); 
//			p=p+lib;
			p=pathNative;
			break;
		case TAG_FACE: 	arr= Arrays.asList(faceLibsList()); break;
		}
		
		try{
			Log.w("ASSETS", "CHECKER path: "+p);
			File [] fArray = new File(p).listFiles();
			if(fArray!=null){
				for(File f:fArray ){
					temp=temp+f.getAbsolutePath()+"\n";
					Log.v("ASSETS", "FILE in: "+p+"::"+f.getName());
					if(arr.contains(f.getName())) Log.i("ASSETS", "FOUND: "+f.getName());
					else {
						Log.w("ASSETS", tag+"MISSING: "+f.getName());
						found = false;
					}
				}
				Log.w("ASSETS",tag+ "array size: "+fArray.length);
			}else found = false;
			Log.v("ASSETS", tag+"array is NULL: "+(fArray==null));

		}catch(Exception e){
			e.printStackTrace();
			found = false;
		}
		this.result=temp;
		return found;
	}
	
	private boolean checkCache(String p, String file){
		boolean b=false;
		String temp="";
		File[] fArray = new File(p).listFiles();
		Log.d("CACHE", "looking inside: "+p);
		
		if(fArray!=null){
			Log.d("CACHE", "cache folder size: : "+fArray.length);
			for(File f:fArray){
				temp=temp+f.getName()+"\n";
				if(f.getName().equals(file)) {
					Log.d("TEMP_LIB", "FOUND ZIP IN: "+f.getAbsolutePath());
					b=true;
				}
				File[] ffArray = new File(f.getAbsolutePath()).listFiles();
				if(ffArray!=null){
					for(File ff:ffArray){
						temp=temp+ff.getAbsolutePath()+"\n";
						if(ff.getName().equals(file)) {
							Log.d("TEMP_LIB", "FOUND ZIP IN: "+ff.getAbsolutePath());
							b=true;
						}
					}
				}
			}
		}
		Log.d("TEMP_LIB", "cache folder content: "+temp);
		return b;
	}
	private void copyLibrary(AssetManager a, String tag) throws Exception{
		String path=parent;
		boolean checker=false;

		Log.v("ASSETS", "parent folder: "+path);
		String temp="empty temp....\n";
		String subTemp="empty sub temp....\n";

		String asList="ASSET LIST: \n";

		try {
			String [] assetList = a.list("");
			
			if(assetList!=null){
				
				switch(tag){
				case TAG_NATIVE:
					path=pathCache;
//					dir = new File(path);
//					if(!dir.exists()) dir.mkdir();
//					path = secretPath;
//					for(int i=0;i<assetList.length;i++){
//						if(isNativeFile(assetList[i])) copyAssetFile(assetList[i],(path));
//						
//						asList=asList+i+"::: asset SO list::: "+assetList[i]+"\n";
//						Log.v("ASSETS", i+": native SO list: "+assetList[i]);
//					}
					for(int i=0;i<assetList.length;i++){
						if(isZipFile(assetList[i])) {
							copyAssetFile(assetList[i],(path));
							checker=true;
							Log.d("ZIP", "ZIP file has been copied to: "+path );
							asList=asList+i+"::: asset ZIP list::: "+assetList[i]+"\n";
							Log.v("ASSETS", i+": native ZIP list: "+assetList[i]);
						}
						
					}
					
//					checkLibrary(asm, pathCache, TAG_NATIVE);
					checker = checkCache(path,"native_kurwa.zip");
					
					if(checker) UnzipLib.extracLib(path+"native_kurwa.zip", pathNative);
					else Log.e("ASSET", "native ZIP file NOT FOUND!!!");
					
					Log.d("NATIVE", "check nativeDir after UNZIP: "+checkLibrary(asm, pathNative, TAG_NATIVE));
					
					//TODO: here - copy from native TEMP lib folder to real: /lib/arm!!!!
					break;
				case TAG_FACE:
					path=pathFaceLib;
					dir = new File(path);
					if(!dir.exists()) dir.mkdir();
					for(int i=0;i<assetList.length;i++){
						if(isFaceFile(assetList[i])) copyAssetFile(assetList[i],(path));
						
						asList=asList+i+"::: face XML list::: "+assetList[i]+"\n";
						Log.v("ASSETS", i+": face XML list: "+assetList[i]);
					}
					break;
				}

			}
			Log.w("ASSETS", "asset list = "+(assetList!=null)+" size: "+assetList.length);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//get files and subfolders from app data folder
		try{

			File [] fArray = new File(path).listFiles();

			if(fArray!=null){
				temp="";
				for(File f:fArray ){
					temp=temp+f.getAbsolutePath()+"\n";
					Log.v("ASSETS", "FILE: "+f.getName());
					File [] fSubArray = new File(path+"/"+f.getName()).listFiles();
					if(fSubArray!=null){
						subTemp="";
						for(File ff:fSubArray){
							subTemp=subTemp+ff.getAbsolutePath()+"\n";
							Log.v("ASSETS", "SUB_FILE: "+ff.getAbsolutePath());
						}
					}
				}
			}else{
				Log.e("AFTER_COPY","No kurwa: "+path);
			}
			Log.w("ASSETS", "FILE array size: "+fArray.length);

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private void copyAssetFile(String fileName, String p){
		
		asm = context.getAssets();
		InputStream in=null;
		OutputStream out=null;
		
		try{
			in = asm.open(fileName);
			out = new BufferedOutputStream(new FileOutputStream(p+fileName));
			
			byte[] buffer = new byte[1024];
			int read;
			while((read=in.read(buffer))!= -1){
				out.write(buffer, 0, read);
			}
			in.close();
			out.flush();
			out.close();
		}catch(Exception e){
			Log.e("ASSETS", "DOH! "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * return list of files in nativeDIR
	 * @param p
	 * @return
	 */
	private String getSecret(String p){
		String sring="";
		
		File[] ar = new File(p).listFiles();
		if(ar!=null){
			for(File f:ar){
				Log.e("SECRET", "files: "+f.getAbsolutePath());
				sring = sring + f.getAbsolutePath()+"\n";
				File[] arr = new File(f.getAbsolutePath()).listFiles();
				if(arr!=null){
					for(File ff:arr){
						Log.e("SECRET", "subFiles: "+ff.getAbsolutePath());
					}
				}//else Log.w("SECRET","secret SUB_DIR is: "+arr.length);
			}
		}else {
			ar = new File(new File(p).getParent()).listFiles();
			for(File f:ar){
				Log.e("SECRET", "parent files: "+f.getAbsolutePath());
				sring = sring + f.getAbsolutePath()+"\n";
			}
			Log.e("SECRET","secret DIR size is: "+ar.length);
		}
		
		return sring;
	}

	private boolean isNativeFile(String s){
		return s.matches(".*?\\.so");
	}
	
	private boolean isFaceFile(String s){
		
		return s.matches(".*?\\.xml");
	}
	private boolean isZipFile(String s){
		
		return s.matches(".*?\\.zip");
	}
	
	
	private String getLibFolder(){
		int current = android.os.Build.VERSION.SDK_INT;
		Log.d("ASSETS", "droid OS: "+current);
		if(current >= android.os.Build.VERSION_CODES.LOLLIPOP) return TAG_LIB;
		else return TAG_LIBS;
	}
	
	public String getAssetList(){
		return result;
	}


	private String[] nativeList(){
		
		String[] myList={"libopencv_java.so", 
						"libnative_camera_r2.2.0.so", 
						"libnative_camera_r3.0.1.so",
						"libnative_camera_r4.0.0.so",
						"libnative_camera_r4.1.1.so",
						"libnative_camera_r4.0.3.so",
						"libnative_camera_r2.3.3.so",
						"libnative_camera_r4.2.0.so",
						"libdetection_based_tracker.so",
						"libnative_camera_r4.3.0.so",
						"libnative_camera_r4.4.0.so" 
						};
		return myList;	
	}
	
	private String[] faceLibsList(){
		
		String[] myList={"lbpcascade_frontalface.xml",
						"left_eye.xml",
						"haarcascade_eye_tree_eyeglasses.xml",
						"haarcascade_eye.xml",
						"haarcascade_mcs_righteye.xml",
						"right_eye.xml",
						"haarcascade_mcs_nose.xml",
						"nose.xml",
						"mouth.xml",
						"nose_new.xml",
						"haarcascade_mcs_mouth.xml",
						"haarcascade_smile.xml"
						};
		
		return myList;
	}

}
