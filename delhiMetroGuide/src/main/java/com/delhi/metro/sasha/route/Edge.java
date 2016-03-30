package com.delhi.metro.sasha.route;
public class Edge {
	private final String id;
	private final Vertex source;
	private final Vertex destination;
	private final int weight;
    private final float length;
	public Edge(String id, Vertex source, Vertex destination, int weight,String l) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.length = Float.valueOf(l);
	}

	public String getId() {
		return id;
	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}

	public float getLength(){
		return length;
	}
	@Override
	public String toString() {
		return source + " " + destination;
	}

}