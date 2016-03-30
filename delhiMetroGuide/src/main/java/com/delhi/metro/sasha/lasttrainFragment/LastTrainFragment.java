package com.delhi.metro.sasha.lasttrainFragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.db.DbHelper;
import com.delhi.metro.sasha.stops.StopAdapter;
import com.delhi.metro.sasha.utils.LanguageChangeUtils;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;
import com.delhi.metro.sasha.utils.LanguageChangeUtils.onLanguageChangeListener;

public class LastTrainFragment extends Fragment implements onLanguageChangeListener{
	private List<String> lineList;
	private ImageButton search;
	private Button spinner;
    private TextView first,last,header;
	private ScrollView scroll;
	
    private String[] stationArray;
    private String lang;
    private HashMap<String, String> langtoEnMap;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
    	LanguageChangeUtils.getInstance().addListener(this);
        pref = getActivity().getSharedPreferences("metro", getActivity().MODE_PRIVATE);
        editor = pref.edit();
    
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lang = pref.getString("language", "en");
    	View view = inflater.inflate(R.layout.last_train_layout, null, false);
		search = (ImageButton)view.findViewById(R.id.searchView1);
		spinner = (Button)view.findViewById(R.id.spinner1);
		header = (TextView)view.findViewById(R.id.header);
		first = (TextView)view.findViewById(R.id.first);
		last = (TextView)view.findViewById(R.id.last);
		spinner.setOnClickListener(new ViewClickListener());
		search.setOnClickListener(new ViewClickListener());
	    
	     
		scroll = (ScrollView)container.findViewById(R.id.scroll);
	     new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
		      scroll.smoothScrollBy(0, 100);		
			}
		},800);
	
	     updateStationListandMap();	  
	     
	     spinner.setText(pref.getString("last_line",""));
         if(!Utils.isNullorWhiteSpace(pref.getString("last_line","")))
        	 calculateFirstLastTime(pref.getString("last_line",""));
	     return view;

	}
	
    
    private void updateStationListandMap() {
	     if(lang.equalsIgnoreCase("en")) {
	    	 lineList  = DataFeeder.getInstance(getActivity()).getLineNameList();
	     }else if(lang.equalsIgnoreCase("hi")) {
	    	 langtoEnMap =  DataFeeder.getInstance(getActivity()).getHiToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getHindiLinelist();
	     }else if(lang.equalsIgnoreCase("mr")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMrToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getMarathiLineList();
	     }else if(lang.equalsIgnoreCase("kn")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getKnToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getKannadaLineList();
	     }
	     /*
	     else if(lang.equalsIgnoreCase("ta")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTaToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getTamilLineList();
	     }else if(lang.equalsIgnoreCase("te")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTeToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getTeluguLinelist();
	     }else if(lang.equalsIgnoreCase("ml")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMlToEnMap();
	    	 lineList = DataFeeder.getInstance(getActivity()).getMalyalamLinelist();
	     }
*/
	}    
	
public class ViewClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
         int id = v.getId();
         switch(id) {
		 case R.id.spinner1:
			 showListDialog("Choose  Station", (Button)v);
			 break;
		
		}
	}
 }

private void calculateFirstLastTime(String line){
	DbHelper dbHelper = new DbHelper(getActivity());
	String tokens[] = line.split("->");
	if(tokens.length==2 && !lang.equalsIgnoreCase("en")) {
		tokens[0]=langtoEnMap.get(tokens[0].trim());
		tokens[1]=langtoEnMap.get(tokens[1].trim());
	}
	
	
	String arr[] = null; 
	if(tokens.length==2 && tokens[0]!=null && tokens[1]!=null)		
	   arr =  dbHelper.getFirstLastTime(tokens[0].trim(), tokens[1].trim());
	first.setVisibility(View.VISIBLE);
	last.setVisibility(View.VISIBLE);
	
	if(arr!=null && arr.length==2 &&arr[0]!=null && !arr[0].equalsIgnoreCase("NA"))
	  first.setText(getResources().getString(R.string.first_time_code)+": "+formatTime(arr[0]));
	else
	  first.setText(getResources().getString(R.string.first_time_code)+": NA");
	
	if(arr!=null && arr.length==2 && arr[1]!=null && !arr[1].equalsIgnoreCase("NA"))
	  last.setText(getResources().getString(R.string.last_time_code)+": "+formatTime(arr[1]));
	else
	  last.setText(getResources().getString(R.string.last_time_code)+": NA");
	
}
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
    int position = -1;
    
    if(data!=null)
    	position = data.getIntExtra("index", 0);
	if (requestCode == 1000 && resultCode == 102) {
    	spinner.setText(lineList.get(position));
    	calculateFirstLastTime(lineList.get(position));
    } 

}


public String formatTime(String time){
	String ampm = "am";
	String arr[]=  time.split(":");
	int h = Integer.valueOf(arr[0]);
	int m = Integer.valueOf(arr[1]);
	if(h>12){
		h = h - 12;
		ampm ="pm";
	}
	String result = null;
	if(m!=0){
		if(m>9)
	      result= h+":"+m+" "+ampm;
		else
		  result= h+":0"+m+" "+ampm;	
	}
	else
	result = h+" "+ampm;	
	return result;
}


private void showListDialog(String title,final Button view) {
	AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
	AlertDialog dialog;
	builder.setTitle(title);
	if(lineList != null) {
		if (lang.trim().equalsIgnoreCase("en")) {
			Collections.sort(lineList);
			stationArray = new String[lineList.size()];
		} else {
			stationArray = new String[lineList.size()];
		}

		builder.setSingleChoiceItems(lineList.toArray(stationArray), 0, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, int which) {
				view.setText(stationArray[which]);

				if (view.getId() == R.id.spinner1) {
					editor.putString("last_line", stationArray[which]);
					editor.commit();
				}

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						calculateFirstLastTime(spinner.getText().toString());
						dialog.dismiss();
					}
				}, 100);

			}
		});
		dialog = builder.create();
		dialog.show();
		int dividerId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = dialog.findViewById(dividerId);
		divider.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
	}
}
@Override
public void onLanguageChange(Locale l) {
	  header.setText(getActivity().getResources().getString(R.string.time_details));
      first.setText(getActivity().getResources().getString(R.string.first_time));
      last.setText(getActivity().getResources().getString(R.string.last_time));
      spinner.setText("");
      spinner.setHint(getActivity().getResources().getString(R.string.last_source_station));
      lang = pref.getString("language", "en");
		 editor.putString("last_line","");
		 editor.commit();
 		 updateStationListandMap();
  }
}
