package com.example.mydevtest;

import com.example.utils.ApiStarter;
import com.example.utils.MsgResult;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GetResultActivity extends Activity {
	
	private Button btnGetResult;
	private TextView tvResult;
	private MsgResult result;
	private boolean bol=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_result);
		
		btnGetResult = (Button) findViewById(R.id.btn_get_result);
		tvResult = (TextView) findViewById(R.id.text_result_stuff);
		tvResult.setText(getResources().getString(R.string.title_activity_get_result));
//		btnGetResult.setVisibility(View.GONE);
		
		
		
		btnGetResult.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ApiStarter starter = getStarter();
				bol = starter.startCamInfo();
				if(bol) tvResult.setText("dupa");
				else {
					result = MsgResult.getResult();
					tvResult.setText(result.isMsg()+" | "+result.getMsg());
					System.out.println("GetResult: "+ result.getMsg());
				}
			}
			
		});
	}
	private ApiStarter getStarter(){
		return new ApiStarter(this);
	}
}
