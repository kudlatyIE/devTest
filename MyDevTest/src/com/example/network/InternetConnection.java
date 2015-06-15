package com.example.network;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class InternetConnection {

	private Context context;
	private NetworkInfo wifi, mob, eth, vpn, usb;
	private enum NETWORK  {WIFI,MOBILE, ETH,VPN};
	
	public InternetConnection(Context c){
		this.context=c;
		this.wifi=null;
		this.mob=null;
		this.eth=null;
		this.vpn=null;
	}
	
	public boolean isConnection(){
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo wifi, mob, eth;
		NetworkInfo[] ni;
		boolean isNet=false;
		
		if(cm!=null){
//			wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			mob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			if(wifi.isConnected()|| mob.isConnected()) return true;
			ni= cm.getAllNetworkInfo();
			for(NetworkInfo info:ni) {
				Log.e("NET TYPE", info.getTypeName());
				setTypeConnection(info);
				if (!isNet) isNet=info.isConnectedOrConnecting();
			}
		}
		return isNet;
	}
	
	public boolean openWiFi(boolean enabled ){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm!=null){
//			NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			wm.setWifiEnabled(enabled);
		}
		return enabled;
	}
	
	public boolean openMobile(boolean enabled){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm!=null){
			NetworkInfo mob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			try{
				final Class cmClass = Class.forName(cm.getClass().getName());
				final Field cmField = cmClass.getDeclaredField("mService");
				cmField.setAccessible(true);
				final Object cmManager = cmField.get(cm);
				final Class<?> cmCManagerClass = Class.forName(cmManager.getClass().getName());
				final Method setMobileDataEnabled = cmCManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
				setMobileDataEnabled.setAccessible(true);
				setMobileDataEnabled.invoke(cmManager, enabled);
				
			}catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException | 
					IllegalArgumentException | NoSuchMethodException | InvocationTargetException ex ){
				ex.printStackTrace();
				return false;
			}
			if(mob.isConnectedOrConnecting()) return true;
		}
		return false;
	}
	
	private void setTypeConnection(NetworkInfo ni){
		switch(ni.getTypeName()){
		case "WIFI": 	this.wifi=ni; 	break;
		case "mobile":	this.mob=ni; 	break;
		case "VPN":		this.vpn=ni;	break;
		case "ETH":		this.eth=ni;	break;
		case "USBNET":	this.usb=ni;	break;
//		default: return false;
		}
	}

	public NetworkInfo getWifi() {
		return wifi;
	}

	public void setWifi(NetworkInfo wifi) {
		this.wifi = wifi;
	}

	public NetworkInfo getMob() {
		return mob;
	}

	public void setMob(NetworkInfo mob) {
		this.mob = mob;
	}

	public NetworkInfo getEth() {
		return eth;
	}

	public void setEth(NetworkInfo eth) {
		this.eth = eth;
	}

	public NetworkInfo getVpn() {
		return vpn;
	}

	public void setVpn(NetworkInfo vpn) {
		this.vpn = vpn;
	}

	public NetworkInfo getUsb() {
		return usb;
	}

	public void setUsb(NetworkInfo usb) {
		this.usb = usb;
	}
}

//public boolean isConnection(){
//	
//	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//	NetworkInfo wifi, mob, eth;
//	if(cm!=null){
//		wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		mob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		eth = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
//		if((wifi.getState()==NetworkInfo.State.CONNECTED) || 
//			(mob.getState()==NetworkInfo.State.CONNECTED) || 
//			(eth.getState()==NetworkInfo.State.CONNECTED)) return true;
//	}
//	return false;

//===========================================================

//public boolean isConnection(){
//	
//	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//	
//	if(cm!= null){
//		NetworkInfo[] info = cm.getAllNetworkInfo();
//		if(info !=null){
//			for(int i =0;i<info.length;i++) if(info[i].getState()==NetworkInfo.State.CONNECTED){
//				return true;
//			}
//		}
//	}
//	return false;
//}