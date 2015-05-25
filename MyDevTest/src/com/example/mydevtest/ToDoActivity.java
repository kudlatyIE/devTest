package com.example.mydevtest;

import com.example.utils.MsgResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ToDoActivity extends Activity {
	
	private Button btnDoit;
	private TextView tvRequest;
	private MsgResult result=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		
		tvRequest = (TextView) findViewById(R.id.text_request);
		btnDoit = (Button) findViewById(R.id.btn_return_stuff);
		
		
		
		btnDoit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				MsgResult.setResult(true, "Well done Nick!");
				result = MsgResult.getResult();
				Intent intent = new Intent();
				intent.putExtra("result", true);
				setResult(11,intent);
				finish();
			}
			
		});

		
	}
}
