package com.example.utils;

public class MsgResult {
	
	private String msg="N/A";
	private boolean isMsg=false;
	private static MsgResult result=null;
	
	public MsgResult(boolean b, String s){
		this.isMsg=b;
		this.msg=s;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isMsg() {
		return isMsg;
	}

	public void setMsg(boolean isMsg) {
		this.isMsg = isMsg;
	}

	public static MsgResult getResult() {
		
		if(result==null){
			result = new MsgResult(false,"N/A");
		}
		return result;
	}

	public static void setResult(boolean b, String s) {
		MsgResult.result = new MsgResult(b,s);
	}

}
