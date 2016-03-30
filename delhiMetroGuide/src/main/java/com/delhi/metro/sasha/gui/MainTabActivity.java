package com.delhi.metro.sasha.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.db.DbHelper;
import com.delhi.metro.sasha.googleapis.FindNearbyAttractionRouteActivity;
import com.delhi.metro.sasha.googleapis.OfflineMapActivity;
import com.delhi.metro.sasha.parking.ParkingActivity;
import com.delhi.metro.sasha.settings.SettingsScreen;
import com.delhi.metro.sasha.utils.DbUpdateChangeUtils;
import com.delhi.metro.sasha.utils.LanguageChangeUtils;
import com.delhi.metro.sasha.utils.LanguageChangeUtils.onLanguageChangeListener;

public class MainTabActivity extends FragmentActivity implements
		OnTabChangeListener, OnPageChangeListener,onLanguageChangeListener {
	private TabsPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private TabHost mTabHost;
	private boolean LOAD_DATA =false;// to repopulate metro.db or not
	ProgressDialog pd = null;
	private Handler handle = new Handler();
	private Dialog dialog;
	
	private String[] langmenuSettings,langmenuSettings1;
	private String lang;
	private HashMap<String, String> langMap;
    private ShareActionProvider mShareActionProvider;
	private static final int copyFinished = 1;
	
	public class DbHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.what;
		   switch(event) {
		   case copyFinished:
			   pd.setMessage("Initialising Language Resources...");
			   pd.show();
			   new UpdateDataTask().execute();
			   break;
		   }
		}
	}
	public class UpdateDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			DataFeeder.getInstance(getApplicationContext()).initialize(getApplicationContext());
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			DbUpdateChangeUtils.getInstance().setDbUpdateChange(getApplicationContext());
			pd.dismiss();
		}
	}
	

	public class CopyDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			copyData();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		   new DbHandler().sendEmptyMessage(copyFinished);
		}
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);

		String appVersion="yyyymmdd.xx.xx";
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			appVersion = String.valueOf(packageInfo.versionCode);
		} catch (PackageManager.NameNotFoundException ex) {
			appVersion = "yyyymmdd.xx.xx";
		}


		langmenuSettings = getResources().getStringArray(R.array.language_settings);
		pref = this.getSharedPreferences("metro", MODE_PRIVATE);
		lang = pref.getString("language", "en");
	    String version = pref.getString("version","");
		if(!version.equalsIgnoreCase(appVersion)){
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("version", String.valueOf(appVersion));
			File f = new File("/data/data/com.delhi.metro.sasha/databases/metro.db");
			if(f.exists()) {
				f.delete();
				editor.putBoolean("copied", false);
			}
			editor.commit();
		}
		initLangMap();
		LanguageChangeUtils.updateCurrentLocale(new Locale(pref.getString("language", "en")));		
		pd = new ProgressDialog(MainTabActivity.this);
		pd.setMessage("Loading Data");
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		// Tab Initialization
		initialiseTabHost();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		// Fragments and ViewPager Initialization

		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(MainTabActivity.this);
		LanguageChangeUtils.getInstance().addListener(this);

	}

	private void initLangMap() {
          langMap = new HashMap<String,String>();
          langMap.put(langmenuSettings[0],"en");
          langMap.put(langmenuSettings[1],"hi");
          langMap.put(langmenuSettings[2],"mr");
          langMap.put(langmenuSettings[3],"kn");
          /*langMap.put(langmenuSettings[4],"te");
          langMap.put(langmenuSettings[5],"ml");
          langMap.put(langmenuSettings[6],"ta");*/
          if(pref.getBoolean("copied", false)==true)
		        new UpdateDataTask().execute();
	}

	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DbHelper dbHelper = new DbHelper(MainTabActivity.this);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				dbHelper.load_Data(db);
			}
		}).start();

	}

	private void copyData() {
	try {
			// CHECK IS EXISTS OR NOT
			SQLiteDatabase dbe = SQLiteDatabase.openDatabase("/data/data/"
					+ getApplicationContext().getPackageName()
					+ "/databases/metro.db", null, 0);
			dbe.close();
		} catch (Exception e) {
			e.printStackTrace();
			// COPY IF NOT EXISTS
			try {
				AssetManager am = getApplicationContext().getAssets();
				new File("/data/data/com.delhi.metro.sasha/databases").mkdirs();
				OutputStream os = new FileOutputStream(
						"/data/data/com.delhi.metro.sasha/databases/metro.db");
				byte[] b = new byte[1024];
				int r;
				InputStream is = am.open("metro.db");
				while ((r = is.read(b)) != -1) {
					os.write(b, 0, r);
				}

				os.flush();
				os.close();
				is.close();

				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("copied", true);
				editor.commit();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		handle.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pd.dismiss();
			}
		}, 3000);

	}

	// }).start();

	// }

	private SharedPreferences pref;
	
	@Override
	protected void onStart() {
		super.onStart();
		boolean firstlaunch = pref.getBoolean("first_launch", true);
		if(firstlaunch) {
			 Intent i = new Intent();
			 i.setComponent(new ComponentName(this,WelcomePage.class));
			 startActivity(i);
			 finish();
		}
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (LOAD_DATA) {
			loadData(); // insert data to database;
		} else if (!pref.getBoolean("copied", false)) {
			pd.show();
			//copyData(); // copy metro.db to /data/data/package folder
		   new CopyDataTask().execute();
		}
      updateLanguageMenu();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		int pos = this.mViewPager.getCurrentItem();
		this.mTabHost.setCurrentTab(pos);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);

	}

	// Method to add a TabHost
	private static void AddTab(MainTabActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	// Tabs Creation
	private void initialiseTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.getTabWidget().setBackgroundColor(getResources().getColor(R.color.actionbar_background));

		MainTabActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Route").setIndicator(getTabIndicator(mTabHost.getContext(),R.string.tab_route,android.R.drawable.btn_minus)));
		MainTabActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Timings").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_lasttrain,android.R.drawable.btn_minus)));
		// MainTabActivity.AddTab(this, this.mTabHost,
		// this.mTabHost.newTabSpec("Nearby").setIndicator("Nearby Metro"));
		mTabHost.setOnTabChangedListener(this);
	}

	
	private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		   getMenuInflater().inflate(R.menu.settingsmenu, menu);
		   //MenuItem item = menu.findItem(R.id.change_lang);
			//   item.setTitle(getResources().getString(R.string.language_menu));

		return true;
	}



	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
    	switch(id) {
    	case R.id.action_settings:
    		PopupMenu menu = new PopupMenu(getApplicationContext(), findViewById(R.id.action_settings));
    		menu.inflate(R.menu.popupmenu);
            menu.setOnMenuItemClickListener(new PopMenuItemClickListener());
    		menu.show();
    		break;
    	case R.id.change_lang:
    		showLanguageOptionsDialog();
		    break;
		case R.id.menu_item_share:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Please Download this app from following link: https://play.google.com/store/apps/details?id="+"com.delhi.metro.sasha");
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent,"Share this app"));
                break;
		}
    			return super.onOptionsItemSelected(item);
	}
    
    private void updateLanguageMenu() {
		langmenuSettings1 = getResources().getStringArray(R.array.language_settings);
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(langmenuSettings1));
        if(lang.equalsIgnoreCase("en")) {
        	list.remove(0);
        } else if(lang.equalsIgnoreCase("hi")){
        	list.remove(1);
        }else if(lang.equalsIgnoreCase("mr")){
        	list.remove(2);
        }else if(lang.equalsIgnoreCase("kn")){
        	list.remove(3);
        }
        /*else if(lang.equalsIgnoreCase("te")){
        	list.remove(4);
        }else if(lang.equalsIgnoreCase("ml")){
        	list.remove(5);
        }else if(lang.equalsIgnoreCase("ta")) {
        	list.remove(6);
        }*/
        
        
        
        langmenuSettings1 = list.toArray(new String[list.size()]);

    }
    
    
    public class PopMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			int id = item.getItemId();
			switch(id) {
			case R.id.settings:
				Intent i1 = new Intent(MainTabActivity.this,SettingsScreen.class);
				startActivity(i1);
				break;
			case R.id.places:
				Intent i2 = new Intent(MainTabActivity.this,FindNearbyAttractionRouteActivity.class);
				startActivity(i2);
				break;
			}
			return false;
		}
    	
    }
    
    private void showLanguageOptionsDialog() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View customView = factory.inflate(R.layout.settings_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
		builder.setTitle(getResources().getString(R.string.langchange));
		builder.setView(customView);
		ListView listView = (ListView) customView.findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.settings_list_item, langmenuSettings1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new LanguageMenuClickListener());
		dialog = builder.create();
		
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		divider.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
	}
    
    
    
    

    private class LanguageMenuClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String language  =((TextView) view.findViewById(android.R.id.text1)).getText().toString();
			String langCode = langMap.get(language);
			   Locale locale;
    			if(langCode.equalsIgnoreCase("en")) {
    				 locale = new Locale("en");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("hi")) {
    				 locale = new Locale("hi");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("ta")) {
    				 locale = new Locale("ta");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("te")) {
    				 locale = new Locale("te");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("kn")) {
                     locale = new Locale("kn");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("ml")) {
    				 locale = new Locale("ml");
        			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
    			}else if(langCode.equalsIgnoreCase("mr")) {
   				 locale = new Locale("mr");
       			LanguageChangeUtils.getInstance().setLanguageChange(locale,MainTabActivity.this);
   			}
    	
    		if(dialog!=null && dialog.isShowing())
    			dialog.dismiss();
    		
    	
			
		}
    }
		
    private class MenuClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
    		switch(position) {
    		case 0:
    			Intent i1 = new Intent(getApplicationContext(), HelpScreen.class);
    			startActivity(i1);
    			break;
    		case 1:
    			Intent i2 = new Intent(getApplicationContext(),OfflineMapActivity.class);
    			startActivity(i2);
    			break;
    		case 2:
    			Intent i3 = new Intent(getApplicationContext(),ParkingActivity.class);
    			startActivity(i3);
    			break;
    		case 3:
    			Intent i4 = new Intent(getApplicationContext(), AboutScreen.class);
    			startActivity(i4);
    		break;
    		case 4:
    		//	openAppPlayStore(MainTabActivity.this);
    		break;
    		case 5:
    		//	openAppPlayStore(MainTabActivity.this);
        		break;
    		case 6:
    			break;
    		case 7:
    			break;
    		}
    	
    		if(dialog!=null && dialog.isShowing())
    			dialog.dismiss();
    		
		}
    }
    
    
    	


	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataFeeder.clearListMap();
		LanguageChangeUtils.getInstance().clearListners();
		DbUpdateChangeUtils.getInstance().clearListners();
	}

	/* callback to be invoked for inflating route fragment in left pane */

	


	@Override
	public void onLanguageChange(Locale l) {
		// TODO Auto-generated method stub
		 lang = pref.getString("language", "en");
		invalidateOptionsMenu();
		updateLanguageMenu();
		pd.setMessage(getResources().getString(R.string.langchange));
		pd.show();
		this.mTabHost.clearAllTabs();
		MainTabActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Route").setIndicator(getTabIndicator(mTabHost.getContext(),R.string.tab_route,android.R.drawable.btn_minus)));
		MainTabActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Fare").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_fare, android.R.drawable.btn_minus)));
		MainTabActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Timings").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab_lasttrain,android.R.drawable.btn_minus)));
	handle.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			pd.dismiss();
		}
	},2500);

	}

}
