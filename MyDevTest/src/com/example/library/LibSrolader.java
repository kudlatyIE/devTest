package com.example.library;

import java.io.File;
import java.io.IOException;
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
	private String path,jarPath;
	private SensiLoader loader;
	
	public LibSrolader(Context c){
		this.context=c;
//		this.path = Environment.getExternalStorageDirectory().getAbsolutePath();
		this.path = c.getApplicationInfo().dataDir.toString();
		this.asm = context.getAssets();
		getFiles(asm,path);
//		Log.v("ASSET","JAR CHECK: "+checkJar());
	}
	
	private void getFiles(AssetManager a, String p){

		Log.v("ASSETS", "parent folder: "+p);
		String temp="empty temp....\n";
		String subTemp="empty sub temp....\n";
//		try{
//			String[] list = a.list(p);
//			
//			if(list!=null){
//				for(String s:list){
//					temp=temp+p+"/"+s+"\n";
//					Log.v("ASSETS",p+"/"+s );
//					getFiles(a,p+"/"+s);
//				}
//			}
//			else{
//				Log.v("ASSETS","EMPTY LIST of: "+p);
//			}
//		}catch(Exception e){
//			Log.e("ASSETS", "cant't list: "+p);
//			e.printStackTrace();
//		}
		
		// http://stackoverflow.com/questions/6275765/android-how-to-detect-a-directory-in-the-assets-folder
		String asList="ASSET LIST: \n";
		try {
			
			String [] assetList = a.list("");
			if(assetList!=null){
				for(int i=1;i<assetList.length+1;i++){
					asList=asList+i+"::: asset list::: "+assetList[i-1]+"\n";
					Log.w("ASSETS", i+"::: asset list::: "+assetList[i-1]);
				}
			}
			Log.w("ASSETS", "asset list = "+(assetList!=null)+" size: "+assetList.length);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
//			File[] fArray = Environment.getExternalStorageDirectory().listFiles();
//			String str = context.getApplicationInfo().dataDir.toString();
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
//				else{
				Log.v("ASSETS", "FILE array size: "+fArray.length);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		temp=temp+"\n"+asList;
//		temp=temp+"\n"+checkJar();
		this.result=temp;
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
	
		

}
