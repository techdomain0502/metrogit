package com.delhi.metro.sasha.googleapis;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.delhi.metro.sasha.R;

public class OfflineMapActivity extends Activity{
    private WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 
	 setContentView(R.layout.static_map_layout);

		ActionBar ab = getActionBar();
		if(ab!=null) {
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		}
	
		
	 webview = (WebView)findViewById(R.id.webview);
	 webview.getSettings().setBuiltInZoomControls(true);
	 webview.getSettings().setDisplayZoomControls(true);
	 webview.getSettings().setSupportZoom(true);
	 webview.getSettings().setJavaScriptEnabled(true);
	 webview.getSettings().setLoadWithOverviewMode(true);
	 webview.getSettings().setUseWideViewPort(true);
	 webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	 webview.setScrollbarFadingEnabled(true);
	 
	 webview.loadDataWithBaseURL("file:///android_asset/", "<img src='map.jpg' />", "text/html", "utf-8", null);
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


}
