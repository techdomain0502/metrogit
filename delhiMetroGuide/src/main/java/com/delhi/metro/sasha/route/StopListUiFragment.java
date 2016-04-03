package com.delhi.metro.sasha.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.fare.CalculateFare;
import com.delhi.metro.sasha.googleapis.FollowRouteActivity;
import com.delhi.metro.sasha.stops.StopListUiActivity;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;

public class StopListUiFragment extends ListFragment {
	
	private String src,dest;
	ArrayList<Vertex> nodes;
	ArrayList<Edge> edges;
	HashMap<String, Vertex> stopMap;
	ArrayList<ListViewItem> itemlist;
	StopListUiAdapter adapter;
	HashMap<String, String> colorMap,lineMap;
	private StringBuilder builder;
	private String noOfInterchange;
	private String journeyTime;
	private String noOfStations;
	private String length;
	float time;
	private float routeLength;
	private float fare;
	private int stationCount;
    private SharedPreferences pref;
    private String lang;
    private HashMap<String, String> enToLangMap,langtoEnMap;
    private CalculatePath path_fare_calculator;
    private boolean isRapidRoute;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pref = getActivity().getSharedPreferences("metro", getActivity().MODE_PRIVATE);
		lang = pref.getString("language", "en");
        updateStationListandMap();
		setHasOptionsMenu(true);
		Bundle bundle = this.getArguments();
		if(bundle!=null){
			src = (String)bundle.getString("source");
			dest = (String)bundle.getString("destination");
		}
		
		DataFeeder instance = DataFeeder.getInstance(getActivity());
		nodes = instance.getNodesList();
		edges = instance.getEdgeList();
		stopMap = instance.getStopMap();
		colorMap = instance.getStopColorMap();
		lineMap = instance.getStopLineMap();
		builder = new StringBuilder();
		builder.append("Line Color Codes:(V(violet),I(indigo),B(blue),G(green),Y(yellow),O(orange),Red(red)))\n");
		builder.append("Path from "+src+" to "+dest+" \n" );
		path_fare_calculator  =new CalculatePath(getActivity());
		ArrayList<Vertex> list = path_fare_calculator.initialize(nodes,edges,src,dest,stopMap);

		itemlist = new ArrayList<ListViewItem>();
		if(src!=null && dest!=null && !src.equalsIgnoreCase(dest) && list!= null && !list.isEmpty()){
		Iterator<Vertex> iterator = list.iterator();
		while(iterator.hasNext()){
			Vertex v = iterator.next();
			ListViewItem item = new ListViewItem(getResources().getDrawable(R.drawable.ic_launcher),v.getName()	,"");
			itemlist.add(item);
		}		

		adapter = new StopListUiAdapter(getActivity(), itemlist,colorMap,lineMap,lang,enToLangMap);

		noOfStations = String.valueOf(list.size() - 1);

		String interchangePlaceHolder =getActivity().getResources().getString(R.string.junction);
		for(int position=0;position<list.size();position++){
			Vertex current = list.get(position);
			Vertex next = null,previous = null;
			if((position+1)<list.size())
				next = list.get(position+1);
			if((position-1)>=0 && (position-1)<list.size())
				previous = list.get(position-1);
			
			if(colorMap.get(current.getName()).equalsIgnoreCase("X")){
				String prevline=null,nextline=null;
				String nextcolor=null,prevcolor=null;
				if(previous!=null){
					prevline = lineMap.get(previous.getName());
					prevcolor= getColorName(previous.getName());
				}
				
				if(next!=null){
					nextline = lineMap.get(next.getName());
					nextcolor = getColorName(next.getName());
				}

				if(!Utils.isNullorWhiteSpace(nextline) && !Utils.isNullorWhiteSpace(prevline) && !prevline.equalsIgnoreCase(nextline)){
		                if(!prevcolor.equalsIgnoreCase(nextcolor) && !nextcolor.equalsIgnoreCase("grey")){
		            	  interchangePlaceHolder = "(Change for "+nextcolor+" line)";
		            	  stationCount++;
		                }
		                else if(nextcolor.equalsIgnoreCase("grey")){
		                  interchangePlaceHolder = "(Junction)";
		                }
		                else{
		                	   if(previous!=null && next!=null) {
		                		   if((previous.getName().equalsIgnoreCase("Indraprastha") && next.getName().equalsIgnoreCase("Laxmi Nagar")) 
		                		      ||(previous.getName().equalsIgnoreCase("Laxmi Nagar") && next.getName().equalsIgnoreCase("Indraprastha"))
		                		      ||(previous.getName().equalsIgnoreCase("Indraprastha") && next.getName().equalsIgnoreCase("Akshardham")) 
		                		      ||(previous.getName().equalsIgnoreCase("Akshardham") && next.getName().equalsIgnoreCase("Indraprastha"))) {
		                			   interchangePlaceHolder = "(Junction)";
		                		   }
		                		   else {
		                    		    interchangePlaceHolder = "(Change here)";
		      		                    stationCount++;
		                    	   }
		                	   }

		                }
		            }
				 builder.append(interchangePlaceHolder + "-"+current.getName());
			}
			 else{
				   builder.append(colorMap.get(current.getName())+"-"+current.getName());
			 }
			
			builder.append("\n");			
		}
		
		noOfInterchange = String.valueOf(stationCount);
		}

	}
	
	
	private void updateStationListandMap() {
	     if(lang.equalsIgnoreCase("en")) {
	     }else if(lang.equalsIgnoreCase("hi")) {
	    	 enToLangMap =  DataFeeder.getInstance(getActivity()).getenToHiMap();
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getHiToEnMap();
	     }else if(lang.equalsIgnoreCase("mr")) {
	    	 enToLangMap = DataFeeder.getInstance(getActivity()).getenToMrMap();
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMrToEnMap();
	     }else if(lang.equalsIgnoreCase("kn")) {
	    	 enToLangMap = DataFeeder.getInstance(getActivity()).getenToKnMap();
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getKnToEnMap();
	     }
	     
	     /*else if(lang.equalsIgnoreCase("ta")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTaToEnMap();
	     }else if(lang.equalsIgnoreCase("te")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getTeToEnMap();
	     }else if(lang.equalsIgnoreCase("ml")) {
	    	 langtoEnMap = DataFeeder.getInstance(getActivity()).getMlToEnMap();
	     }
*/
	}    

	
	private String formatTime(float time) {
		float h=0,m=0,s=0;

	  if(time>1) {
		  time = time * 60;
		  h =  time /60;
		  m = time%60;
	  }else{
		  m = time * 60;
	  }
		if(time>1){
			return (int)h+" hr"+" "+(int)m+" min";
		}else{
			return (int)m+" min";
		}
	}




	private String getColorName(String stop_name){
		String value = colorMap.get(stop_name);
		String color ="";
				
		switch(value){
				case "V":
					color = getActivity().getResources().getString(R.string.violet);
					break;
				case "I":
					color = getActivity().getResources().getString(R.string.indigo);
					break;
				case "B":
					color = getActivity().getResources().getString(R.string.blue);
					break;
				case "G":
					color = getActivity().getResources().getString(R.string.green);
					break;
				case "Y":
					color = getActivity().getResources().getString(R.string.yellow);
					break;
				case "O":
					color = getActivity().getResources().getString(R.string.orange);
					break;
				case "R":
					color = getActivity().getResources().getString(R.string.red);
					break;
				case "X":
					color = getActivity().getResources().getString(R.string.grey);
			    case "dB":
				    color=  "Change for Rapid Metro";
		}			
		
		return color;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = (ViewGroup) inflater.inflate(android.R.layout.list_content, container, false);
		ListView list = (ListView)v.findViewById(android.R.id.list);
		list.setDivider(getResources().getDrawable(R.drawable.divider));
		list.setDividerHeight(2);
		if(!src.equalsIgnoreCase(dest) && adapter != null)
		    setListAdapter(adapter);
		else
			Toast.makeText(getActivity(),"No such route exists :(",Toast.LENGTH_LONG).show();
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof StopListUiActivity) {
		((StopListUiActivity)getActivity()).setSharePathString(builder.toString());
		new UpdateHeaderTask().execute();
		}else if(getActivity() instanceof FollowRouteActivity){
			((FollowRouteActivity)getActivity()).setSharePathString(builder.toString());
		}
	}
	
	public class UpdateHeaderTask extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			if(!Utils.isNullorWhiteSpace(src) && !Utils.isNullorWhiteSpace(dest)) {
				routeLength = path_fare_calculator.getroute_length();
				 length = String.format("%.02f", routeLength);

                isRapidRoute = path_fare_calculator.isRapidRoute();
				fare = new CalculateFare(getActivity()).getFare(Float.valueOf(length));
				if(isRapidRoute)
					fare+=20;  // checking if route contains rapid metro node, then add flat 20Rs.


				time = (float) (routeLength/32f);  //assuming 32kph is average speed
				journeyTime = formatTime(time);
				LogUtils.LOGD("timecheck","routelengh="+routeLength+ " time="+time);

			}
			return String.valueOf(fare);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		   if(getActivity() instanceof StopListUiActivity) {
			   Message msg = Message.obtain();
			   Bundle  b = new Bundle();
			   b.putString("station", noOfStations);
			   b.putString("interchange", noOfInterchange);
			   b.putString("time", String.valueOf(journeyTime));
			   b.putString("distance",String.valueOf(length+" km"));
			   b.putFloat("fare", fare);
			   msg.setData(b);
			   ((StopListUiActivity)getActivity()).getHandler().sendMessage(msg);
		   }
		}
	}
	
}
