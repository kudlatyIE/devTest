package com.example.mydevtest;

import com.example.network.InternetConnection;
import com.example.utils.DevDetails;
import com.example.utils.MySd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView tvMsg, tvScreen, tvNet;
	private Button btnCheckNet, btnOpenWifi, btnOpenMob, btnCamera, btnJavaView, btnForResult, btnSd, btnLib,btnSensor;
	private String dev, screen;
	private boolean isNet, isWifi=false, isMob=false;
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
		btnForResult = (Button) findViewById(R.id.btn_for_result);
		btnOpenWifi.setVisibility(View.GONE);
		btnOpenMob.setVisibility(View.GONE);
		btnSd = (Button) findViewById(R.id.btn_sd);
		btnLib = (Button) findViewById(R.id.btn_check_library);
		btnSensor = (Button) findViewById(R.id.btn_sensors_start);
		
		dev = DevDetails.getDeviceName();
		screen = DevDetails.getDensity(getApplicationContext());
		
		MyButtons button= new MyButtons();
		btnCheckNet.setOnClickListener(button);
		btnOpenWifi.setOnClickListener(button);
		btnOpenMob.setOnClickListener(button);
		btnCamera.setOnClickListener(button);
		btnJavaView.setOnClickListener(button);
		btnForResult.setOnClickListener(button);
		btnSd.setOnClickListener(button);
		btnLib.setOnClickListener(button);
		btnSensor.setOnClickListener(button);
		
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
				if(isNet) {
					tvNet.setText("internet connection ON");
					if(isWifi) btnOpenWifi.setText("WiFi OFF");
					if (isMob) btnOpenMob.setText("3G OFF");
				}
				else {
					tvNet.setText("Buuuuu");
					if(ic.getWifi()!=null) setVisibleBtn(btnOpenWifi,true);
					if(ic.getMob()!=null) setVisibleBtn(btnOpenMob,true);
				}
				if(isWifi) btnOpenWifi.setText("WiFi OFF"); else btnOpenWifi.setText("WiFi ON");
				if(isMob) btnOpenMob.setText("3G OFF"); else btnOpenMob.setText("3G ON");
				break;
			case R.id.btn_open_wifi:
				
				if(isWifi) {
					isWifi=ic.openWiFi(false);
					tvNet.setText("WiFi connection OFF!");
					btnOpenWifi.setText("WiFi ON");
//					setVisibleBtn(btnOpenWifi,false);
//					setVisibleBtn(btnOpenMob,false);
				}else{
					isWifi = ic.openWiFi(true);
					tvNet.setText("WiFi connection ON!");
					btnOpenWifi.setText("WiFi OFF");
				}
				break;
			case R.id.btn_open_mobile:
				
				if(isMob) {
					isMob=ic.openMobile(false);
					btnOpenMob.setText("3G ON");
					tvNet.setText("MobileData connection OFF!");
//					setVisibleBtn(btnOpenWifi,false);
//					setVisibleBtn(btnOpenMob,false);
				}else{
					isMob = ic.openMobile(true);
					btnOpenMob.setText("3G OFF");
					tvNet.setText("MobileData connection ON!");
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
			case R.id.btn_for_result:
				intent = new Intent(getApplicationContext(), GetResultActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_sd:
				intent = new Intent(getApplicationContext(),SDActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_check_library:
				intent = new Intent(getApplicationContext(), NativeLibActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_sensors_start:
				intent = new Intent(getApplicationContext(),SensorsActivity.class);
				startActivity(intent);
				break;
			}
			
		}
		
	}
	
}
