package com.delhi.metro.sasha.stops;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delhi.metro.sasha.R;

public class StopAdapter extends BaseAdapter {
	// Declare Variables
		Context mContext;
		LayoutInflater inflater;
		private List<String> stationList = null;
		private ArrayList<String> arraylist;
		private String filter;
		
		public StopAdapter(Context context, List<String> stoplist) {
			mContext = context;
			this.stationList = stoplist;
			inflater = LayoutInflater.from(mContext);
			this.arraylist = new ArrayList<String>();
			this.arraylist.addAll(stoplist);
		}
		
		public class ViewHolder {
			TextView country;
		}
		
		
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return stationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return stationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
		}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.suggestion_list_item, null);
			// Locate the TextViews in listview_item.xml
			holder.country = (TextView) view.findViewById(R.id.suggestion_list_item);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		Spannable text = new SpannableString(stationList.get(position));
		if(filter!=null) {
		int start = stationList.get(position).toLowerCase().indexOf(filter.toString().toLowerCase());
		int len = filter.length();
		int end = start + len ;
		if(start>-1 && end >-1) 
			text.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.darkred)), start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		holder.country.setText(text);
		
		return view;
	}

	
	
	public void filter(String charText) {
		filter  = charText;
		charText = charText.toLowerCase(Locale.getDefault());
		stationList.clear();
		if (charText.length() == 0) {
			stationList.addAll(arraylist);
		} 
		else 
		{
			for (String wp : arraylist) 
			{
				if (wp.toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					stationList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}
}
