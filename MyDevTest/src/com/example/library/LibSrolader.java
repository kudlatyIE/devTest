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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.example.libraryloader.SensiLoader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class LibSrolader {
	
	private AssetManager asm;
	private Context context;
	private String result = "empty result";
	private String parent,jarPath;
	private SensiLoader loader;
	private String lib;
	private final String TAG_LIB="/lib/", TAG_LIBS="/libs";
	
	
	public LibSrolader(Context c){
		this.context=c;
		this.lib=getLibFolder();
//		this.path = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.parent = c.getApplicationInfo().dataDir.toString();
		this.asm = context.getAssets();
		getFiles(asm,parent);

	}
	
	private void getFiles(AssetManager a, String p){
		
		InputStream in=null;
		OutputStream out=null;

		Log.v("ASSETS", "parent folder: "+p);
		String temp="empty temp....\n";
		String subTemp="empty sub temp....\n";

		Log.d("ASSETS", "context.getFilesDir(): "+context.getFilesDir());
		
		File dir = new File(p+lib);
		if(!dir.exists()) dir.mkdir();
		dir = new File(p+"/app_cascade/");
		if(!dir.exists()) dir.mkdir();
		
		// http://stackoverflow.com/questions/6275765/android-how-to-detect-a-directory-in-the-assets-folder
		
		//get files from ASSET folder
		String asList="ASSET LIST: \n";
		boolean b=false;
		try{
			a.open("lbpcascade_frontalface.xml");
//			a.close();
			b=true;
		}catch(IOException e){
			Log.e("ASSETS", "fail to open asset file lbpcascade_frontalface.xml...."+ e.getMessage());
			e.printStackTrace();
		}
		Log.d("ASSETS", "IS FILE lbpcascade_frontalface.xml EXIST? "+b);
		try {
			String tempName="";
			String [] assetList = a.list("");
			
			if(assetList!=null){
				for(int i=0;i<assetList.length;i++){
					
					//get face XML
					if(isFaceFile(assetList[i])) {
						tempName=assetList[i];
						copyAssetFile(tempName,(p+"/app_cascade"));
					}
					
					//get Native SO
					if(isNativeFile(assetList[i])) {
						tempName=assetList[i];
						copyAssetFile(tempName,(p+lib));
					} 
					
					asList=asList+i+"::: asset list::: "+assetList[i]+"\n";
					Log.w("ASSETS", i+"::: asset list::: "+assetList[i]);
				}
			}
			Log.w("ASSETS", "asset list = "+(assetList!=null)+" size: "+assetList.length);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//get files from app data folder
		try{

			File [] fArray = new File(p).listFiles();

			if(fArray!=null){
				temp="";
				for(File f:fArray ){
					temp=temp+f.getAbsolutePath()+"\n";
					Log.v("ASSETS", "FILE subfolders: "+f.getName());
//					getFiles(a,p+"/"+f.getName());
					File [] fSubArray = new File(p+"/"+f.getName()).listFiles();
					if(fSubArray!=null){
						subTemp="";
						for(File ff:fSubArray){
							subTemp=subTemp+ff.getAbsolutePath()+"\n";
							Log.v("ASSETS", "SUB_FILE subfolders: "+ff.getAbsolutePath());
						}
					}
					temp=temp+subTemp;
				}
			}
			Log.v("ASSETS", "FILE array size: "+fArray.length);

		}catch(Exception e){
			e.printStackTrace();
		}
		temp=temp+"\n"+asList;

		this.result=temp;
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
//			asm.close();
			in.close();
			out.flush();
			out.close();
		}catch(Exception e){
//			e.printStackTrace();
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
	
	private String getLibFolder(){
		int current = android.os.Build.VERSION.SDK_INT;
		Log.d("ASSETS", "droid OS: "+current);
		if(current >= android.os.Build.VERSION_CODES.LOLLIPOP) return TAG_LIB;
		else return TAG_LIBS;
	}
	
		

}
