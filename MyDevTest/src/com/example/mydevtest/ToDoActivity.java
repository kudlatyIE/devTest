package com.example.mydevtest;

import com.example.utils.ButtonFactory;
import com.example.utils.MsgResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ToDoActivity extends Activity {
	
	private Button btnDoit, btnDont;
	private MsgResult result;
	private ButtonFactory btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		
	
		btnDoit = (Button) findViewById(R.id.btn_return_stuff1);
		btn.addBtn(R.id.btn_return_stuff1, "btn_return_stuff1");
		btnDont = (Button) findViewById(R.id.btn_return_stuff2);
		btn.addBtn(R.id.btn_return_stuff2, "btn_return_stuff2");
		
		Buttony btn = new Buttony();
		
		btnDoit.setOnClickListener(btn);
		btnDont.setOnClickListener(btn);
		
	}
	
	 private class Buttony implements OnClickListener{
		 Intent intent;
			@Override
			public void onClick(View v) {
				
				if(btn.getBtnId(R.id.btn_return_stuff1)==v.getId()){
					MsgResult.setResult(true, "Well done Nick!");
					returnIntent(ToDoActivity.this,"result",true,Activity.RESULT_OK,"my true return");
				}
				if(btn.getBtnId(R.id.btn_return_stuff2)==v.getId()){
					MsgResult.setResult(false, "Straszna koopa Nick!");
					returnIntent(ToDoActivity.this,"result",false,Activity.RESULT_OK,"my false return");
				}
//				switch(v.getId()){
//				case R.id.btn_return_stuff1:
//					MsgResult.setResult(true, "Well done Nick!");
////					result = MsgResult.getResult();
////					intent = new Intent();
////					intent.putExtra("result", true);
////					setResult(11,intent);
////					finish();
//					returnIntent(ToDoActivity.this,"result",true,Activity.RESULT_OK,"my true return");
//					break;
//				case R.id.btn_return_stuff2:
//					MsgResult.setResult(false, "Straszna koopa Nick!");
////					result = MsgResult.getResult();
////					intent = new Intent();
////					intent.putExtra("result", false);
////					setResult(11,intent);
////					finish();
//					returnIntent(ToDoActivity.this,"result",false,Activity.RESULT_OK,"my false return");
//					break;
//				}
				
			}
			
		}
	 
	 public static void returnIntent(Activity ac, String dataName, boolean returnValue,int flag, String logMsg){
//			Context c = ac.getApplicationContext();
			Intent returnIntent = ac.getIntent();
//			if(flag==Activity.RESULT_OK) {
				returnIntent.putExtra(dataName,returnValue);
				returnIntent.putExtra("msg", logMsg);
//			}
			ac.setResult(flag, returnIntent);
			Log.e(dataName, logMsg+": "+returnValue);
			ac.finish();
		}
}
