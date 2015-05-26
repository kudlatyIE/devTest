package com.example.mydevtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import com.example.utils.MySd;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SDActivity extends Activity {

	private TextView tvSd;
	private static final Pattern DIR_SEPORATOR = Pattern.compile("/");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sd);
		
		tvSd = (TextView) findViewById(R.id.text_sd);
//		tvSd.setText(MySd.getSd());
		tvSd.setText(getExternalStorage());
		
		MySd.getListMnt();
		getStorageDirectories();
		
		//------------------------------------------------------
		
		
	}
	
	static String getExternalStorage(){
        String exts =  Environment.getExternalStorageDirectory().getPath();
        try {
           FileReader fr = new FileReader(new File("/proc/mounts"));       
           BufferedReader br = new BufferedReader(fr);
           String sdCard=null;
           String line;
           while((line = br.readLine())!=null){
               if(line.contains("secure") || line.contains("asec")) continue;
           if(line.contains("fat")){
               String[] pars = line.split("\\s");
               if(pars.length<2) continue;
               if(pars[1].equals(exts)) continue;
               sdCard =pars[1]; 
               break;
           }
           }
           fr.close();
           br.close();
           return sdCard;  

        } catch (Exception e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        return null;
	}
	//-----------------------------------------------------
		public static String[] getStorageDirectories() {
		    // Final set of paths
		    final Set<String> rv = new HashSet<String>();
		    // Primary physical SD-CARD (not emulated)
		    final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
		    // All Secondary SD-CARDs (all exclude primary) separated by ":"
		    final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
		    // Primary emulated SD-CARD
		    final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
		    if(TextUtils.isEmpty(rawEmulatedStorageTarget))
		    {
		        // Device has physical external storage; use plain paths.
		        if(TextUtils.isEmpty(rawExternalStorage))
		        {
		            // EXTERNAL_STORAGE undefined; falling back to default.
		            rv.add("/storage/sdcard1");
		        }
		        else
		        {
		            rv.add(rawExternalStorage);
		        }
		    }
		    else
		    {
		        // Device has emulated storage; external storage paths should have
		        // userId burned into them.
		        final String rawUserId;
		        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
		        {
		            rawUserId = "";
		        }
		        else
		        {
		            final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		            final String[] folders = DIR_SEPORATOR.split(path);
		            final String lastFolder = folders[folders.length - 1];
		            boolean isDigit = false;
		            try
		            {
		                Integer.valueOf(lastFolder);
		                isDigit = true;
		            }
		            catch(NumberFormatException ignored)
		            {
		            }
		            rawUserId = isDigit ? lastFolder : "";
		        }
		        // /storage/emulated/0[1,2,...]
		        if(TextUtils.isEmpty(rawUserId))
		        {
		            rv.add(rawEmulatedStorageTarget);
		        }
		        else
		        {
		            rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
		        }
		    }
		    // Add all secondary storages
		    if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
		    {
		        // All Secondary SD-CARDs splited into array
		        final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
		        
		        //-------------print all-----
		        for(String s:rawSecondaryStorages){
		        	System.out.println("sub: "+s);
		        }
		        
		        Collections.addAll(rv, rawSecondaryStorages);
		    }
		    return rv.toArray(new String[rv.size()]);
		}
		//------------------------------------------------------
		public static HashSet<String> getExternalMounts() {
		    final HashSet<String> out = new HashSet<String>();
		    String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
		    String s = "";
		    try {
		        final Process process = new ProcessBuilder().command("mount")
		                .redirectErrorStream(true).start();
		        process.waitFor();
		        final InputStream is = process.getInputStream();
		        final byte[] buffer = new byte[1024];
		        while (is.read(buffer) != -1) {
		            s = s + new String(buffer);
		        }
		        is.close();
		    } catch (final Exception e) {
		        e.printStackTrace();
		    }

		    // parse output
		    final String[] lines = s.split("\n");
		    for (String line : lines) {
		        if (!line.toLowerCase(Locale.US).contains("asec")) {
		            if (line.matches(reg)) {
		                String[] parts = line.split(" ");
		                for (String part : parts) {
		                    if (part.startsWith("/"))
		                        if (!part.toLowerCase(Locale.US).contains("vold"))
		                            out.add(part);
		                }
		            }
		        }
		    }
		    return out;
		}
}
