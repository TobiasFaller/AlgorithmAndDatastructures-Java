package org.tpc.uni.algo.exercises.exercises10;

import java.util.Iterator;

/**
 * Provides a binary search tree with <code>int</code> as keytype and
 * <code>T</code> as element type.
 * 
 * @author Tobias Faller
 *
 * @param <T> The element type of the search tree
 */
public class BinarySearchTree<T> implements Iterable<T> {
	
	protected class Node {
		
		protected int key;
		
		protected Node parent;
		protected Node leftChild;
		protected Node rightChild;
		
		protected Node previous;
		protected Node next;
		
		protected T value;
		
		protected Node() { }
		
		protected Node(int key, T value) {
			this.value = value;
			this.key = key;
		}
		
		@Override
		public String toString() {
			return key + ": " + value;
		}
	}
	
	protected Node head;
	protected Node tail;
	
	protected Node root;
	
	protected long size;
	protected long maxDepth;
	
	/**
	 * Creates a new binary search tree with the size of 0.
	 */
	public BinarySearchTree() {
		size = 0;
		root = null;
		head = (tail = new Node());
		
		head.next = tail;
		tail.previous = head;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		for (T element : this) {
			sb.append(String.valueOf(element));
			sb.append(", ");
		}
		
		int length = sb.length();
		if (length > 1) {
			sb.setLength(length - 2);
		}
		
		sb.append(']');
		
		return sb.toString();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			protected Node currentElement = head;
			
			@Override
			public boolean hasNext() {
				return currentElement.next != tail;
			}
			
			@Override
			public T next() {
				return (currentElement = currentElement.next).value;
			}
		};
	}
	
	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	public Iterator<T> inverseIterator() {
		return new Iterator<T>() {
			
			protected Node currentElement = tail;
			
			@Override
			public boolean hasNext() {
				return currentElement != head;
			}
			
			@Override
			public T next() {
				return (currentElement = currentElement.previous).value;
			}
		};
	}
	
	public long getSize() {
		return size;
	}
	
	public boolean isEmpty() {
		return (size == 0);
	}
	
	/**
	 * Returns the index of an element at the provided position.
	 * If an invalid index is provided an
	 * <code>ArrayIndexOutOutBoundsException</code> is thrown.
	 * 
	 * @param index The index to look for
	 * @return The key of the element at the specified index
	 * @throws ArrayIndexOutOfBoundsException Thrown if an invalid index is
	 * 					provided
	 */
	public int getKey(int index) throws ArrayIndexOutOfBoundsException {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		Node current = head;
		for (; index >= 0; index--) {
			current = current.next;
		}
		
		return current.next.key;
	}
	
	/**
	 * Returns the index of a the element at the position provided by the passed
	 * key.
	 * 
	 * @param key The key to search for
	 * @return The index of the element if found, else <code>-1</code> is
	 * 					returned.
	 */
	public long getIndex(int key) {
		Node node = nearestNode(key);
		if ((node == null) || (node.key != key)) {
			return -1;
		}
		
		long index = -1;
		do {
			node = node.previous;
			index++;
		} while (node != head);
		
		return index;
	}
	
	/**
	 * Inserts a new element with a given key into this tree.
	 * 
	 * @param key The key to insert this element to
	 * @param value The value to insert at the given key
	 * @return The inserted value
	 */
	public T insert(int key, T value) {
		if (root == null) {
			root = new Node(key, value);
			
			root.next = tail;
			root.previous = head;
			
			head.next = root;
			tail.previous = root;
			
			maxDepth = 1;
			return value;
		}
		Node currentNode = root;
		for (long depth = 2;; depth++) {
			if (currentNode.key < key) {
				// Insert right from node
				
				Node child = currentNode.rightChild;
				if (child != null) {
					currentNode = child;
					continue;
				}
				
				Node node = new Node(key, value);
				
				node.parent = currentNode;
				currentNode.rightChild = node;
				
				Node next = currentNode.next;
				node.next = next;
				currentNode.next = node;
				
				next.previous = node;
				node.previous = currentNode;
				
				maxDepth = Math.max(depth, maxDepth);
			} else if (currentNode.key == key) {
				currentNode.value = value; // Replace
			} else {
				// Insert left from Node
				
				Node child = currentNode.leftChild;
				if (child != null) {
					currentNode = child;
					continue;
				}
				
				Node node = new Node(key, value);
				
				node.parent = currentNode;
				currentNode.leftChild = node;
				
				Node previous = currentNode.previous;
				node.previous = previous;
				currentNode.previous = node;
				
				node.next = currentNode;
				previous.next = node;
				
				maxDepth = Math.max(depth, maxDepth);
			}
			
			break;
		}
		
		return value;
	}
	
	/**
	 * Looks for a specified element with a provided key.
	 * 
	 * @param key The key to search for
	 * @return The element associated with the key
	 */
	public T lookup(int key) {
		Node node = root;
		for (; node != null;) {
			int nodeKey = node.key;
			if (nodeKey == key) {
				return node.value;
			}
			
			node = (key < nodeKey) ? node.leftChild : node.rightChild;
		}
		
		return null;
	}
	
	/**
	 * Returns if an element with the provided key exists in this tree.
	 * 
	 * @param key The key to search for
	 * @return The found element or <code>null</code>
	 */
	public boolean contains(int key) {
		Node node = root;
		for (; node != null;) {
			int nodeKey = node.key;
			if (nodeKey == key) {
				return true;
			}
			
			node = (key < nodeKey) ? node.leftChild : node.rightChild;
		}
		
		return false;
	}
	
	protected Node nearestNode(int key) {
		if (head == null) {
			return null;
		}
		
		Node prevNode = root;
		for (;;) {
			int nodeKey = prevNode.key;
			if (nodeKey == key) {
				return prevNode;
			}
			
			Node nextNode = (key < nodeKey)
							? prevNode.leftChild : prevNode.rightChild;
			if (nextNode == null) {
				return prevNode;
			}
		}
	}
	
	/**
	 * Returns the value of the element at the specified index.
	 * 
	 * @param index The index to look for
	 * @return The element at the provided index
	 */
	public T get(long index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		Node current = head;
		for (; index >= 0; index--) {
			current = current.next;
		}
		
		return current.next.value;
	}
	
	/**
	 * Returns the minimum search-key in this binary search tree.
	 * 
	 * @return The minimum key in this tree
	 */
	public int getMinimumKey() {
		Node rightChild = head.rightChild;
		if (rightChild == null) {
			return 0;
		}
		
		return rightChild.key;
	}
	
	/**
	 * Returns the maximum search-key in this binary search tree.
	 * 
	 * @return The maximum key in this tree
	 */
	public int getMaximumKey() {
		Node leftChild = tail.leftChild;
		if (leftChild == null) {
			return 0;
		}
		return leftChild.key;
	}
	
	/**
	 * Returns the maximum depth of this search tree.
	 * 
	 * @return The maximum depth
	 */
	public long getMaxDepth() {
		return maxDepth;
	}
}