package com.delhi.metro.sasha.tutorial;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.gui.MainTabActivity;

public class TutorialScreen extends FragmentActivity implements CustomButtonCallBacks {
	    private ViewPager pager;
   		private TutorialPagerAdapter mAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
           setContentView(R.layout.tutorial);
           pager = (ViewPager)findViewById(R.id.pager);
       	   mAdapter = new TutorialPagerAdapter(this.getSupportFragmentManager());
    	   pager.setAdapter(mAdapter);
        }

		@Override
		public void onNextClicked() {
			if(pager.getCurrentItem()==3) {
				 finish();
				 Intent i = new Intent();
				 i.setComponent(new ComponentName(TutorialScreen.this,MainTabActivity.class));
				 startActivity(i);
			}else if(pager.getCurrentItem()+1<=pager.getChildCount())
                pager.setCurrentItem(pager.getCurrentItem()+1);
		}

		@Override
		public void onPreviousClicked() {
			// TODO Auto-generated method stub
			if(pager.getCurrentItem()-1>=0)
			    pager.setCurrentItem(pager.getCurrentItem()-1);
		}
}
