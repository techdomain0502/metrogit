package com.delhi.metro.sasha.route;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;





public class CalculatePath {
	private Context mContext;
	private float route_length;
    private boolean isRapidRoute;


	public void setRapidRoute(boolean isRapid){
	   isRapidRoute = isRapid;
	}

	public boolean isRapidRoute(){
	  return  isRapidRoute;
	}

	public void setroute_length(float route_length){
		this.route_length = route_length;
	}

	public float getroute_length(){
		return route_length;
	}


 	public CalculatePath(Context context) {
		mContext = context;
	}
 	
	 public  ArrayList<Vertex> initialize(ArrayList<Vertex> n, ArrayList<Edge> e, String source, String destination,HashMap<String, Vertex> map1) {
		
		    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(new Graph(n,e));
	/*	    dijkstra.execute((Vertex)map1.get(source),(Vertex)map1.get(destination));
		    ArrayList<Vertex> path = dijkstra.getPath((Vertex)map1.get(destination));
	*/	   
		    dijkstra.execute(new Vertex(source, source),new Vertex(destination, destination));
		    ArrayList<Vertex> path = dijkstra.getPath(new Vertex(destination, destination));
		    this.setroute_length(dijkstra.getroute_length());
		    this.setRapidRoute(dijkstra.isRapidMetro());
		    return path;
	 }
}
