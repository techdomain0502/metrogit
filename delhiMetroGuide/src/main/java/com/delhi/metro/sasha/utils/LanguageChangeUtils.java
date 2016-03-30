package com.delhi.metro.sasha.utils;

import java.util.ArrayList;
import java.util.Locale;

import com.delhi.metro.sasha.gui.MetroApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class LanguageChangeUtils {

	private Context mContext;
	private static LanguageChangeUtils mInstance;
	private Locale currentLocale,previousLocale;
	private LanguageChangeUtils() {
	}
	
	public static LanguageChangeUtils getInstance() {
        if(mInstance==null)
        	mInstance = new LanguageChangeUtils();
        return mInstance;
	}
	
	public interface onLanguageChangeListener{
		public void onLanguageChange(Locale l);
	}
	
	public void setLanguageChange(Locale l,Context c) {
		mContext = c;
		currentLocale = l;
		if(previousLocale!=currentLocale) {
		   updateCurrentLocale(currentLocale);
		   previousLocale = currentLocale;
		   SharedPreferences.Editor editor = mContext.getSharedPreferences("metro", mContext.MODE_PRIVATE).edit();
		   editor.putString("language", currentLocale.toString());
		   editor.commit();
		   notifyListeners(currentLocale);
		}
	
	}
	
	public static  void updateCurrentLocale(Locale l) {
            Configuration configuration = new Configuration();
            configuration.locale = l;
            configuration.fontScale = MetroApplication.getInstance().getResources().getConfiguration().fontScale;
            MetroApplication.getInstance().getResources().updateConfiguration(configuration, null);       
	}

	private static ArrayList<onLanguageChangeListener> list = new ArrayList<>();
	
	public void addListener(onLanguageChangeListener listener) {
		list.add(listener);
	}
	
	private void notifyListeners(Locale locale) {
		for(onLanguageChangeListener l : list) {
			l.onLanguageChange(locale);
		}
	}
	
	public void clearListners() {
		list.clear();
		mInstance = null;
	}

	
}
