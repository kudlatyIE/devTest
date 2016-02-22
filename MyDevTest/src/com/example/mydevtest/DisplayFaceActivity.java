package com.example.mydevtest;

import com.example.visionface.FaceLandmarker;
import com.example.visionface.FaceVisionUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayFaceActivity extends Activity {
	
	private final static String TAG=DisplayFaceActivity.class.getSimpleName();
	private TextView tvInfo;
	private ImageView img;
	private Bitmap face;
	private String bioInfo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_display_face);
		tvInfo = (TextView) findViewById(R.id.displayface_info_txt);
		tvInfo.setVisibility(View.GONE);
		img = (ImageView) findViewById(R.id.displayface_img);
		
		Bitmap b = FaceVisionUtils.getBtm();
		if(b!=null) img.setImageBitmap(b);
		
//		if(byteFace!=null){
//			
//			Bitmap temp = BitmapFactory.decodeByteArray(byteFace, 0, byteFace.length);
//			face = temp.copy(Bitmap.Config.ARGB_8888, true);
//			img.setImageBitmap(rotatedImg(face,90));
////			tvInfo.setText("biometric will be here...\nbyte[] size: "+byteFace.length);
//		}
//		else tvInfo.setText("Doopa!\nbyte[] is NULL: "+(byteFace==null));
	}
	
	
}
