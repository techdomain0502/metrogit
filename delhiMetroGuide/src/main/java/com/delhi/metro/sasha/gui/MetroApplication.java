package com.delhi.metro.sasha.gui;

import android.app.Application;

public class MetroApplication extends Application {
     
	private static MetroApplication mInstance;
	@Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
       mInstance = this;
     }

	
 public static MetroApplication getInstance(){
	 return mInstance;
 }
 
 
 
 
}
