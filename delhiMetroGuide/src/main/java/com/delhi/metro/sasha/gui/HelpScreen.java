package com.delhi.metro.sasha.gui;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.FlipView.OnFlipListener;
import se.emilsjolander.flipview.FlipView.OnOverFlipListener;
import se.emilsjolander.flipview.OverFlipMode;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.gui.FlipAdapter.Callback;
public class HelpScreen extends Activity implements Callback, OnFlipListener, OnOverFlipListener {
	
	private FlipView mFlipView;
	private FlipAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		ActionBar ab = getActionBar();
		if(ab!=null) {
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		}
		mFlipView = (FlipView) findViewById(R.id.flip_view);
		mAdapter = new FlipAdapter(this);
		mAdapter.setCallback(this);
		mFlipView.setAdapter(mAdapter);
		mFlipView.setOnFlipListener(this);
		mFlipView.peakNext(false);
		//mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		//mFlipView.setEmptyView(findViewById(R.id.empty_view));
		mFlipView.setOnOverFlipListener(this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	//	getMenuInflater().inflate(R.menu.menu, menu);
		return true;
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

	@Override
	public void onPageRequested(int page) {
		mFlipView.smoothFlipTo(page);
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		if(position > mFlipView.getPageCount()-3 && mFlipView.getPageCount()<30){
	//		mAdapter.addItems(5);
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
	}

}
