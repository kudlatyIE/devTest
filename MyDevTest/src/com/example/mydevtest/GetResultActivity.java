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
	private ApiStarter starter;
	private boolean bol=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_result);
		
		btnGetResult = (Button) findViewById(R.id.btn_get_result);
		tvResult = (TextView) findViewById(R.id.text_result_stuff);
		tvResult.setText(getResources().getString(R.string.title_activity_get_result));
//		btnGetResult.setVisibility(View.GONE);
//		starter= getStarter();
		
		
		btnGetResult.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				starter= getStarter();
				try {
					bol = starter.startForResult();
//					result = MsgResult.getResult();
//					tvResult.setText(result.isMsg()+" | "+result.getMsg());
				} catch (Exception e) {
//					tvResult.setText(result.isMsg()+" | "+result.getMsg());
					e.printStackTrace();
				}finally{
					result = MsgResult.getResult();
					tvResult.setText(result.isMsg()+" | "+result.getMsg()+" | "+bol);
				}
//				if(bol) tvResult.setText("dupa");
//				else {
////					result = MsgResult.getResult();
////					tvResult.setText(result.isMsg()+" | "+result.getMsg());
//					System.out.println("GetResult: "+ result.getMsg());
//				}
			}
			
		});
	}
	private ApiStarter getStarter(){
		return new ApiStarter(this);
	}
	protected void onResume(){
		super.onResume();
		
			result = MsgResult.getResult();
			tvResult.setText(result.isMsg()+" | "+result.getMsg());
		
	}
//	protected void onDestroy(){
//		super.onDestroy();
//		MsgResult.setResult(false, null);
//	}
}
