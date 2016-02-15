package com.example.mydevtest;

import com.example.network.InternetConnection;
import com.example.utils.ButtonFactory;
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
	private Button btnCheckNet, btnOpenWifi, btnOpenMob, btnCamera, btnJavaView, btnForResult, btnSd, btnLib,btnSensor;
	private String dev, screen;
	private boolean isNet, isWifi=false, isMob=false;
	private InternetConnection ic;
	private ButtonFactory btn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.btn = new ButtonFactory();
		
		tvMsg = (TextView) findViewById(R.id.text_msg);
		tvScreen = (TextView) findViewById(R.id.text_screen);
		tvNet = (TextView) findViewById(R.id.text_network_status);
		btnCheckNet = (Button) findViewById(R.id.btn_check_connection);
		btn.addBtn(R.id.btn_check_connection, "btn_check_connection");
		btnOpenWifi = (Button) findViewById(R.id.btn_open_wifi);
		btn.addBtn(R.id.btn_open_wifi, "btn_open_wifi");
		btnOpenMob = (Button) findViewById(R.id.btn_open_mobile);
		btn.addBtn(R.id.btn_open_mobile, "btn_open_mobile");
		btnCamera = (Button) findViewById(R.id.btn_getCameraParams);
		btn.addBtn(R.id.btn_getCameraParams, "btn_getCameraParams");
		btnJavaView = (Button) findViewById(R.id.btn_new_view);
		btn.addBtn(R.id.btn_new_view, "btn_new_view");
		btnForResult = (Button) findViewById(R.id.btn_for_result);
		btn.addBtn(R.id.btn_for_result, "btn_for_result");
		btnOpenWifi.setVisibility(View.GONE);
		btnOpenMob.setVisibility(View.GONE);
		btnSd = (Button) findViewById(R.id.btn_sd);
		btn.addBtn(R.id.btn_sd, "btn_sd");
		btnLib = (Button) findViewById(R.id.btn_check_library);
		btn.addBtn(R.id.btn_check_library, "btn_check_library");
		btnSensor = (Button) findViewById(R.id.btn_sensors_start);
		btn.addBtn(R.id.btn_sensors_start, "btn_sensors_start");
		
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
			int id = v.getId();
			if(btn.getBtnId(R.id.btn_check_connection)==id){
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
			}
			if(btn.getBtnId(R.id.btn_open_wifi)==id){
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
			}
			if(btn.getBtnId(R.id.btn_open_mobile)==id){
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
			}
			if(btn.getBtnId(R.id.btn_getCameraParams)==id){
				intent = new Intent(getApplicationContext(), CameraStuffActivity.class);
				startActivity(intent);
			}
			if (btn.getBtnId(R.id.btn_new_view)==id){
				intent = new Intent(getApplicationContext(),JavaViewActivity.class);
				startActivity(intent);
			}
			if(btn.getBtnId( R.id.btn_for_result)==id){
				intent = new Intent(getApplicationContext(), GetResultActivity.class);
				startActivity(intent);
			}
			if(btn.getBtnId(R.id.btn_sd)==id){
				intent = new Intent(getApplicationContext(),SDActivity.class);
				startActivity(intent);
			}
			if(btn.getBtnId(R.id.btn_check_library)==id){
				intent = new Intent(getApplicationContext(), NativeLibActivity.class);
				startActivity(intent);
			}
			if(btn.getBtnId(R.id.btn_sensors_start)==id){
				intent = new Intent(getApplicationContext(),SensorsActivity.class);
				startActivity(intent);
			}
//			switch(v.getId()){
//			case btn.getBtnId(R.id.btn_check_connection):
//				
//				isNet = ic.isConnection();
//				if(isNet) {
//					tvNet.setText("internet connection ON");
//					if(isWifi) btnOpenWifi.setText("WiFi OFF");
//					if (isMob) btnOpenMob.setText("3G OFF");
//				}
//				else {
//					tvNet.setText("Buuuuu");
//					if(ic.getWifi()!=null) setVisibleBtn(btnOpenWifi,true);
//					if(ic.getMob()!=null) setVisibleBtn(btnOpenMob,true);
//				}
//				if(isWifi) btnOpenWifi.setText("WiFi OFF"); else btnOpenWifi.setText("WiFi ON");
//				if(isMob) btnOpenMob.setText("3G OFF"); else btnOpenMob.setText("3G ON");
//				break;
//			case R.id.btn_open_wifi:
//				
//				if(isWifi) {
//					isWifi=ic.openWiFi(false);
//					tvNet.setText("WiFi connection OFF!");
//					btnOpenWifi.setText("WiFi ON");
////					setVisibleBtn(btnOpenWifi,false);
////					setVisibleBtn(btnOpenMob,false);
//				}else{
//					isWifi = ic.openWiFi(true);
//					tvNet.setText("WiFi connection ON!");
//					btnOpenWifi.setText("WiFi OFF");
//				}
//				break;
//			case R.id.btn_open_mobile:
//				
//				if(isMob) {
//					isMob=ic.openMobile(false);
//					btnOpenMob.setText("3G ON");
//					tvNet.setText("MobileData connection OFF!");
////					setVisibleBtn(btnOpenWifi,false);
////					setVisibleBtn(btnOpenMob,false);
//				}else{
//					isMob = ic.openMobile(true);
//					btnOpenMob.setText("3G OFF");
//					tvNet.setText("MobileData connection ON!");
//				}
//				break;
//			case R.id.btn_getCameraParams:
//				intent = new Intent(getApplicationContext(), CameraStuffActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.btn_new_view:
//				intent = new Intent(getApplicationContext(),JavaViewActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.btn_for_result:
//				intent = new Intent(getApplicationContext(), GetResultActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.btn_sd:
//				intent = new Intent(getApplicationContext(),SDActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.btn_check_library:
//				intent = new Intent(getApplicationContext(), NativeLibActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.btn_sensors_start:
//				intent = new Intent(getApplicationContext(),SensorsActivity.class);
//				startActivity(intent);
//				break;
//			}
			
		}
		
	}
	
}
