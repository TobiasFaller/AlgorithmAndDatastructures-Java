package org.tpc.uni.algo.exercises.exercises13;

/**
 * Stellt Informationen fuer eine einseitige Node-Node Verbindung bereit.
 * 
 * @author Tobias Faller
 *
 */
public class Arc implements Comparable<Arc> {
	
	private int tailNode;
	private int headNode;
	private int cost;
	private int maxSpeed;
	
	/**
	 * Erzeugt eine neue Verbindung zu einem Node.
	 * 
	 * @param tailNode Der Startknoten
	 * @param headNode Der Zielknoten
	 * @param cost Die Kosten (leange) der Verbindung
	 * @param maxSpeed Die maximale Geschwindigkeit
	 */
	public Arc(int tailNode, int headNode, int cost, int maxSpeed) {
		this.cost = cost;
		this.maxSpeed = maxSpeed;
		this.tailNode = tailNode;
		this.headNode = headNode;
	}
	
	public int getTailNode() {
		return tailNode;
	}
	
	public int getHeadNode() {
		return headNode;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	@Override
	public String toString() {
		String tail = String.valueOf(tailNode);
		String head = String.valueOf(headNode);
		String costString = String.valueOf(cost);
		String speedString = String.valueOf(maxSpeed);
		
		return new StringBuilder(costString.length() + speedString.length()
			+ tail.length() + head.length() + 7)
				.append(tail)
				.append(" -> ")
				.append(head)
				.append(": ")
				.append(costString)
				.append("m @")
				.append(speedString)
				.append("km/h")
				.toString();
	}
	
	/**
	 * Gibt die Zeit zurrueck, welche benoetigt wird um uber diese verbindung bei
	 * einer festen Maximalgeschwindgkeit zu reisen.
	 * 
	 * @param maxSpeed Die maximale Geschwindigkeit
	 * @return Die benoetigte Zeit
	 */
	public double getTravelTime(int maxSpeed) {
		long speed = (maxSpeed <= 0) ? this.maxSpeed :
						Math.min(maxSpeed, this.maxSpeed);
		
		return (double) cost / (double) speed;
	}
	
	@Override
	public int compareTo(Arc other) {
		int value = Integer.compare(tailNode, other.tailNode);
		if (value != 0) {
			return value;
		}
		
		value = Integer.compare(headNode, other.headNode);
		if (value != 0) {
			return value;
		}
		
		return Integer.compare(cost, other.cost);
	}
}