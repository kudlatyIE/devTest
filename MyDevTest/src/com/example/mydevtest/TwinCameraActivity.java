package com.example.mydevtest;

import com.example.dualcamera.CameraPreview;
import com.example.dualcamera.FaceDetectListener;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TwinCameraActivity extends Activity {
	
	private TextView tvInfo;
	private FrameLayout frontView, rearView;
	private Camera cFront, cRear;
	private CameraPreview previewFront, previewRear;
	private static int front=CameraInfo.CAMERA_FACING_FRONT, back=CameraInfo.CAMERA_FACING_BACK;
	
	private String info="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twin_camera);
		
		frontView = (FrameLayout) findViewById(R.id.camera_front_preview);
		rearView = (FrameLayout) findViewById(R.id.camera_rear_preview);
		
//		cRear = CameraPreview.getCamera(CameraInfo.CAMERA_FACING_BACK);
		cFront = CameraPreview.getCamera(front);
		cRear = CameraPreview.getCamera(back);
		
		cFront.setFaceDetectionListener(new FaceDetectListener(this,frontView ));
	
		
		CameraPreview.lumos(cFront, 3);
		CameraPreview.setPortrait(this, cFront, front);
		CameraPreview.setFaceArea(cFront);
		CameraPreview.setNewWhiteBalance(cFront);
		
		previewFront = new CameraPreview(this, cFront);
		previewRear = new CameraPreview(this, cRear);

		
		tvInfo = (TextView) findViewById(R.id.camera_preview_txt_info);
		tvInfo.setVisibility(View.GONE);
		
		if(cFront!=null) {
			
			frontView.addView(previewFront); 
			
		}else{
			info = "Front Camera NULL \n";
			tvInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(info);
		}
		if(cRear!=null) {
			rearView.addView(previewRear);
		}else{
			//just disable back surface view
//			rearView.setVisibility(View.GONE);
			info = info+"Rear Camera NULL ";
			tvInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(info);
		}
	}
	
	@Override
	public void onBackPressed(){
		if(cFront!=null) {
			cFront.release();
			cFront = null;
		}
		if(cRear!=null) {
			cRear.release();
			cRear = null;
		}
		finish();
	}
}
