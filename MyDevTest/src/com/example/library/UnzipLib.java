package com.example.library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.util.Log;

public class UnzipLib {
	
	/**
	 * extract ZIP file into extractFolder: for native library .so it's a "/lib/ folder inside app data folder.
	 * @param zipFile
	 * @param extractFolder
	 * @throws Exception 
	 */
	public static void extracLib(String zipFile, String extractFolder) throws Exception{
		BufferedOutputStream dest = null;
		BufferedInputStream input = null;
		try{
			int BUFFER = 2048;
			File file = new File(zipFile);
			ZipFile zip = new ZipFile(file);
			String newPath = extractFolder;
			
			File dir = new File(newPath);
			if(!dir.exists()) dir.mkdir();
			
			Enumeration zipFileEntries = zip.entries();
			
			while(zipFileEntries.hasMoreElements()){
				
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				Log.d("UNZIP_LIB", "in ZIP found file: "+currentEntry);
				
				File destFile = new File(newPath,currentEntry);
				
				if(!entry.isDirectory()){
					input = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					 byte[] data = new byte[BUFFER];
					 
					 //write file from zip to file in dest folder
					 FileOutputStream out = new FileOutputStream(destFile);
					 dest = new BufferedOutputStream(out, BUFFER);
					 
					 //read and write until last byte is encountered
					 while((currentByte = input.read(data,0,BUFFER)) != -1) dest.write(data, 0, currentByte);
		
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("UNZIP library error: "+e.getMessage());
		}
		finally{
			
			try {
				dest.flush();
				dest.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
