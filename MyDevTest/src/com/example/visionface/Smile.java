package com.example.visionface;

public class Smile {
	
	interface SmileCallback{
		boolean smileCalbackReturn();
	}
	
	SmileCallback mCallback;
	
	void registerSmileCallback(SmileCallback callback){
		mCallback = callback;
	}
	
	void doSomething(){
		//do something here
		mCallback.smileCalbackReturn();
	}

}
