package com.delhi.metro.sasha.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.stops.StopAdapter;
import com.delhi.metro.sasha.utils.LanguageChangeUtils.onLanguageChangeListener;

public class SearchStation extends Activity {
	
	private EditText edit;
    private List<String> stationList,temp;
    private StopAdapter adapter;
    private ListView suggestion;
    private SharedPreferences pref;
    private String lang;
    private HashMap<String, String> enToLangMap;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_station_layout);

		ActionBar ab = getActionBar();
		if(ab!=null) {
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		}
	
		
		pref = getSharedPreferences("metro", MODE_PRIVATE);
		lang = pref.getString("language", "en");
		
		Intent i = getIntent();
		int code = i.getExtras().getInt("code");
		edit = (EditText)findViewById(R.id.edit);
		edit.setHint(getResources().getText(R.string.enter_station_hint));
	    suggestion = (ListView)findViewById(R.id.list);
        edit.addTextChangedListener(new EditTextWatcher());
        
        if(code == 100  || code == 101) 
        	updateStationListandMap();
	
        if(lang.equalsIgnoreCase("en"))
            Collections.sort(stationList); 
        
        temp = new ArrayList<String>();
        temp.clear();
        
            temp.addAll(stationList);
	    
        adapter = new StopAdapter(SearchStation.this, temp);
	    suggestion.setAdapter(adapter);
	    suggestion.setOnItemClickListener(new ListItemClickListener());
	}
	
	
	private void updateStationListandMap() {
	     if(lang.equalsIgnoreCase("en")) {
	    	 stationList  = DataFeeder.getInstance(this).getStopNameList();
	     }else if(lang.equalsIgnoreCase("hi")) {
	    	 stationList = DataFeeder.getInstance(this).getHindiStopList();
	     }else if(lang.equalsIgnoreCase("mr")) {
	    	 stationList = DataFeeder.getInstance(this).getMarathiStopList();
	     }else if(lang.equalsIgnoreCase("kn")) {
	    	 stationList = DataFeeder.getInstance(this).getKannadaStopList();
	     }
	     /*else if(lang.equalsIgnoreCase("ta")) {
	    	 stationList = DataFeeder.getInstance(this).getTamilStopList();
	     }else if(lang.equalsIgnoreCase("te")) {
	    	 stationList = DataFeeder.getInstance(this).getTeluguStopList();
	     }else if(lang.equalsIgnoreCase("ml")) {
	         stationList = DataFeeder.getInstance(this).getMalyalamStopList();
	     }
*/
	}


	public class ListItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			 String str = temp.get(position);
        	 int index = 0;
        	 
        		index =  stationList.indexOf(str);
        	 
             Intent i = new Intent();
             i.putExtra("index", index);
             setResult(102, i);
             hideKeyboard();
             finish();
		}
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	public class EditTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String text = "";
		    text = edit.getText().toString().toLowerCase(Locale.getDefault());
			adapter.filter(text);      			
		}

		@Override
		public void afterTextChanged(Editable s) {
		
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
	
}
