package com.delhi.metro.sasha.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

	public TutorialPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new FirstTutorialPage();
		case 1:
			return new FirstPage();
		case 2:
			return new SecondPage();
		case 3:
			return new ThirdPage();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of pages
		return 4;
	}

}
