package org.tpc.uni.algo.exercises.exercises13;

import java.util.Stack;

public class PathElement {
	
	private double travelTime = -1D;
	private long distance = -1L;
	
	protected Arc arc;
	protected Node node;
	protected PathElement previous;
	protected int maxSpeed;
	
	protected PathElement(Arc arc, Node node) {
		this.arc = arc;
		this.node = node;
		
		maxSpeed = -1;
	}
	
	protected void setPrevious(PathElement previous) {
		this.previous = previous;
		distance = -1L;
		travelTime = -1.0D;
		
		// Calculate distance
		getDistance();
	}
	
	protected void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	protected void setArc(Arc arc) {
		this.arc = arc;
	}
	
	protected double getTimeImpl() {
		if (travelTime >= 0.0D) {
			return travelTime;
		}
		
		if (previous == null) {
			return (travelTime = 0D);
		}
		
		double time = arc.getTravelTime(maxSpeed);
		if (previous.travelTime >= 0L) {
			time += previous.travelTime;
		} else {
			PathElement element = previous;
			Stack<PathElement> stack = new Stack<>();
			
			// Temporarily store elements
			while (element != null) {
				if (element.travelTime >= 0.0D) {
					break;
				}
				stack.push(element);
				element = element.previous;
			}
			
			// Traverse elements
			while (!stack.isEmpty()) {
				element = stack.pop();
				if (element.previous == null) {
					element.travelTime = 0D;
				} else {
					element.travelTime = element.previous.travelTime
							+ element.arc.getTravelTime(maxSpeed);
				}
			}
			
			time += previous.travelTime;
		}
		
		return (this.travelTime = time);
	}
	
	public double getTime() {
		return getTimeImpl() / 1000.0D;
	}
	
	/**
	 * Gibt die zurrueck zu legende Distanz zum Start zurrueck.
	 * 
	 * @return Die Distanz in Metern
	 */
	public long getDistance() {
		if (distance >= 0) {
			return distance;
		}
		
		if (previous == null) {
			return (distance = 0L);
		}
		
		long distance = arc.getCost();
		if (previous.distance >= 0L) {
			distance += previous.distance;
		} else {
			PathElement element = previous;
			Stack<PathElement> stack = new Stack<>();
			
			// Temporarily store elements
			while (element != null) {
				if (element.distance >= 0L) {
					break;
				}
				stack.push(element);
				element = element.previous;
			}
			
			// Traverse elements
			while (!stack.isEmpty()) {
				element = stack.pop();
				if (element.previous == null) {
					element.distance = 0L;
				} else {
					element.distance = element.previous.distance
							+ element.arc.getCost();
				}
			}
		}
		
		return (this.distance = distance);
	}
	
	/**
	 * Gibt die Koordinaten dieses Elements zurrueck. Diese sind durch ein
	 * Komma getrennt.
	 * 
	 * @return Die Koordinaten dieses Elements
	 */
	public String getCoordinates() {
		String lat = String.valueOf(node.latitude).intern();
		String lon = String.valueOf(node.longitude).intern();
		
		return new StringBuilder(lat.length() + lon.length() + 1)
				.append(lat)
				.append(',')
				.append(lon)
				.toString();
	}
	
	/**
	 * Gibt die ID des Knotens zurrueck.
	 * 
	 * @return Die ID des Knoten
	 */
	public int getId() {
		return arc.getHeadNode();
	}
}