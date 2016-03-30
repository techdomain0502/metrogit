package com.delhi.metro.sasha.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.delhi.metro.sasha.gui.MainTabActivity;

public class Utils {
     
	public static String getDeviceMetrics(Context context) {
		
		DisplayMetrics metrics = new DisplayMetrics();
		((MainTabActivity)context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		float des = metrics.density;
		int w = metrics.widthPixels;
		int h = metrics.heightPixels;
	    String info = des+" "+w+" "+h;
        Toast.makeText(context,info, 500).show();

	    return info;
	}
	
	
	public static boolean isWifiorData_connected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mData = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(mWifi.isConnected() || mData.isConnected())
			return true;
		
		return false;
	}
	
	public static boolean isNullorWhiteSpace(String str){
		return (str==null||str.isEmpty())?true:false;
	}
	
}
