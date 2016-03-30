package com.delhi.metro.sasha.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.delhi.metro.sasha.R;

public class AboutScreen extends Activity {
private TextView ref,email,version;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_one_parallax);
	
		ActionBar ab = getActionBar();
		if(ab!=null) {
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		}
	
	   ref = (TextView)findViewById(R.id.reference);
	   email = (TextView)findViewById(R.id.email);
	   version = (TextView)findViewById(R.id.version);
       
	   ref.setText(Html.fromHtml(getResources().getString(
				R.string.reference)));
	   ref.setMovementMethod(LinkMovementMethod.getInstance());
	   ref.setPaintFlags(ref.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
	   ref.setTextColor(Color.BLUE);
	   ref.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				 Intent browse = new Intent( Intent.ACTION_VIEW );
                 browse.setData( Uri.parse( "http://www.delhimetrorail.com" ));
                 if(browse.resolveActivity(AboutScreen.this.getPackageManager()) != null)
                 startActivity(Intent.createChooser(browse, "Open Link"));
                 else
                	 Toast.makeText(getApplicationContext(), "No matching applications found", 500).show();
			}
		});
	   
	   
	   email.setText(Html.fromHtml(getResources().getString(
				R.string.suggestion)));
	   email.setMovementMethod(LinkMovementMethod.getInstance());
	   email.setPaintFlags(email.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
	   email.setTextColor(Color.BLUE);
	   email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				    Intent email = new Intent(Intent.ACTION_SEND);
				    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"webdev0502@yahoo.com"});          
				    email.putExtra(Intent.EXTRA_SUBJECT, "subject");
				    email.putExtra(Intent.EXTRA_TEXT, "message");
				    email.setType("message/rfc822");
				    startActivity(Intent.createChooser(email, "Choose an Email client :"));
				    
			}
		});
	   
	   setVersion();
	}
	
	private void setVersion() {
		String appVersion="yyyymmdd.xx.xx";
		PackageManager pm = getPackageManager();
		try { 
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			appVersion = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException ex) { 
			appVersion = "yyyymmdd.xx.xx";
		}
		if(version!=null)
		   version.setText(appVersion);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.menu, menu);
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
}
