package com.example.serwisy;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SensorService extends Service{

	private final static String TAG = SensorService.class.getSimpleName();
	private boolean serviceRunning = false;
	private ServiceListener mCallback;
	private Thread sensorThread;
	private AudioRecord audio;
	private static final int SAMPLE_RATE = 8000, SAMPLE_DELAY=75;
	private static int [] mSamplesRate = new int[] {8000, 11025, 22050, 44100};
	private int bufferSize;
	
	public static ArrayList<Float> noiseList;
	private Float noise;
	
	private final Binder mBinder = new LocalBinder();
	
	@Override
	public void onCreate(){
		this.audio = findAudioRecord();
		noiseList = new ArrayList<Float>();
	}
	
	@Override
	public void onDestroy(){
		Log.d(TAG, "Sensor listener destroyed, all job is done...");
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		return super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "Sensor listener is starting!");
		startSensors();
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	
	
	private class SensorThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(serviceRunning && sensorThread.isAlive() && !sensorThread.isInterrupted()){
				synchronized(this){
					try{
						Thread.sleep(SAMPLE_DELAY);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					noise = getLevel(audio);
					mCallback.updateState(String.valueOf(noise));
					noiseList.add(noise);
					//TODO: add the rest of sensors here!
				}
			}
			
		}
		
	}
	
	private void startSensors(){
		if(!serviceRunning){
			serviceRunning = true;
			sensorThread = new Thread(new SensorThread());
			sensorThread.start();
		}
	}
	
	private void stopSensors(){
		serviceRunning = false;
		if(sensorThread!=null){
			try{
				Log.d("AUDIO", "AUDIO THREAD INTERUPTED !");
				sensorThread.interrupt();
				sensorThread=null;
				audio.stop();
				audio.release();
				
				//TODO: stop other sensors here!
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public class LocalBinder extends Binder implements ServiceMonitor {

		@Override
		public void setListener(ServiceListener callback) {
			// TODO Auto-generated method stub
			mCallback = callback;
			
		}
		
	}
	
	private float getLevel(AudioRecord ar){
		
		short[] buffer = new short[bufferSize];
		int result=1, sumLevel=0;
		if(ar!=null){
			result = ar.read(buffer, 0, bufferSize);
			for(int i=0;i<result;i++){
				sumLevel +=buffer[i];
			}
		}
		return Math.abs(sumLevel/result);
	}
	
	private AudioRecord findAudioRecord(){
		int[] mAudioFormat = new int[] {AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT};
//		int aFormat = AudioFormat.ENCODING_PCM_16BIT;
		int[] mAudioChanels = new int[] {AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.CHANNEL_CONFIGURATION_MONO};
		for(int rate: mSamplesRate){
			for(int af: mAudioFormat){
				Log.d("AUDIO", "ENCODING_PCM_16BIT:  "+af);
				for(int ach: mAudioChanels){
					try{
						this.bufferSize = AudioRecord.getMinBufferSize(rate, ach, af);
						Log.i("AUDIO", "Attempting rate: "+rate+" Hz"+ ", Format: "+af+ ", chanell: "+ach);
						if(bufferSize != AudioRecord.ERROR_BAD_VALUE){
							AudioRecord ar = new AudioRecord(AudioSource.DEFAULT,rate,ach,af,bufferSize);
							if(ar.getState() == AudioRecord.STATE_INITIALIZED) {
								Log.v("AUDIO", "AudioRecord INITIALIZED! "+ar.getState());
								Log.v("AUDIO", "Found rate: "+rate+" Hz"+ ", Format: "+af+ ", chanell: "+ach);
//								result = String.valueOf(getLevel(ar));
//								Log.d("AUDIO", "SOUND CAPTURED: "+result);
								return ar;
							}else{
								Log.v("AUDIO", "AudioRecord UNINITIALIZED! "+ar.getState());
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
		
		Log.i("AUDIO", "AudioRecord Not Found!");
		return null;
	}

	
}
