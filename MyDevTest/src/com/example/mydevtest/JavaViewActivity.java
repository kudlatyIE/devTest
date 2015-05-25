package com.example.mydevtest;

import com.example.utils.JavaViewCreator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JavaViewActivity extends Activity {
	private TextView tv,tvHint, tvRepeat;
	private ImageView ivPhoto;
	private EditText etUserName;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JavaViewCreator view = new JavaViewCreator(this);
//		LinearLayout myLayout = view.getFdLayout();
		RelativeLayout myLayout = view.getResultLayout();
		setContentView(myLayout);
		
//		tv = myLayout.get
		System.out.println("layout childern: "+myLayout.getChildCount());
		for(int i=0;i<myLayout.getChildCount();i++){
			System.out.println("kid["+i+"]: "+ myLayout.getChildAt(i).getId());
		}
		
		tvHint = (TextView) myLayout.getChildAt(0);
		etUserName = (EditText) myLayout.getChildAt(1);
		btnSave = (Button) myLayout.getChildAt(2);
		
		
//		ivPhoto = (ImageView) myLayout.getChildAt(0);
//		ivPhoto.setBackgroundResource(R.drawable.cat_purple_big);;
//		tvRepeat = (TextView) myLayout.getChildAt(2);
//		tvRepeat.setText("OVER PIC!");
//		tv = (TextView) myLayout.getChildAt(0);
//		tv.setText("IT'S a: "+tv.getId());
		
		btnSave.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String msg = etUserName.getText().toString();
				tvHint.setText(msg);
			}
			
		});
		
	}
	
	
}
