package com.delhi.metro.sasha.gui;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.parking.ParkingFragment;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewDebug.FlagToString;
import android.widget.ImageView;

public class WelcomePage extends FragmentActivity {
	
	private View mWelcomeVideo = null;
  @Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.welcome_page);
	if(Build.VERSION.SDK_INT >= 16 )
		hideSystemUI();
		initFragments();      
  
   }
  
  private void hideSystemUI() {
/*	  View decorView = getWindow().getDecorView();
	  int uiOptions =View.SYSTEM_UI_FLAG_FULLSCREEN;
			 
	  decorView.setSystemUiVisibility(uiOptions);
*/	  ActionBar actionbar = getActionBar();
	  if(actionbar!=null)
	     actionbar.hide();
	  
  }

  private void initFragments() {
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction transaction = fm.beginTransaction();
      WelcomeFragment fragment = new WelcomeFragment();
      transaction.add(R.id.welcome_page_container, fragment);
      transaction.commitAllowingStateLoss();
	}
}
