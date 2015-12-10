package com.example.library;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.example.libraryloader.SensiLoader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class LibSroladerOld {
	
	private AssetManager asm;
	private Context context;
	private File dir;
	private String result = "empty result";
	private String parent,secretPath;
	private String lib, pathNative, pathFaceLib;
	private final String TAG_LIB="/lib/", TAG_LIBS="/libs/", TAB_CASCADE="/app_cascade/", TAG_NATIVE="native", TAG_FACE="face";
	
	
	public LibSroladerOld(Context c) throws Exception{
		this.context=c;
		this.lib=getLibFolder();
		
//		this.path = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.parent = c.getApplicationInfo().dataDir.toString();
		this.pathNative=parent+lib;
		this.pathFaceLib=parent+TAB_CASCADE;
		this.asm = context.getAssets();
		if(!checkLibrary(pathFaceLib, TAG_FACE)) getFiles(asm,parent, TAG_FACE);
		if(!checkLibrary(pathNative, TAG_NATIVE)) getFiles(asm,parent, TAG_NATIVE);
		
		 
		secretPath= c.getApplicationInfo().nativeLibraryDir;
		result = getSecret(secretPath);
		Log.v("NATIVES",result );

	}
	
	private void getFiles(AssetManager a, String parent, String destination) throws Exception{

		String path=parent;

		Log.v("ASSETS", "parent folder: "+path);
		String temp="empty temp....\n";
		String subTemp="empty sub temp....\n";

		Log.d("ASSETS", "context.getFilesDir(): "+context.getFilesDir());

		// http://stackoverflow.com/questions/6275765/android-how-to-detect-a-directory-in-the-assets-folder
		
		//get files from ASSET folder
		String asList="ASSET LIST: \n";

		try {
			String [] assetList = a.list("");
			
			if(assetList!=null){
				
				switch(destination){
				case TAG_NATIVE:
					path=pathNative;
					dir = new File(path);
					if(!dir.exists()) dir.mkdir();
					
//					for(int i=0;i<assetList.length;i++){
//						if(isNativeFile(assetList[i])) copyAssetFile(assetList[i],(path));
//						
//						asList=asList+i+"::: asset list::: "+assetList[i]+"\n";
//						Log.v("ASSETS", i+": native list: "+assetList[i]);
//					}
					
					for(int i=0;i<assetList.length;i++){
						if(isZipFile(assetList[i])) copyAssetFile(assetList[i],(path));
						
						asList=asList+i+"::: asset list::: "+assetList[i]+"\n";
						Log.v("ASSETS", i+": native list: "+assetList[i]);
					}
					
					UnzipLib.extracLib(path+"native_lib.zip", path);
					break;
					
				case TAG_FACE:
					path=pathFaceLib;
					dir = new File(path);
					if(!dir.exists()) dir.mkdir();
					for(int i=0;i<assetList.length;i++){
						if(isFaceFile(assetList[i])) copyAssetFile(assetList[i],(path));
						
						asList=asList+i+"::: face list::: "+assetList[i]+"\n";
						Log.v("ASSETS", i+": face list: "+assetList[i]);
					}
					break;
				}
				
					
//				for(int i=0;i<assetList.length;i++){
//					
//					//get face XML
//					if(isFaceFile(assetList[i])) {
//						tempName=assetList[i];
//						copyAssetFile(tempName,(pathFaceLib));
//					}
//					
//					//get Native SO
//					if(isNativeFile(assetList[i])) {
//						tempName=assetList[i];
//						copyAssetFile(tempName,(pathNative));
//					} 
//					
//					asList=asList+i+"::: asset list::: "+assetList[i]+"\n";
//					Log.v("ASSETS", i+"::: asset list::: "+assetList[i]);
//				}
			}
			Log.w("ASSETS", "asset list = "+(assetList!=null)+" size: "+assetList.length);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
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
//					getFiles(a,p+"/"+f.getName());
					File [] fSubArray = new File(path+"/"+f.getName()).listFiles();
					if(fSubArray!=null){
						subTemp="";
						for(File ff:fSubArray){
							subTemp=subTemp+ff.getAbsolutePath()+"\n";
							Log.v("ASSETS", "SUB_FILE: "+ff.getAbsolutePath());
						}
					}
					temp=temp+subTemp;
				}
			}
			Log.w("ASSETS", "FILE array size: "+fArray.length);

		}catch(Exception e){
			e.printStackTrace();
		}
//		temp=temp+"\n"+asList;

		this.result=temp;
	}
	
	public boolean checkLibrary(String p, String tag){
		boolean found = true;
		String temp="";
		List<String> arr=new ArrayList<String>(); 
		
		switch(tag){
		case TAG_NATIVE:
			arr= Arrays.asList(nativeList());
			break;
		case TAG_FACE:
			arr= Arrays.asList(faceLibsList());
			break;
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
						Log.w("ASSETS", "MISSING: "+f.getName());
						found = false;
					}
				}
				Log.w("ASSETS", "CHECKER array size: "+fArray.length);
			}else found = false;
			Log.v("ASSETS", "CHECKER array is NULL: "+(fArray==null));

		}catch(Exception e){
			e.printStackTrace();
			found = false;
		}
		return found;
	}
	
	private String getSecret(String p){
		String sring="";
		
		File[] ar = new File(p).listFiles();
		if(ar!=null){
			for(File f:ar){
				Log.w("SECRET", "files: "+f.getAbsolutePath());
				sring = sring + f.getAbsolutePath();
				File[] arr = new File(f.getAbsolutePath()).listFiles();
				if(arr!=null){
					for(File ff:arr){
						Log.w("SECRET", "subFiles: "+ff.getAbsolutePath());
					}
				}else Log.w("SECRET","secret SUB_DIR is: "+arr.length);
			}
		}else {
			ar = new File(new File(p).getParent()).listFiles();
			for(File f:ar){
				Log.w("SECRET", "parent files: "+f.getAbsolutePath());
				sring = sring + f.getAbsolutePath();
			}
			Log.w("SECRET","secret DIR is: "+ar.length);
		}
		
		return sring;
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
			Log.i("ASSETS", "COPY: "+fileName+" TO: "+p);
		}catch(Exception e){
			Log.e("ASSETS", "DOH! "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public String getAssetList(){
		return result;
	}
	
	public String checkJar(){
		String s="doopa blada";
//		String temp;
		try {
//			temp = getJarPath(SensiLoader.class);
			JarFile jf = new JarFile("cascade_lib_temp.jar");
			Log.v("ASSET", "JAR number of ZipEntries: "+jf.size());
			JarEntry je = jf.getJarEntry("assets/left_eye.xml");
			if(je!=null) s="found: "+je.getName();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public String getJarPath(Class cl) throws Exception{
		String kootas="kootas default";
		CodeSource cs = cl.getProtectionDomain().getCodeSource();
		File jarFile;
		
		if(cs.getLocation()!=null){
			jarFile = new File(cs.getLocation().toURI());
		}else{
			Log.e("ASSETS", "class loader location is NULL");
			String str = cl.getResource(cl.getSimpleName()+".class").getPath();
			String jarFilePath = str.substring(str.indexOf(":"+1), str.indexOf("!"));
			jarFilePath = URLDecoder.decode(jarFilePath,"UTF-8");
			jarFile = new File(jarFilePath);
		}
		kootas = jarFile.getParentFile().getAbsolutePath();
		return kootas;
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


