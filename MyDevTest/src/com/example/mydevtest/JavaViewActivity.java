package com.example.mydevtest;

import com.example.utils.JavaViewCreator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JavaViewActivity extends Activity {
	private TextView tv,tvHint, tvRepeat;
	private ImageView ivPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JavaViewCreator view = new JavaViewCreator(this);
//		LinearLayout myLayout = view.getFdLayout();
		RelativeLayout myLayout = view.getGestureLayout();
		setContentView(myLayout);
		
//		tv = myLayout.get
		System.out.println("layout childern: "+myLayout.getChildCount());
		for(int i=0;i<myLayout.getChildCount();i++){
			System.out.println("kid["+i+"]: "+ myLayout.getChildAt(i).getId());
		}
		ivPhoto = (ImageView) myLayout.getChildAt(0);
		ivPhoto.setBackgroundResource(R.drawable.cat_purple_big);;
		tvRepeat = (TextView) myLayout.getChildAt(2);
		tvRepeat.setText("OVER PIC!");
//		tv = (TextView) myLayout.getChildAt(0);
//		tv.setText("IT'S a: "+tv.getId());
		
	}
}
