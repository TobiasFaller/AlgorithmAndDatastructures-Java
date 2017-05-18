package org.tpc.uni.algo.exercises.exercises13;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.tpc.uni.algo.exercises.exercises13.queue.PriorityQueue;
import org.tpc.uni.algo.exercises.exercises13.queue.PriorityQueueItem;

/**
 * Stellt eine Hilfsklasse zum laden / untersuchen von Graph-Systemen zur
 * verfuegung.
 * 
 * @author Tobias Faller
 *
 */
public class Graph {
	
	private static final double FACTOR = 100000.0D;
	
	protected Map<Integer, Node> nodes;
	
	protected Map<Integer, Collection<Arc>> arcs;
	private int arcCount;
	
	/**
	 * Erstellt einen leeren Graphen.
	 */
	public Graph() {
		nodes = new TreeMap<>();
		
		arcs = new TreeMap<>();
		arcCount = 0;
	}
	
	public int getNumArcs() {
		return arcCount;
	}
	
	public int getNumNodes() {
		return nodes.size();
	}
	
	/**
	 * Fuegt einen neuen Knoten in diesen Graphen ein.
	 * 
	 * @param node Der einzufuegende Knoten
	 * @return Die ID des eingefuegten Knoten
	 */
	public int addNode(Node node) {
		// if (nodeSet.contains(node)) {
		// 	return -1;
		// }
		
		nodes.put(node.hashCode(), node);
		
		return node.hashCode();
	}
	
	/**
	 * Fuegt einen neuen Knoten in diesen Graphen ein.
	 * Wird ignoriert, falls der Knoten mit der ID bereits existiert.
	 * 
	 * @param id Die ID des Knotens
	 * @param node Der einzufuegende Knoten
	 */
	public void addNode(int id, Node node) {
		// if (nodes.containsKey(id) || nodeSet.contains(node)) {
		// 	return;
		// }
		
		nodes.put(id, node);
	}
	
	/**
	 * Fuegt eine neue Verbindung zwischen zwei Knoten hinzu.
	 * 
	 * @param from Der Startknoten
	 * @param arc Die Verbindung zwischen den beiden Knoten
	 */
	public void addArc(int from, Arc arc) {
		Collection<Arc> arcList
						= arcs.computeIfAbsent(from, k -> new ArrayList<>(3));
		// if ((arc == null) || arcList.contains(arc)
		// 				|| !nodes.containsKey(arc.getHeadNode())) {
		// 	return;
		// }
		
		arcCount++;
		arcList.add(arc);
	}
	
	/**
	 * Liest die Graph-Daten aus einer Eingabedatei.
	 * 
	 * @param fileName Der Datei-Pfad der zu ladenden Datei
	 * @throws IOException Falls eine Eingabe-/Ausgabeausnahme auftritt
	 */
	public void readFromFile(Path fileName) throws IOException {
		try {
			try (BufferedReader bReader = Files.newBufferedReader(fileName)) {
				int nodes = Integer.parseInt(bReader.readLine());
				int arcs = Integer.parseInt(bReader.readLine());
				
				for (int i = nodes - 1; i >= 0; i--) {
					String line = bReader.readLine();
					String[] data;
					if ((line == null)
									|| (line = line.trim()).isEmpty()
									|| (data = line.split(" ")).length != 3) {
						continue; // Retry at next line
					}
					
					int id = Integer.parseInt(data[0].trim());
					float lat = Float.parseFloat(data[1].trim());
					float lon = Float.parseFloat(data[2].trim());
					
					Node node = new Node(lat, lon);
					// System.out.println("Added Node: " + id + ": " + node.toString());
					addNode(id, node);
				}
				
				for (int i = arcs - 1; i >= 0; i--) {
					String line = bReader.readLine();
					String[] data;
					if ((line == null)
									|| (line = line.trim()).isEmpty()
									|| (data = line.split(" ")).length != 4) {
						continue; // Retry at next line
					}
					
					int from = Integer.parseInt(data[0].trim());
					int to = Integer.parseInt(data[1].trim());
					int distance = Integer.parseInt(data[2].trim());
					int speed = Integer.parseInt(data[3].trim());
					
					Arc arc = new Arc(from, to, distance, speed);
					// System.out.println("Added Arc: " + arc);
					addArc(from, arc);
				}
			} catch (NumberFormatException numberFormatException) {
				throw new IOException(numberFormatException);
			}
		} catch (IOException ioException) {
			reset();
			throw ioException;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Entry<Integer, Node> node : nodes.entrySet()) {
			Node nodeValue = node.getValue();
			Integer id = node.getKey();
			
			sb.append(id)
				.append(": ")
				.append(nodeValue.toString())
				.append("\r\n");
			
			Collection<Arc> arcs = this.arcs.get(id);
			if (arcs == null) {
				continue;
			}
			
			for (Arc arc : arcs) {
				sb.append('\t')
					.append(arc.toString())
					.append("\r\n");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Berechnet alle erreichbaren Komponenten von eine start-node aus.
	 * 
	 * @param start Das start-node als Einstiegspunkt
	 * @return Eine Sammlung von allen Nodes, welche durch die gegebene Node-Id
	 * 				erreichbar sind
	 */
	public Collection<Integer> computeReachableNodes(int start) {
		Set<Integer> nodes = new HashSet<>();
		Set<Integer> newNodes = new HashSet<>();
		Set<Integer> newReachableNodes = new HashSet<>();
		
		newNodes.add(start);
		while (!newNodes.isEmpty()) {
			nodes.addAll(newNodes);
			newReachableNodes.clear();
			
			for (Integer id : newNodes) {
				Collection<Arc> arcs =  this.arcs.get(id);
				if (arcs == null) {
					continue;
				}
				
				for (Arc arc : arcs) {
					int head = arc.getHeadNode();
					if (nodes.contains(head) || newNodes.contains(head)) {
						continue;
					}
					
					newReachableNodes.add(arc.getHeadNode());
				}
			}
			
			Set<Integer> tmp = newNodes;
			newNodes = newReachableNodes;
			newReachableNodes = tmp;
		}
		
		return nodes;
	}
	
	/**
	 * Setzt dieses Objekt zurrueck.
	 */
	private void reset() {
		nodes.clear();
		arcs.clear();
		arcCount = 0;
	}
	
	/**
	 * Berechnet die groesste Zusammenhangskomponente, welche in diesem Graphen
	 * existiert.
	 * 
	 * @return Die groesste Zusammenhangskomponente als Sammlung von Knoten-IDs
	 */
	public int computeLargestConnectedComponent() {
		Stack<Integer> keys = new Stack<>();
		keys.addAll(nodes.keySet());
		
		long lastComponentId = 0;
		Map<Integer, Long> components = new TreeMap<>();
		Map<Long, Collection<Long>> includedComponents = new TreeMap<>();
		
		Set<Integer> visitedNodes = new HashSet<>();
		Stack<Integer> nodesToVisit = new Stack<>();
		Set<Long> includeComponents;
		
		while (!keys.isEmpty()) {
			Integer id = keys.pop();
			if (components.containsKey(id)) {
				continue;
			}
			
			nodesToVisit.clear();
			nodesToVisit.add(id);
			
			lastComponentId++;
			includeComponents = new HashSet<>();
			
			while (!nodesToVisit.isEmpty()) {
				Integer newId = nodesToVisit.pop();
				
				if (!components.containsKey(newId)) {
					components.put(newId, lastComponentId);
				}
				
				if (!visitedNodes.contains(newId)) {
					visitedNodes.add(newId);
				}
				
				Collection<Arc> arcs = this.arcs.get(newId);
				if (arcs == null) {
					continue;
				}
				
				for (Arc arc : arcs) {
					Integer head = arc.getHeadNode();
					
					if (components.containsKey(head)
									&& !components.get(head).equals(lastComponentId)) {
						includeComponents.add(components.get(head));
						continue;
					}
					if (visitedNodes.contains(head) || nodesToVisit.contains(head)) {
						continue;
					}
					
					if (!nodesToVisit.contains(head) && !head.equals(newId)) {
						nodesToVisit.push(head);
					}
				}
			}
			
			includedComponents.put(lastComponentId, includeComponents);
		}
		
		Map<Long, Integer> componentSizes = new TreeMap<>();
		Stack<Long> componentIds = new Stack<>();
		for (Entry<Integer, Long> nodeEntry : components.entrySet()) {
			Long id = nodeEntry.getValue();
			
			Integer value = componentSizes.computeIfAbsent(id,
							(key) -> Integer.valueOf(0));
			value += 1;
			componentSizes.put(id, value);
			
			if (!componentIds.contains(id)) {
				componentIds.add(id);
			}
		}
		
		Map<Long, Integer> finalComponentSizes = new TreeMap<>();
		while (!componentIds.isEmpty()) {
			Deque<Long> deque = new LinkedBlockingDeque<>();
			deque.add(componentIds.pop());
			
			while (!deque.isEmpty()) {
				Long id = deque.pop();
				if (!includedComponents.containsKey(id)) {
					finalComponentSizes.put(id, componentSizes.get(id));
					continue;
				}
				
				boolean resolved = true;
				int size = 0;
				Set<Long> addedComponents = new HashSet<>();
				Stack<Long> componentsToAdd = new Stack<>();
				componentsToAdd.addAll(includedComponents.get(id));
				
				while (!componentsToAdd.isEmpty()) {
					Long component = componentsToAdd.pop();
					
					if (finalComponentSizes.containsKey(component)) {
						if (resolved) {
							size += componentSizes.get(component);
							addedComponents.add(component);
							
							for (Long subComponent : includedComponents.get(id)) {
								if (!addedComponents.contains(subComponent)
												&& !componentsToAdd.contains(subComponent)) {
									componentsToAdd.add(subComponent);
								}
							}
						}
					} else {
						if (!deque.contains(component)) {
							deque.push(component);
							resolved = false;
						}
					}
				}
				
				if (!resolved) {
					deque.addLast(id);
					continue;
				}
				
				size += componentSizes.get(id);
				finalComponentSizes.put(id, size);
			}
		}
		
		IntSummaryStatistics stats = finalComponentSizes
						.values()
						.stream()
						.collect(Collectors.summarizingInt((value) -> value));
		
		return stats.getMax();
	}
	
	/**
	 * Berechnet den kuerzesten Pfad von einem gegebenen Startknoten.
	 * 
	 * @param startNode Der zu nutzende Startknoten
	 * @return Eine Map von Knoten-IDs zu PfadElementen
	 */
	public Map<Integer, PathElement> computeShortestPaths(int startNode) {
		Map<Integer, PathElement> pathElements
						= new HashMap<>((int)(nodes.size() * 1.50D));
		
		PriorityQueue<Integer> nextNodes = new PriorityQueue<>();
		Map<Integer, PriorityQueueItem<Integer>> nextNodeElements
						= new HashMap<>((int)(nodes.size() * 1.50D));
		
		// Insert first element
		pathElements.put(startNode, new PathElement(null, nodes.get(startNode)));
		nextNodes.insert(0L, startNode);
		
		// Jump from one element to the next and add them to the queue
		while (!nextNodes.isEmpty()) {
			Integer index = nextNodes.pop();
			nextNodeElements.remove(index); // Element is no longer in queue
			
			PathElement currentElement = pathElements.get(index);
			
			Collection<Arc> arcs = this.arcs.get(index);
			if (arcs == null) {
				continue;
			}
			
			// Handle the connected-nodes
			for (Arc arc : arcs) {
				Integer nextNode = arc.getHeadNode();
				long distance = currentElement.getDistance() + arc.getCost();
				PathElement pathElement;
				
				// Was node already visited?
				if (pathElements.containsKey(nextNode)) {
					pathElement = pathElements.get(nextNode);
					
					if (pathElement.getDistance() > distance) {
						pathElement.setArc(arc);
						pathElement.setPrevious(currentElement);
						
						if (!nextNodeElements.containsKey(nextNode)) {
							nextNodeElements.put(nextNode,
											nextNodes.insert(distance, nextNode));
						} else {
							PriorityQueueItem<Integer> queueItem
											= nextNodeElements.get(nextNode);
							if (queueItem.getPriority() > distance) {
								queueItem.setPriority(distance);
							}
						}
					}
				} else {
					pathElement = new PathElement(arc, nodes.get(nextNode));
					pathElement.setPrevious(currentElement);
					pathElements.put(nextNode, pathElement);
					
					if (!nextNodeElements.containsKey(nextNode)) {
						nextNodeElements.put(nextNode,
										nextNodes.insert(distance, nextNode));
					} else {
						PriorityQueueItem<Integer> queueItem
										= nextNodeElements.get(nextNode);
						if (queueItem.getPriority() > distance) {
							queueItem.setPriority(distance);
						}
					}
				}
			}
		}
		
		return pathElements;
	}
	
	/**
	 * Berechnet den schnellsten Pfad von einem gegebenen Startknoten.
	 * Die Zeit ist dabei auf ca. 3600 / FACTOR Sekunden genau.
	 * 
	 * @param startNode Der zu nutzende Startknoten
	 * @param maxSpeed Die maximale Reisegeschwindigkeit
	 * @return Eine Map von Knoten-IDs zu PfadElementen
	 */
	public Map<Integer, PathElement> computeFastestPaths(int startNode,
					int maxSpeed) {
		Map<Integer, PathElement> pathElements = new TreeMap<>();
		
		PriorityQueue<Integer> nextNodes = new PriorityQueue<>();
		Map<Integer, PriorityQueueItem<Integer>> nextNodeElements = new TreeMap<>();
		
		// Insert first element
		PathElement startElement = new PathElement(null, nodes.get(startNode));
		startElement.setMaxSpeed(maxSpeed);
		pathElements.put(startNode, startElement);
		nextNodes.insert(0L, startNode);
		
		// Jump from one element to the next and add them to the queue
		while (!nextNodes.isEmpty()) {
			Integer index = nextNodes.pop();
			nextNodeElements.remove(index); // Element is no longer in queue
			PathElement currentElement = pathElements.get(index);
			
			Collection<Arc> arcs = this.arcs.get(index);
			if (arcs == null) {
				continue;
			}
			
			// Handle the connected-nodes
			for (Arc arc : arcs) {
				Integer nextNode = arc.getHeadNode();
				double time = currentElement.getTimeImpl()
								+ arc.getTravelTime(maxSpeed);
				PathElement pathElement;
				
				// Was node already visited?
				if (pathElements.containsKey(nextNode)) {
					pathElement = pathElements.get(nextNode);
					
					if (pathElement.getTimeImpl() > time) {
						pathElement.setArc(arc);
						pathElement.setPrevious(currentElement);
						
						if (!nextNodeElements.containsKey(nextNode)) {
							nextNodeElements.put(nextNode,
											nextNodes.insert((long) (time * FACTOR), nextNode));
						} else {
							PriorityQueueItem<Integer> queueItem
											= nextNodeElements.get(nextNode);
							if (queueItem.getPriority() > (long) (time * FACTOR)) {
								queueItem.setPriority((long) (time * FACTOR));
							}
						}
					}
				} else {
					pathElement = new PathElement(arc, nodes.get(nextNode));
					pathElement.setPrevious(currentElement);
					pathElement.setMaxSpeed(maxSpeed);
					pathElements.put(nextNode, pathElement);
					
					if (!nextNodeElements.containsKey(nextNode)) {
						nextNodeElements.put(nextNode,
										nextNodes.insert((long) (time * FACTOR), nextNode));
					} else {
						PriorityQueueItem<Integer> queueItem
										= nextNodeElements.get(nextNode);
						if (queueItem.getPriority() > (long) (time * FACTOR)) {
							queueItem.setPriority((long) (time * FACTOR));
						}
					}
				}
			}
		}
		
		return pathElements;
	}
}