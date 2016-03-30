package com.delhi.metro.sasha.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;


import com.delhi.metro.sasha.BuildConfig;
import com.delhi.metro.sasha.R;
import com.delhi.metro.sasha.db.DbHelper.line;
import com.delhi.metro.sasha.gui.MetroApplication;
import com.delhi.metro.sasha.route.Edge;
import com.delhi.metro.sasha.route.Pair;
import com.delhi.metro.sasha.route.Vertex;
import com.delhi.metro.sasha.utils.LogUtils;

public class DataFeeder {
	private String TAG = DataFeeder.class.getSimpleName();
	private static HashMap<String, String> stopname_colorid_map,stopname_linename_map;
	private static HashMap<String, Vertex> stopMap;
	private static ArrayList<Vertex> nodes ;
	private static ArrayList<Edge> edges ;
	private static ArrayList<String> stopNamelist;
	private static ArrayList<line> lastStopNameList ;
	private static ArrayList<String> lineNamelist,hindiLineNameList,tamilLineNameList,teluguLineNameList,kannadaLineNameList,malyalamLineNameList,marathiLineNameList;
	private static Context mContext;
	private static  DbHelper dbhelper;
    private static DataFeeder mInstance;
    private static Map<Pair,Float> map = new HashMap<Pair,Float>();

	/*Translation Map*/
    private static HashMap<String, String> enTohiMap,hiToEnMap,enToTaMap,TaToEnMap,enToTeMap,TeToenMap,enToKnMap,KnToenMap,enToMlMap,MlToenMap,enToMrMap,MrToenMap;
	private DataFeeder() {
		dbhelper = new DbHelper(mContext);
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		stopMap = new HashMap<String, Vertex>();
		stopNamelist = new ArrayList<String>();
		lastStopNameList = new ArrayList<line>();
		lineNamelist = new ArrayList<String>();
		hindiLineNameList = new ArrayList<String>();
		tamilLineNameList = new ArrayList<String>();
		teluguLineNameList = new ArrayList<String>();
		kannadaLineNameList = new ArrayList<String>();
		malyalamLineNameList = new ArrayList<String>();
		marathiLineNameList = new ArrayList<String>();
		
		
		enTohiMap = new HashMap<String,String>();
		hiToEnMap = new HashMap<String,String>();
		enToTaMap = new HashMap<String,String>();
		TaToEnMap= new HashMap<String,String>();
		enToTeMap = new HashMap<String,String>();
		TeToenMap= new HashMap<String,String>();
		KnToenMap= new HashMap<String,String>();
		enToKnMap= new HashMap<String,String>();;
		MlToenMap= new HashMap<String,String>();
		enToMlMap= new HashMap<String,String>();
		MrToenMap= new HashMap<String,String>();
		enToMrMap= new HashMap<String,String>();
		
		//initData();
	}
	
	
	public void initialize(Context c) {
		mContext = c;
		initData();
	}
	
	public static DataFeeder getInstance(Context c) {
		mContext =c ;
		if(mInstance == null) {
			mInstance = new DataFeeder();
		}
		
		return mInstance;
	}




	private void initData() {
		if(dbhelper!=null) {
		    stopname_colorid_map = dbhelper.getStopsColorMap(nodes);
		    stopname_linename_map = dbhelper.getStopsLineMap();
		    edges = dbhelper.getEdgeList();
			initPair_lengthMap(edges);
			stopNamelist = dbhelper.getStopsList(hiToEnMap,enTohiMap,/*TaToEnMap,enToTaMap,TeToenMap,enToTeMap,*/KnToenMap,enToKnMap,/*MlToenMap,enToMlMap,*/MrToenMap,enToMrMap);
		    lastStopNameList = dbhelper.getLastStopList(lineNamelist,hindiLineNameList/*,tamilLineNameList,teluguLineNameList*/,kannadaLineNameList/*,malyalamLineNameList*/,marathiLineNameList,enTohiMap/*,enToTaMap,enToTeMap*/,enToKnMap/*,enToMlMap*/,enToMrMap);
		}
	}

	private void initPair_lengthMap(List<Edge> list) {
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			Edge e = (Edge)iterator.next();
			Pair p = new Pair(e.getSource(),e.getDestination());
			map.put(p,e.getLength());
		}
	}

	public Map<Pair,Float> getPairLengthMap(){return map;}
	public HashMap<String, String> getenToHiMap(){
	   return enTohiMap;	
	}
	
	public HashMap<String, String> getHiToEnMap(){
		return hiToEnMap;
	}

	public HashMap<String, String> getenToTaMap(){
		   return enToTaMap;	
		}
		
		public HashMap<String, String> getTaToEnMap(){
			return TaToEnMap;
		}
		
	
		public HashMap<String, String> getenToTeMap(){
			   return enToTeMap;	
			}
			
			public HashMap<String, String> getTeToEnMap(){
				return TeToenMap;
			}
			
			
			public HashMap<String, String> getenToKnMap(){
				   return enToKnMap;	
				}
				
				public HashMap<String, String> getKnToEnMap(){
					return KnToenMap;
				}
				
				
				public HashMap<String, String> getenToMlMap(){
					   return enToMlMap;	
					}
					
					public HashMap<String, String> getMlToEnMap(){
						return MlToenMap;
					}
					
					public HashMap<String, String> getenToMrMap(){
						   return enToMrMap;	
						}
						
						public HashMap<String, String> getMrToEnMap(){
							return MrToenMap;
						}
						
					
		
	
	public List<String> getHindiStopList(){
		String []stop_hi = mContext.getResources().getStringArray(R.array.stoplist_hi);
		List<String> list = Arrays.asList(stop_hi);
		return list;
	}
	
	
/*
	public List<String>getTamilStopList(){
		String []stop_ta = mContext.getResources().getStringArray(R.array.stoplist_ta);
		List<String> list = Arrays.asList(stop_ta);
		return list;
	}
	
	public List<String> getTeluguStopList(){
		String []stop_hi = mContext.getResources().getStringArray(R.array.stoplist_te);
		List<String> list = Arrays.asList(stop_hi);
		return list;
	}
*/

	
	public List<String>getKannadaStopList(){
		String []stop_ta = mContext.getResources().getStringArray(R.array.stoplist_kn);
		List<String> list = Arrays.asList(stop_ta);
		return list;
	}


	public List<String>getMarathiStopList(){
		String []stop_ta = mContext.getResources().getStringArray(R.array.stoplist_mr);
		List<String> list = Arrays.asList(stop_ta);
		return list;
	}
	
	
	public ArrayList<String> getLineNameList(){
		return lineNamelist;
	}
	
	public  ArrayList<Vertex> getNodesList() {
		return nodes;
	}

	public  ArrayList<Edge> getEdgeList() {
		return edges;
	}

	public  HashMap<String, Vertex> getStopMap() {
		return stopMap;
	}

	public  HashMap<String, String> getStopColorMap() {
		return stopname_colorid_map;
	}

	public  HashMap<String, String> getStopLineMap() {
		return stopname_linename_map;
	}

	
	public ArrayList<String> getStopNameList() {
		return stopNamelist;
	}
	

	
	public List<String> getHindiLinelist() {
		return hindiLineNameList;
	}
	



	public List<String> getKannadaLineList(){
		return kannadaLineNameList;
	}
	

	public List<String> getMarathiLineList(){
		return marathiLineNameList;
	}
		
	
	
	
	//clear all saved datastructures.
	public static void clearListMap() {
		if (nodes != null)
			nodes.clear();
		nodes = null;

		if (edges != null)
			edges.clear();
		edges = null;

		if (stopMap != null)
			stopMap.clear();
		stopMap = null;

		if (stopname_colorid_map != null)
			stopname_colorid_map.clear();
		stopname_colorid_map = null;

		if (stopname_linename_map != null)
			stopname_linename_map.clear();
		stopname_linename_map = null;
		
		if (stopNamelist != null)
			stopNamelist.clear();
		stopNamelist = null;

		if(lastStopNameList!= null)
			lastStopNameList.clear();
		lastStopNameList = null;
		
		if(lineNamelist!= null)
			lineNamelist.clear();
		
		if(hindiLineNameList!=null)
			hindiLineNameList.clear();
		
		if(enTohiMap!=null)
			enTohiMap.clear();
		
		if(hiToEnMap!=null)
			hiToEnMap.clear();
		
		if(TaToEnMap!=null)
			TaToEnMap.clear();
		
		if(enToTaMap!=null)
			enToTaMap.clear();
	
		
		if(KnToenMap!=null)
			KnToenMap.clear();
		
		if(enToKnMap!=null)
			enToKnMap.clear();
	
		KnToenMap = null;
		enToKnMap = null;
		hiToEnMap = null;
		enTohiMap = null;
		TaToEnMap = null;
		enToTaMap = null;
		hindiLineNameList =null;
		lineNamelist = null;
		dbhelper = null;
		mInstance = null;
		mContext = null;

	}
}
