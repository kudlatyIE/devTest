package com.example.visionface;

public class FaceInterfaces {
	
	public interface SmileEvent {

		void smiling(boolean isSmile);
	}
	
	public interface SavePicture {
		void saveIt(boolean readyToDo);
	}
	public interface PicDone{
		void isSaved(boolean status);
	}
	
	SmileEvent smileEvent;
	SavePicture savePic;
	
	public void registerSmileCallback(SmileEvent smile){
		smileEvent=smile;
	}
	
	public void registerSaveCallabck(SavePicture save){
		savePic=save;
	}


}
