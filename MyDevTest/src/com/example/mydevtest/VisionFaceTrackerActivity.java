package com.example.mydevtest;

import com.example.visionface.CameraSourcePreview;
import com.example.visionface.FaceMarkers;
import com.example.visionface.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VisionFaceTrackerActivity extends Activity {
	
	private final static String TAG = VisionFaceTrackerActivity.class.getSimpleName();
	private final static int RC_HANDLE_GSM = 9001, RC_HANDLE_CAMERA_PERM = 2;//camera permission request code must be less than 256!
	private CameraSource cameraSource = null;
	private CameraSourcePreview cameraPreview;
	private GraphicOverlay overlay;
	private TextView tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vision_face_tracker);
		
		cameraPreview = (CameraSourcePreview) findViewById(R.id.visionface_preview);
		overlay = (GraphicOverlay) findViewById(R.id.visionface_overlay);
		tvInfo = (TextView) findViewById(R.id.visionface_text_info);
		
		int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		if(rc==PackageManager.PERMISSION_GRANTED){
			createCameraSource();
		}else{
			requestCameraPermission(); //TODO: be continue.....
		}
		
	}
	
	private void requestCameraPermission(){
		Log.w(TAG, "requesting permission for camera....");
		final String[] permissions = new String[]{Manifest.permission.CAMERA};
		
		if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
			ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
			return;
		}
		final Activity suomi = this;
		View.OnClickListener listener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				ActivityCompat.requestPermissions(suomi, permissions, RC_HANDLE_CAMERA_PERM);				
			}
		};
		Snackbar.make(overlay, R.string.permission_camera_rationale, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, listener).show(); 
	}
	
	To be continue:
	https://github.com/googlesamples/android-vision/blob/master/visionSamples/FaceTracker/app/src/main/java/com/google/android/gms/samples/vision/face/facetracker/FaceTrackerActivity.java
			
	@Override
	public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantedResults){
		if(requestCode!=RC_HANDLE_CAMERA_PERM){
			Log.d(TAG, "Got unexpected permission result: "+requestCode);
			super.onRequestPermissionsResult(requestCode, permissions, grantedResults);
			return;
		}
		if(grantedResults.length!=0 && grantedResults[0]==PackageManager.PERMISSION_GRANTED){
			Log.d(TAG, "Camera permission granted - start initialize calera source....");
			createCameraSource();
			return;
		}
		Log.d(TAG, "Camera permission not granted: result lenght: "+grantedResults.length+" result code: "+
				(grantedResults.length>0 ? grantedResults[0] : "empty") );
		
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("This App o_O");
		builder.setMessage(R.string.no_camera_permission);
		builder.setPositiveButton(R.string.ok, listener);
		builder.show();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createCameraSource(){
		
		FaceDetector detector = new FaceDetector.Builder(this).setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build();
		
		detector.setProcessor(new MultiProcessor.Builder(new GraphicFaceTrackerFactory()).build());
		
		if(!detector.isOperational()){
			Log.w(TAG, "Face detector dependenicies are not yet avaliable!");
		}
		cameraSource = new CameraSource.Builder(this, detector).setRequestedPreviewSize(640, 480)
						.setFacing(CameraSource.CAMERA_FACING_FRONT).setRequestedFps(30f).build();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		startCameraSource();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		cameraPreview.stop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(cameraSource!=null) {
			cameraSource.release();;
			cameraSource=null;
		}
	}
	
	
	
	
	private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face>{

		@Override
		public Tracker<Face> create(Face arg0) {
			// TODO Auto-generated method stub
			return new GraphicFaceTracker(overlay);
		}
	}
	
	private class GraphicFaceTracker extends Tracker<Face>{
		private GraphicOverlay mOverlay;
		private FaceMarkers mGraphic;
		
		GraphicFaceTracker(GraphicOverlay overlay){
			this.mOverlay = overlay;
			this.mGraphic = new FaceMarkers(overlay);
		}

		@Override
		public void onDone() {
			this.mOverlay.remove(mGraphic);
		}

		@Override
		public void onMissing(FaceDetector.Detections<Face> detections) {
			this.mOverlay.remove(mGraphic);
		}

		@Override
		public void onNewItem(int id, Face item) {
			this.mGraphic.setId(id);
		}

		@Override
		public void onUpdate(FaceDetector.Detections<Face> detections, Face item) {
			this.mOverlay.add(mGraphic);
			this.mGraphic.updateFace(item);
		}
		
		
	}
}