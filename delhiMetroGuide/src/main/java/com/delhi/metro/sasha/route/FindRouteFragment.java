package com.delhi.metro.sasha.route;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.search.SearchStation;
import com.delhi.metro.sasha.stops.StopListUiActivity;
import com.delhi.metro.sasha.utils.DbUpdateChangeUtils;
import com.delhi.metro.sasha.utils.DbUpdateChangeUtils.onDbUpdateFinishListener;
import com.delhi.metro.sasha.utils.LanguageChangeUtils;
import com.delhi.metro.sasha.utils.LanguageChangeUtils.onLanguageChangeListener;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;

public class FindRouteFragment extends Fragment implements onLanguageChangeListener,onDbUpdateFinishListener {

	private String TAG = FindRouteFragment.class.getSimpleName();
	private ImageButton search1,search2;
	Button routeButton,switchButton;
	private Button spinner1, spinner2;
    FrameLayout routeListContainer;
    ArrayAdapter<String> dataAdapter;
    private List<String> stationList;
    private ScrollView scroll;
	private TextView note;
    private TextView header;
    private String[] stationArray;
    private String lang;
    private HashMap<String, String>langtoEnMap;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       LanguageChangeUtils.getInstance().addListener(this);
       DbUpdateChangeUtils.getInstance().addListener(this);
       pref = getActivity().getSharedPreferences("metro", getActivity().MODE_PRIVATE);
       editor = pref.edit();
    }
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 
   		 lang = pref.getString("language", "en");
   		 container = (ViewGroup) inflater.inflate(R.layout.find_route_layout, null, false);
		 header = (TextView)container.findViewById(R.id.header);
		note = (TextView)container.findViewById(R.id.note);
		 search1 = (ImageButton)container.findViewById(R.id.searchView1);
	     search2 = (ImageButton)container.findViewById(R.id.searchView2);
	     routeButton= (Button)container.findViewById(R.id.routeButton);
	     switchButton= (Button)container.findViewById(R.id.swapButton);
	     scroll = (ScrollView)container.findViewById(R.id.scroll);
	     new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
		      scroll.smoothScrollBy(0, 100);		
			}
		},800);
	     
	     spinner1 = (Button)container.findViewById(R.id.spinner1);
	     spinner2 = (Button)container.findViewById(R.id.spinner2);
	      
	     
	     
	     
          
	     spinner1.setOnClickListener(new ViewClickListener());
	     spinner2.setOnClickListener(new ViewClickListener());
	     search1.setOnClickListener(new ViewClickListener());
	     search2.setOnClickListener(new ViewClickListener());
	     routeButton.setOnClickListener(new ViewClickListener());
	     switchButton.setOnClickListener(new ViewClickListener());
	     spinner1.setText(pref.getString("route_source",""));
	     spinner2.setText(pref.getString("route_destination", ""));
		return container;
	}
	
	private void updateStationListandMap() {
	     if(lang.equalsIgnoreCase("en")) {
	    	 stationList  = DataFeeder.getInstance(getActivity()).getStopNameList();
	     }else if(lang.equalsIgnoreCase("hi")) {
	    	 langtoEnMap =  DataFeeder.getInstance(getActivity()).getHiToEnMap();
	    	 stationList = DataFeeder.getInstance(getActivity()).getHindiStopList();
	     }else if(lang.equalsIgnoreCase("mr")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMrToEnMap();
	    	 stationList = DataFeeder.getInstance(getActivity()).getMarathiStopList();
	     }else if(lang.equalsIgnoreCase("kn")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getKnToEnMap();
	    	 stationList = DataFeeder.getInstance(getActivity()).getKannadaStopList();
	     }
	     /*else if(lang.equalsIgnoreCase("ta")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTaToEnMap();
	    	 stationList = DataFeeder.getInstance(getActivity()).getTamilStopList();
	     }else if(lang.equalsIgnoreCase("te")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTeToEnMap();
	    	 stationList = DataFeeder.getInstance(getActivity()).getTeluguStopList();
	     }else if(lang.equalsIgnoreCase("ml")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMlToEnMap();
	         stationList = DataFeeder.getInstance(getActivity()).getMalyalamStopList();
	     }
*/
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        int position = 0;
        if(data!=null)
        	position = data.getIntExtra("index", 0);
		if (stationList != null && requestCode == 100 && resultCode == 102 && position!=-1) {
	   spinner1.setText(stationList.get(position));
        } else if (stationList != null && requestCode == 101 && resultCode == 102 && position!=-1) {
	   spinner2.setText(stationList.get(position));
        }
	
	}

	public class ViewClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
         int id = v.getId();
         switch(id) {
         case R.id.searchView1:
             Intent i1 = new Intent(getActivity(), SearchStation.class);
             i1.putExtra("code", 100);
             startActivityForResult(i1, 100);
        	 break;
         case R.id.searchView2:
        	 Intent i2 = new Intent(getActivity(), SearchStation.class);
        	 i2.putExtra("code", 101);
             startActivityForResult(i2, 101);
        	 break;
         case R.id.routeButton:
        	 String source = spinner1.getText().toString();
        	 String dest = spinner2.getText().toString();
        	 if(Utils.isNullorWhiteSpace(source)  && Utils.isNullorWhiteSpace(dest)){
        		 Toast.makeText(getActivity(), getResources().getString(R.string.sde), Toast.LENGTH_SHORT).show();
            	 return;
        	 }
        	 else if(!Utils.isNullorWhiteSpace(source)  && !Utils.isNullorWhiteSpace(dest) && source.equalsIgnoreCase(dest)) {
            	 Toast.makeText(getActivity(), getResources().getString(R.string.sds), Toast.LENGTH_SHORT).show();
            	 return;
             }
             else if(Utils.isNullorWhiteSpace(source)){
            	 Toast.makeText(getActivity(), getResources().getString(R.string.cs), Toast.LENGTH_SHORT).show();
                return;
             }
             else if(Utils.isNullorWhiteSpace(dest)){
            	 Toast.makeText(getActivity(), getResources().getString(R.string.cd), Toast.LENGTH_SHORT).show();
                 return;
             }
        	 
        	 String src =spinner1.getText().toString();
        	 String des =spinner2.getText().toString();
        	 if(!lang.equalsIgnoreCase("en")) {
        		 src = langtoEnMap.get(src);
        		 des =langtoEnMap.get(des);
        	 }
			
              Intent i = new Intent(getActivity(),StopListUiActivity.class);
        	 Bundle b = new Bundle();
        	 b.putString("source", src);
        	 b.putString("destination", des);
        	 i.putExtras(b);
        	 startActivity(i);
        	 break;
		 case R.id.spinner1:
			 showListDialog(R.string.choose_source_station, (Button)v);
			 break;
		 case R.id.spinner2:
			 showListDialog(R.string.choose_destination_station, (Button)v);
			 break;
		 case R.id.swapButton:
			 String str1 = spinner1.getText().toString();
			 String str2 = spinner2.getText().toString();
			 spinner1.setText(str2);
			 spinner2.setText(str1);
			 break;
         }
		}
	}
	
	private void showListDialog(int resourceId,final Button view) {
		String title = getResources().getString(resourceId);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
		AlertDialog dialog;
		builder.setTitle(title);
		if(stationList != null) {
			if (lang.trim().equalsIgnoreCase("en")) {
				Collections.sort(stationList);
				stationArray = new String[stationList.size()];
			} else {
				stationArray = new String[stationList.size()];
			}

			builder.setSingleChoiceItems(stationList.toArray(stationArray), 0, new DialogInterface.OnClickListener() {
				public void onClick(final DialogInterface dialog, int which) {
					view.setText(stationArray[which]);
					if (view.getId() == R.id.spinner1)
						editor.putString("route_source", stationArray[which]);
					else if (view.getId() == R.id.spinner2)
						editor.putString("route_destination", stationArray[which]);
					editor.commit();

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
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
             header.setText(getActivity().getResources().getString(R.string.route_details));
	         routeButton.setText(getActivity().getResources().getString(R.string.show_route));
	         switchButton.setText(getActivity().getResources().getString(R.string.switch_button));
		     note.setText(getResources().getString(R.string.smartnote_fare));
	         spinner1.setText("");
	         spinner2.setText("");
	         spinner1.setHint(getActivity().getResources().getString(R.string.source_station));
	         spinner2.setHint(getActivity().getResources().getString(R.string.destination_station));
	 		 lang = pref.getString("language", "en");
	 		 editor.putString("route_source","");
	 		 editor.putString("route_destination","");
	 		 editor.commit();
	 		 updateStationListandMap();
	}

	@Override
	public void onDbUpdate() {
		   updateStationListandMap();	     
		     
		     if(lang.equalsIgnoreCase("en"))
	    	     Collections.sort(stationList);

	}
	
}
