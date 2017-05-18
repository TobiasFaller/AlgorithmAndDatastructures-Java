package org.tpc.uni.algo.exercises.exercises12;

/**
 * Stellt Informationen fuer eine einseitige Node-Node Verbindung bereit.
 * 
 * @author Tobias Faller
 *
 */
public class Arc {
	
	private int headNode;
	private int cost;
	private int maxSpeed;
	
	/**
	 * Erzeugt eine neue Verbindung zu einem Node.
	 * 
	 * @param headNode Der Endnode
	 * @param cost Die kosten (leange) der Verbindung
	 * @param maxSpeed Die maximale Geschwindigkeit
	 */
	public Arc(int headNode, int cost, int maxSpeed) {
		this.cost = cost;
		this.maxSpeed = maxSpeed;
		this.headNode = headNode;
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
		String costString = String.valueOf(cost);
		String speedString = String.valueOf(maxSpeed);
		
		return new StringBuilder(costString.length() + speedString.length() + 7)
				.append(costString)
				.append("m @")
				.append(speedString)
				.append("km/h")
				.toString();
	}
}