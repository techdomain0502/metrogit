package com.delhi.metro.sasha.googleapis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.route.StopListUiFragment;

public class FollowRouteActivity extends FragmentActivity {
	private String src, dest,place;
    private TextView guideText,header;
    private String pathString;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.place_follow_route);
		guideText = (TextView)findViewById(R.id.guide);
		header = (TextView)findViewById(R.id.header);
		Intent i = getIntent();
		src = i.getStringExtra("source");
		dest = i.getStringExtra("destination");
		place = i.getStringExtra("place");

		if(src!=null && dest !=null && place !=null) {
		        guideText.setText(String.format(getResources().getString(R.string.place_guide_text),place,src,dest));
			    initFragment();
		}
 		else{
			header.setVisibility(View.GONE);
			guideText.setText("Sorry Unable to find any route :(. Please try again");

		}

	}

	private void initFragment() {
		StopListUiFragment fragment = new StopListUiFragment();
		Bundle args = new Bundle();
		args.putString("source", src);
		args.putString("destination", dest);
		fragment.setArguments(args);
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.container, fragment, "stoplistfragment");
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


	public void setSharePathString(String str){
		pathString = str;
		//setShareIntent(getDefaultShareIntent());
		//invalidateOptionsMenu();
	}


	public String getSharePathString(){
		return pathString!=null?pathString:"";
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sharemenu, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_item_share:
				getDefaultShareIntent();
				break;
		}
		return true;
	}
}
