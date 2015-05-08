package com.example.mydevtest;

import java.util.ArrayList;

import com.example.utils.CameraStuff;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CameraStuffActivity extends Activity {
	
	private ArrayList<CameraStuff> camList;
	private CameraStuff camStuff;
	private TextView tvCamId, tvSensorH, tvSensorW;
	private ListView lv;
	private String cameraName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_stuff);
		
		camStuff = new CameraStuff(getApplicationContext());
		camList=camStuff.getCamerasList();
		
		lv = (ListView) findViewById(R.id.list_cameras);
		MyAdapter adapter = new MyAdapter(getApplicationContext(),camList);
		lv.setAdapter(adapter);
		
	}//end onCreate
	
	class MyAdapter  extends BaseAdapter{
		private Camera camera;
		private ArrayList<CameraStuff> myList;
		private LayoutInflater inflater;
		
		MyAdapter(Context context, ArrayList<CameraStuff> list){
			this.myList=list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return myList.size();
		}

		@Override
		public Object getItem(int position) {
			return myList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null) view = inflater.inflate(R.layout.adapter_cameras, parent,false);
			else view = convertView;
			
			tvCamId = (TextView) view.findViewById(R.id.adaptercamera_text_sensor_id);
			tvSensorH = (TextView) view.findViewById(R.id.adaptercamera_text_sensor_hight);
			tvSensorW = (TextView) view.findViewById(R.id.adaptercamera_text_sensor_width);
			
			cameraName="ID:"+myList.get(position).getCameraId()+" "+myList.get(position).getCameraName();
			tvCamId.setText(cameraName);
//			camera = myList.get(position).getCamera().open();
//			tvSensorH.setText(String.valueOf(myList.get(position).getCamera().getParameters().getPictureSize().height));
//			tvSensorW.setText(String.valueOf(myList.get(position).getCamera().getParameters().getPictureSize().width));
//			camera.release();
			tvSensorH.setText(String.valueOf(myList.get(position).getHeight()));
			tvSensorW.setText(String.valueOf(myList.get(position).getWidth()));
			return view;
		}
		
	}
}

// 
