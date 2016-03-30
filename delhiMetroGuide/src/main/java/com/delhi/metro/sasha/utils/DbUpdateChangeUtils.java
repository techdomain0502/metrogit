package com.delhi.metro.sasha.utils;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class DbUpdateChangeUtils {

	private static DbUpdateChangeUtils mInstance;
	private DbUpdateChangeUtils() {
	}
	
	public static DbUpdateChangeUtils getInstance() {
        if(mInstance==null)
        	mInstance = new DbUpdateChangeUtils();
        return mInstance;
	}
	
	public interface onDbUpdateFinishListener{
		public void onDbUpdate();
	}
	
	public void setDbUpdateChange(Context c) {
		notifyListeners();
		}
	
	
	
	private static ArrayList<onDbUpdateFinishListener> list = new ArrayList<>();
	
	public void addListener(onDbUpdateFinishListener listener) {
		list.add(listener);
	}
	
	private void notifyListeners() {
		for(onDbUpdateFinishListener l : list) {
			l.onDbUpdate();
		}
	}
	
	public void clearListners() {
		list.clear();
		mInstance = null;
	}

	
}
