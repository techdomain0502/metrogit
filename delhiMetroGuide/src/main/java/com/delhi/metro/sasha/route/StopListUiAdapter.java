package com.delhi.metro.sasha.route;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.customview.TrackDrawable;
import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.utils.LogUtils;
import com.delhi.metro.sasha.utils.Utils;

public class StopListUiAdapter extends ArrayAdapter<ListViewItem>{ 
	
	private HashMap<String, String> colormap,linemap;
	private Context context;
    private String lang;
    private HashMap<String, String> enToLangMap;
	public StopListUiAdapter(Context context, List<ListViewItem> items,HashMap<String,String> colorMap,HashMap<String,String> lineMap,String language,HashMap<String, String>langMap) {      
		super(context, R.layout.stoplistui_item, items);   
		colormap = colorMap;
		linemap = lineMap;
		this.context = context;
		this.lang = language;
		this.enToLangMap = langMap;
	}   
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}
	
	@Override  
	public View getView(int position, View convertView, ViewGroup parent){      
		
			LayoutInflater inflater = LayoutInflater.from(getContext()); 
			convertView = inflater.inflate(R.layout.stoplistui_item, parent, false);
			TrackDrawable lineDrawable = (TrackDrawable) convertView.findViewById(R.id.ivIcon);
			TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			TextView tvLine = (TextView) convertView.findViewById(R.id.tvLine);
			TextView tvInterchange = (TextView) convertView.findViewById(R.id.tvInterchange);
     		ListViewItem item = getItem(position);
			
	    	ListViewItem nextItem = null,prevItem =null;
		
		   if((position +1) < getCount())
			    nextItem = getItem(position+1);
		
		  if((position-1)>=0 && (position-1)<getCount())
			   prevItem = getItem(position-1);
		
		lineDrawable.setColor(getStopColor(item.title));

		if(lang.equalsIgnoreCase("en")) {
                 tvTitle.setText(item.title);
		} else
		        tvTitle.setText(enToLangMap.get(item.title));                    	 
		
		tvLine.setText(getLineName(item.title));

		if(getStopColor(item.title)==context.getResources().getColor(R.color.grey)){
			tvInterchange.setVisibility(View.VISIBLE);
			tvLine.setVisibility(View.GONE);
			String prevline=null,nextline=null;
			String nextcolor=null,prevcolor=null;
			if(prevItem!=null && nextItem!=null)
			if(prevItem!=null){
			    prevline =  linemap.get(prevItem.title);
			    prevcolor = getColorName(prevItem.title);
		    }
           if(nextItem!=null){			
			nextline =  linemap.get(nextItem.title);
		    nextcolor= getColorName(nextItem.title);
           }


            if(!Utils.isNullorWhiteSpace(nextline) && !Utils.isNullorWhiteSpace(prevline) && !prevline.equalsIgnoreCase(nextline)){
                if(!prevcolor.equalsIgnoreCase(nextcolor) && !nextcolor.equalsIgnoreCase("grey")) {
                 if(lang.equalsIgnoreCase("en")) {
					 	 tvInterchange.setText("Change for " + nextcolor + " " + context.getResources().getString(R.string.line));
				 }
                 else
                	  tvInterchange.setText(nextcolor+" "+context.getResources().getString(R.string.line)+" "+context.getResources().getString(R.string.keliyebadle));	 
                }
                else if(nextcolor.equalsIgnoreCase("grey")) {
                	tvInterchange.setText(context.getResources().getString(R.string.junction));
                }
                else {
                	   if(prevItem!=null && nextItem!=null) {
                		   if((prevItem.title.equalsIgnoreCase("Indraprastha") && nextItem.title.equalsIgnoreCase("Laxmi Nagar"))
                		      ||(prevItem.title.equalsIgnoreCase("Laxmi Nagar") && nextItem.title.equalsIgnoreCase("Indraprastha"))
                		      ||(prevItem.title.equalsIgnoreCase("Indraprastha") && nextItem.title.equalsIgnoreCase("Akshardham")) 
                		      ||(prevItem.title.equalsIgnoreCase("Akshardham") && nextItem.title.equalsIgnoreCase("Indraprastha"))) {
                			        tvInterchange.setText(context.getResources().getString(R.string.junction));
                	      
                		   }
                		   else {
                                tvInterchange.setText(context.getResources().getString(R.string.changehere));
                    	   }
                	   }
                	  
                }
            }
		} else {
			tvInterchange.setVisibility(View.GONE);
			tvLine.setVisibility(View.VISIBLE);
		}		
			
        return convertView;   
	}     

	
	
	private String getColorName(String stop_name){
		String value = colormap.get(stop_name);
		String color ="";

	if(value!=null) {
		switch (value) {
			case "V":
				color = context.getResources().getString(R.string.violet);
				break;
			case "I":
				color = context.getResources().getString(R.string.indigo);
				break;
			case "B":
				color = context.getResources().getString(R.string.blue);
				break;
			case "G":
				color = context.getResources().getString(R.string.green);
				break;
			case "Y":
				color = context.getResources().getString(R.string.yellow);
				break;
			case "O":
				color = context.getResources().getString(R.string.orange);
				break;
			case "R":
				color = context.getResources().getString(R.string.red);
				break;
			case "X":
				color = context.getResources().getString(R.string.grey);
				break;
			case "dB":
				color = "Rapid Metro";
				break;
		}
	}else{
		color="grey";
	}
		return color;
	}
		
	private int getStopColor(String stop_name){
			
		String value = colormap.get(stop_name);

		int color =-1;
	if(value!=null) {
		switch (value) {
			case "V":
				color = context.getResources().getColor(R.color.violet);
				break;
			case "I":
				color = context.getResources().getColor(R.color.indigo);
				break;
			case "B":
				color = context.getResources().getColor(R.color.blue);
				break;
			case "G":
				color = context.getResources().getColor(R.color.green);
				break;
			case "Y":
				color = context.getResources().getColor(R.color.yellow);
				break;
			case "O":
				color = context.getResources().getColor(R.color.orange);
				break;
			case "R":
				color = context.getResources().getColor(R.color.red);
				break;
			case "X":
				color = context.getResources().getColor(R.color.grey);
				break;
			case "dB":
				color = context.getResources().getColor(R.color.dark_blue);
				break;
		}
	} else{
		color = context.getResources().getColor(R.color.grey);
	}
		return color;
	}
		
			
	private String getLineName(String stop_name) {
		String value = colormap.get(stop_name);
		String line ="";

	if(value!=null) {
		switch (value) {
			case "V":
				line = context.getResources().getString(R.string.violet) + " " + context.getResources().getString(R.string.line);
				break;
			case "I":
				line = context.getResources().getString(R.string.indigo) + " " + context.getResources().getString(R.string.line);
				break;
			case "B":
				line = context.getResources().getString(R.string.blue) + " " + context.getResources().getString(R.string.line);
				break;
			case "G":
				line = context.getResources().getString(R.string.green) + " " + context.getResources().getString(R.string.line);
				break;
			case "Y":
				line = context.getResources().getString(R.string.yellow) + " " + context.getResources().getString(R.string.line);
				break;
			case "O":
				line = context.getResources().getString(R.string.orange) + " " + context.getResources().getString(R.string.line);
				break;
			case "R":
				line = context.getResources().getString(R.string.red) + " " + context.getResources().getString(R.string.line);
				break;
			case "dB":
				line = context.getResources().getString(R.string.rapid);
				break;
		}
	}else{
		line="metro line";
	}
		return line;
	}
			
	class NearPlacesOnClickListener implements View.OnClickListener{
		
		private Context mContext;
		private String sourceStation;
		
		NearPlacesOnClickListener(Context mContext, String sourceStation){
			this.mContext=mContext;
			this.sourceStation=sourceStation;
		}
		
		@Override
		public void onClick(View v) {
					// TODO Auto-generated method stub
		/*	Intent i = new Intent(mContext, NearPlacesActivity.class);
			i.putExtra(SOURCE_STATION, sourceStation);
			mContext.startActivity(i);*/
		}		
	}
}
