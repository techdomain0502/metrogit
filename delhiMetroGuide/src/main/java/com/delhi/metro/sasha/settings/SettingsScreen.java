package com.delhi.metro.sasha.settings;

import java.util.List;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.googleapis.FindNearbyAttractionRouteActivity;
import com.delhi.metro.sasha.googleapis.OfflineMapActivity;
import com.delhi.metro.sasha.gui.AboutScreen;
import com.delhi.metro.sasha.gui.HelpScreen;
import com.delhi.metro.sasha.gui.MainTabActivity;
import com.delhi.metro.sasha.parking.ParkingActivity;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsScreen extends PreferenceActivity implements OnPreferenceClickListener{
	
	private Preference help,map,parking,about,update,like;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
       
		ActionBar ab = this.getActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);

		help = findPreference("metro_help");
       help.setIntent(new Intent(getApplicationContext(), HelpScreen.class));
       
       map = findPreference("metro_map");
       map.setIntent(new Intent(getApplicationContext(), OfflineMapActivity.class));
       
       parking = findPreference("metro_parking");
       parking.setIntent(new Intent(getApplicationContext(), ParkingActivity.class));
       
       about = findPreference("about");
       about.setIntent(new Intent(getApplicationContext(), AboutScreen.class));
       
       update = findPreference("update");
       update.setOnPreferenceClickListener(this);       
       
       like = findPreference("like");
       like.setOnPreferenceClickListener(this);
       
       
       
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home){
			finish();
			return true; 
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey().toString();
		if(key.equalsIgnoreCase("update") || key.equalsIgnoreCase("like")) {
			openAppPlayStore(getApplicationContext());
		}
		return true;
	}



	private  void openAppPlayStore(Context context) {
        Intent launchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(launchIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName.equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                        );
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                launchIntent.setComponent(componentName);
                if(launchIntent.resolveActivity(context.getPackageManager())!=null)
                   context.startActivity(launchIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+context.getPackageName()));
            if(webIntent.resolveActivity(context.getPackageManager())!=null)
                 context.startActivity(webIntent);
        }
    }



}
