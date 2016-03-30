package com.delhi.metro.sasha.route;

import com.delhi.metro.sasha.db.DataFeeder;
import com.delhi.metro.sasha.gui.MetroApplication;
import com.delhi.metro.sasha.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.delhi.metro.sasha.db.DataFeeder.*;

public class DijkstraAlgorithm {

	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Vertex> predecessors;
	private Map<Vertex, Integer> distance;
    private Map<Pair,Float> edge_length_map;
	private HashMap<String, String> nodeColor_map;
	private float route_length;
    private boolean rapid;

	public DijkstraAlgorithm(Graph graph) {
		// create a copy of the array so that we can operate on this array
		this.edges = new ArrayList<Edge>(graph.getEdges());
		edge_length_map = DataFeeder.getInstance(MetroApplication.getInstance().getApplicationContext()).getPairLengthMap();
		nodeColor_map = DataFeeder.getInstance(MetroApplication.getInstance().getApplicationContext()).getStopColorMap();
	}

	public float getroute_length(){
		return route_length;
	}

    public boolean isRapidMetro(){
		return rapid;
	}
	public void execute(Vertex source, Vertex destination) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			LogUtils.LOGD("dijk", node.toString());

			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}

	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target,
						getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}

	}

	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& edge.getDestination().equals(target)) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();

		for (Edge edge : edges) {
			if (edge.getSource().equals(node)
					&& !isSettled(edge.getDestination())) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public ArrayList<Vertex> getPath(Vertex target) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		Vertex step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);


		Vertex current=null;
		Vertex next=null;
		int length = path.size();
		for(int i=0;i<length-1;i++){
             current = path.get(i);
			 next = path.get(i+1);
			if(!rapid && (nodeColor_map.get(current.getName()).equalsIgnoreCase("dB")||
					nodeColor_map.get(next.getName()).equalsIgnoreCase("dB")))
				rapid = true;
			route_length+= edge_length_map.get(new Pair(current,next));
		}

		return path;
	}

}
