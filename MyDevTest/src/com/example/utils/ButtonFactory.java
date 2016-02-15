package com.example.utils;

import java.util.HashMap;

public class ButtonFactory {
	
	private String btnName;
	private Integer btnId;
	public static HashMap<Integer, String> btns;
	
	public ButtonFactory(){
		if(btns==null) btns = new HashMap<Integer, String>();
	}
	
	public HashMap<Integer, String> getButtons(){
		return btns;
	}
	
	public void addBtn(int id, String btnName){
		btns.put(Integer.valueOf(id), btnName);
	}
	
	/**
	 * return Resource id for existing button or 0
	 * @param id
	 * @return
	 */
	public int getBtnId(int id){
		for(int i: btns.keySet()){
			if(i==id) return id;
		}
		return 0;
	}

	public String getBtnName() {
		return btnName;
	}

//	public Integer getBtnId() {
//		return btnId;
//	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}

	public void setBtnId(Integer btnId) {
		this.btnId = btnId;
	}

}
