package com.example.mydevtest;

import com.example.library.LibSrolader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NativeLibActivity extends Activity {
	
	private TextView tvLib;
	
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_native_lib);
		
		tvLib = (TextView) findViewById(R.id.native_text_list);
		String result;
		
		LibSrolader sro;
		try {
			sro = new LibSrolader(this);
			result = sro.getAssetList();
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		tvLib.setText(result);
		
	}
}
