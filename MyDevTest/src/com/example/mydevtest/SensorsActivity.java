package com.example.mydevtest;

import com.example.utils.SensiSensors;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SensorsActivity extends Activity {
	
	private TextView tvLight,tvNoise,tvVibration,tvOther;
	private Button btnStop;
	private SensiSensors sensors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors);
		
		tvLight = (TextView) findViewById(R.id.sensnor_text_light);
		tvNoise = (TextView) findViewById(R.id.sensnor_text_noise);
		tvVibration = (TextView) findViewById(R.id.sensnor_text_winrations);
		tvOther = (TextView) findViewById(R.id.sensnor_text_other);
		btnStop = (Button) findViewById(R.id.sensors_btn_stop);
		
		Buttons button = new Buttons();
		btnStop.setOnClickListener(button);
		sensors = new SensiSensors(this);
		sensors.checkLight(tvLight);
		sensors.checkAcceleration(tvVibration);
		sensors.checkRotation(tvOther);
	}
	
	class Buttons implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.sensors_btn_stop:
				if(sensors!=null) sensors.disableSensorManager();
				break;
			}
		}
		
	}
}