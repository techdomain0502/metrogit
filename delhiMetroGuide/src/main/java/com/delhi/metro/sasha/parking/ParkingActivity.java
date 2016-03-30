package com.delhi.metro.sasha.parking;

import com.delhi.metro.sasha.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

public class ParkingActivity extends FragmentActivity {
  @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.parking_container);
	ActionBar ab = getActionBar();
	if(ab!=null) {
	ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	ab.setDisplayHomeAsUpEnabled(true);
	ab.setDisplayShowHomeEnabled(true);
	}
	
	
	
	if(findViewById(R.id.mainContainer)!=null) {
		if(savedInstanceState != null)
             return;                    
		initFragments();      
	}
  
  }

  
  @Override
	public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
  
	private void initFragments() {
	      FragmentManager fm = getSupportFragmentManager();
	      FragmentTransaction transaction = fm.beginTransaction();
	      ParkingFragment fragment = new ParkingFragment();
	      transaction.add(R.id.mainContainer, fragment);
	      transaction.commitAllowingStateLoss();
		}




}
