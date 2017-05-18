package org.tpc.uni.algo.exercises.exercises12;

/**
 * Stellt die Grundstruktur eines Nodes mit zwei Koordinaten bereit.
 * 
 * @author Tobias Faller
 *
 */
public class Node {
	
	private float latitude;
	private float longitude;
	
	public Node(float latitude, float longitude) {
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
		
		return new StringBuilder(lat + lon + 4)
				.append('(')
				.append(lat)
				.append(" ,")
				.append(lon)
				.append(')')
				.toString();
	}
}