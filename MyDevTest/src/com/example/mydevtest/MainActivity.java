package com.example.mydevtest;

import com.example.network.InternetConnection;
import com.example.utils.DevDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView tvMsg, tvScreen, tvNet;
	private Button btnCheckNet, btnOpenWifi, btnOpenMob, btnCamera, btnJavaView;
	private String dev, screen;
	private boolean isNet;
	private InternetConnection ic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvMsg = (TextView) findViewById(R.id.text_msg);
		tvScreen = (TextView) findViewById(R.id.text_screen);
		tvNet = (TextView) findViewById(R.id.text_network_status);
		btnCheckNet = (Button) findViewById(R.id.btn_check_connection);
		btnOpenWifi = (Button) findViewById(R.id.btn_open_wifi);
		btnOpenMob = (Button) findViewById(R.id.btn_open_mobile);
		btnCamera = (Button) findViewById(R.id.btn_getCameraParams);
		btnJavaView = (Button) findViewById(R.id.btn_new_view);
		btnOpenWifi.setVisibility(View.GONE);
		btnOpenMob.setVisibility(View.GONE);
		
		dev = DevDetails.getDeviceName();
		screen = DevDetails.getDensity(getApplicationContext());
		
		MyButtons button= new MyButtons();
		btnCheckNet.setOnClickListener(button);
		btnOpenWifi.setOnClickListener(button);
		btnOpenMob.setOnClickListener(button);
		btnCamera.setOnClickListener(button);
		btnJavaView.setOnClickListener(button);
		
		
		tvMsg.setText(dev);
		tvScreen.setText(screen);
		tvNet.setText("No internet connection :(");
		
		
	}
	
	private void setVisibleBtn(Button btn,boolean b){
		if(b) btn.setVisibility(View.VISIBLE);
		else  btn.setVisibility(View.GONE);
	}
	
	private class MyButtons implements OnClickListener{
		Intent intent;
		MyButtons(){
			ic = new InternetConnection(getApplicationContext());
		}

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btn_check_connection:
				
				isNet = ic.isConnection();
				if(isNet) tvNet.setText("internet connection ON");
				else {
					tvNet.setText("Buuuuu");
					if(ic.getWifi()!=null) setVisibleBtn(btnOpenWifi,true);
					if(ic.getMob()!=null) setVisibleBtn(btnOpenMob,true);
				}
				break;
			case R.id.btn_open_wifi:
				isNet = ic.openWiFi();
				if(isNet) {
					tvNet.setText("WiFi connection ON!");
					setVisibleBtn(btnOpenWifi,false);
					setVisibleBtn(btnOpenMob,false);
				}
				break;
			case R.id.btn_open_mobile:
				isNet = ic.openMobile(true);
				if(isNet) {
					tvNet.setText("MobileData connection ON!");
					setVisibleBtn(btnOpenWifi,false);
					setVisibleBtn(btnOpenMob,false);
				}
				break;
			case R.id.btn_getCameraParams:
				intent = new Intent(getApplicationContext(), CameraStuffActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_new_view:
				intent = new Intent(getApplicationContext(),JavaViewActivity.class);
				startActivity(intent);
				break;
			}
			
		}
		
	}
	
}
