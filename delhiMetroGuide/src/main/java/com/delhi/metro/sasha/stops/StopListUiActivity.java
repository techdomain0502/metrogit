package com.delhi.metro.sasha.stops;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.route.StopListUiFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class StopListUiActivity extends FragmentActivity {

	private String pathString;
	private ShareActionProvider mShareActionProvider; 
	private  TextView station,interchange,time,details,fare,smartFare,distance;
    public static final int UPDATE_HEADER = 0;
    private ProgressBar bar;
    
    public DetailsUiHandler getHandler() {
    	return new DetailsUiHandler();
    }
    public class DetailsUiHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
            Bundle  b = msg.getData();
            String no = b.getString("station");
            String interchange = b.getString("interchange");
            String time = b.getString("time");
			String distance = b.getString("distance");
            float fare = b.getFloat("fare");
			String smartfare = String.format("%.02f",0.9f*fare);
        		StopListUiActivity.this.station.setText(String.format(getString(R.string.noOfStations),no));
        		StopListUiActivity.this.interchange.setText(String.format(getString(R.string.noOfInterchange),interchange));
        		StopListUiActivity.this.time.setText(String.format(getString(R.string.journeyTime),time));
			    StopListUiActivity.this.distance.setText(String.format(getString(R.string.distance),distance));
     			StopListUiActivity.this.fare.setText(String.format(getString(R.string.normalfare),fare));
     			StopListUiActivity.this.smartFare.setText(String.format(getString(R.string.smartfare),smartfare));

			bar.setVisibility(View.GONE);
        		
        	}
        }	
    
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.stoplist_ui_layout);
		ActionBar ab = this.getActionBar();
		if(ab!=null) {
		    ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		    ab.setDisplayHomeAsUpEnabled(true);
		    ab.setDisplayShowHomeEnabled(true);
		}
		station = (TextView)findViewById(R.id.station);
		interchange = (TextView)findViewById(R.id.interchange);
		time = (TextView)findViewById(R.id.time);
		distance = (TextView)findViewById(R.id.distance);
		details=(TextView)findViewById(R.id.details);
		fare = (TextView)findViewById(R.id.fare);
		smartFare = (TextView)findViewById(R.id.smartfare);

	    bar = (ProgressBar)findViewById(R.id.progress);
		Intent i = getIntent();
		String source = i.getStringExtra("source");
		String destination = i.getStringExtra("destination");
		StopListUiFragment fragment = new StopListUiFragment();
 		Bundle args = new Bundle();
 		args.putString("source", source);
 		args.putString("destination", destination);
 		fragment.setArguments(args);
 		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
 		transaction.replace(R.id.sideContainer,fragment,"stoplistfragment");
 		transaction.commit();
	}
	
	
	/** Returns a share intent */ 
	private Intent getDefaultShareIntent(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, getSharePathString());
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent, "Share Route with Friends"));

		return sendIntent;
	} 
	
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent); 
	    } 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		   getMenuInflater().inflate(R.menu.sharemenu, menu);
		return true;
	}
	
	
	public void setSharePathString(String str){
		   pathString = str;
		   //setShareIntent(getDefaultShareIntent());
		   //invalidateOptionsMenu();
	   }
		
	
	public String getSharePathString(){
		   return pathString!=null?pathString:"";
		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_item_share:
		    getDefaultShareIntent();
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	
}
