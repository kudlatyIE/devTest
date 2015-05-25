package com.example.utils;

import com.example.mydevtest.GetResultActivity;
import com.example.mydevtest.ToDoActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

public class ApiStarter {
	private boolean isStart=false;
	private String msg="NO cam info";
	private Context context;
	private Activity activity;
	private FragmentManager fm;
	
	public ApiStarter(GetResultActivity getResultActivity){
		this.activity=getResultActivity;
		context = getResultActivity.getApplicationContext();
		this.fm= getResultActivity.getFragmentManager();
		System.out.println("API constructor - YES!!");
	}
	
	public boolean startCamInfo() throws Exception{
//	public String startCamInfo(){
		Fragment fr = new Fragment(){
			@Override
			public void onAttach(Activity activity){
				super.onAttach(activity);
				Intent intent = new Intent(activity.getApplicationContext(), ToDoActivity.class);
				startActivityForResult(intent, 11);
				System.out.println("API, onAtache(): "+ "YES");
			}
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data){
				System.out.println("API, onActivityResult(): "+ "YES");
				if(requestCode==11){
					if(resultCode==Activity.RESULT_OK){
						if(true==data.getBooleanExtra("result",false)) isStart = true;
						else isStart = false;
//						if(true==data.getBooleanExtra("result", false)) msg = MsgResult.getResult().getMsg();
					}
				}
			}
		};
		FragmentTransaction ft = this.fm.beginTransaction();
		ft.add(fr, "getUserName");
		ft.commit();
		if(isStart) throw new Exception("Buuuuu! Failure!");
		return isStart;
//		return msg;
	}

}
