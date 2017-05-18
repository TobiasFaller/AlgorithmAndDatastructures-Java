package org.tpc.uni.algo.exercises.exercises13;

/**
 * Stellt die Grundstruktur eines Nodes mit zwei Koordinaten bereit.
 * 
 * @author Tobias Faller
 *
 */
public class Node implements Comparable<Node> {
	
	protected float latitude;
	protected float longitude;
	
	protected Node(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof Node)) {
			return false;
		}
		
		Node other = (Node) obj;
		return (other.latitude == latitude) && (other.longitude == longitude);
	}
	
	@Override
	public String toString() {
		String lat = String.valueOf(latitude);
		String lon = String.valueOf(longitude);
		
		return new StringBuilder(lat.length() + lon.length() + 4)
				.append('(')
				.append(lat)
				.append(" ,")
				.append(lon)
				.append(')')
				.toString();
	}
	
	/**
	 * Gibt die Koordinaten spariert durch ein Komma als String zurrueck.
	 * 
	 * @return Die Koordinaten
	 */
	public String getCoordinates() {
		String lat = String.valueOf(latitude);
		String lon = String.valueOf(longitude);
		
		return new StringBuilder(lat.length() + lon.length() + 1)
				.append(lat)
				.append(',')
				.append(lon)
				.toString();
	}
	
	@Override
	public int compareTo(Node other) {
		int value = Float.compare(latitude, other.longitude);
		return value != 0 ? value : Float.compare(longitude, other.longitude);
	}
}