package com.delhi.metro.sasha.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delhi.metro.sasha.R;

public class FlipAdapter extends BaseAdapter{
	
	public interface Callback{
		public void onPageRequested(int page);
	}
	
	
	private LayoutInflater inflater;
	private Callback callback;
	
	private List<String> items0 = new ArrayList<String>();
	private List<String> items1 = new ArrayList<String>();
	private List<String> items2 = new ArrayList<String>();
	private List<String> items3 = new ArrayList<String>();
    	private Context c; 
	public FlipAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		 c = context;
		 
		 
	 String arr[] =  context.getResources().getStringArray(R.array.help_smartcard0);
	 for(int i = 0 ; i<arr.length ; i++){
			items0.add(arr[i]);
		}
	 
	 arr =  context.getResources().getStringArray(R.array.help_smartcard1);
	 for(int i = 0 ; i<arr.length ; i++){
			items1.add(arr[i]);
		} 
	
	 
	 
	 arr =  context.getResources().getStringArray(R.array.help_smartcard2);
	 for(int i = 0 ; i<arr.length ; i++){
			items2.add(arr[i]);
		} 
	 
	 arr =  context.getResources().getStringArray(R.array.helpline);
	 for(int i = 0 ; i<arr.length ; i++){
			items3.add(arr[i]);
		} 
	 
	 
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.page, parent, false);
			
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.header = (TextView) convertView.findViewById(R.id.header);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		StringBuilder bi = new StringBuilder();
		CharSequence[] cc = null;
		int count = -1;
		if(position == 0) {
			
		Iterator i = items0.iterator();
		cc = new CharSequence[items0.size()];
		
		Spannable str ;
		while(i.hasNext()) {
			String arr[] = ((String)i.next()).split("-");
			str = new SpannableString(arr[0]);
			str.setSpan(new BackgroundColorSpan(Color.RED),0,arr[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		cc[++count] =	TextUtils.concat(str,"-",arr[1]);
		}
		Spannable str0 = new SpannableString(c.getResources().getString(R.string.helpheader0));
		str0.setSpan(new BackgroundColorSpan(Color.RED),0,str0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.header.setText(str0);
		
	   }
	   else if(position == 1) {
			Iterator i = items1.iterator();
			cc = new CharSequence[items1.size()];
			Spannable str ,str1;
			while(i.hasNext()) {
				String arr[] = ((String)i.next()).split("-");
				str = new SpannableString(arr[0]);
				str.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.tab_back)),0,arr[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				str1 = new SpannableString(arr[1]);
				str1.setSpan(new BackgroundColorSpan(Color.YELLOW),0,arr[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			cc[++count] =	TextUtils.concat(str,"-",arr[1]);
			}
			Spannable str0 = new SpannableString(c.getResources().getString(R.string.helpheader1));
			str0.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.tab_back)),0,str0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.header.setText(str0);
			
		   }
	   else if(position == 2) {
			Iterator i = items2.iterator();
			cc = new CharSequence[items2.size()];
			Spannable str ;
			while(i.hasNext()) {
				String arr[] = ((String)i.next()).split("-");
				str = new SpannableString(arr[0]);
				str.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.red)),0,arr[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			cc[++count] =	TextUtils.concat(str,"-",arr[1]);
			}
			Spannable str0 = new SpannableString(c.getResources().getString(R.string.helpheader2));
			str0.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.red)),0,str0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.header.setText(str0);
		   }
	   else if (position == 3) {
			Iterator i = items3.iterator();
			cc = new CharSequence[items3.size()];
			Spannable str,str1 ;
			while(i.hasNext()) {
				String arr[] = ((String)i.next()).split("-");
				str = new SpannableString(arr[0]);
				str.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.tab_back)),0,arr[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				str1 = new SpannableString(arr[1]);
				str1.setSpan(new BackgroundColorSpan(Color.YELLOW),0,arr[1].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
			cc[++count] =	TextUtils.concat(str,"-",arr[1]);
			}
			Spannable str0 = new SpannableString(c.getResources().getString(R.string.helpheader3));
			str0.setSpan(new BackgroundColorSpan(c.getResources().getColor(R.color.tab_back)),0,str0.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.header.setText(str0);
	   }
	   

		if(position ==0 )
		    holder.text.setText(TextUtils.concat(cc[0],cc[1],cc[2],cc[3]));
		else if(position ==1)
			holder.text.setText(TextUtils.concat(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7]));
		else if(position ==2)
			holder.text.setText(TextUtils.concat(cc[0],cc[1],cc[2],cc[3]));
	   else if(position ==3)
			holder.text.setText(TextUtils.concat(cc[0],cc[1],cc[2],cc[3],cc[4],cc[5],cc[6],cc[7],cc[8],cc[9],cc[10],cc[11],cc[12]));
		
		return convertView;
	}


	
	static class ViewHolder{
		TextView header;
		TextView text;
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}



}
