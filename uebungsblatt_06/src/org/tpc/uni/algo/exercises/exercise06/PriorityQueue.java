package org.tpc.uni.algo.exercises.exercise06;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a non synchronized priority queue.
 * 
 * @author Tobias Faller
 *
 */
public class PriorityQueue<T> {
  
  /**
   * Returns the index of the parent of this child.
   * 
   * @param index The index of the current element in the heap
   * @return The index of the parent of this element
   */
  private static final long getParent(long index) {
    return (index - 1) / 2;
  }
  
  /**
   * Returns the index of the left child.
   * 
   * @param index The index of the parent in the heap
   * @return The index of the left child
   */
  private static final long getLeftChild(long index) {
    return index * 2 + 1;
  }
  
  /**
   * Returns the index of the right child.
   * 
   * @param index The index of the parent in the heap
   * @return The index of the right child
   */
  private static final long getRightChild(long index) {
    return index * 2 + 2;
  }
  
  private List<PriorityQueueItem<T>> items;
  
  /**
   * Creates a new priority queue qithout elements.
   */
  public PriorityQueue() {
    items = new ArrayList<>();
  }
  
  /**
   * Inserts a new element into the queue.
   * 
   * @param priority The priority of the element
   * @param value The element to add
   * @return The editable queue-item
   */
  public PriorityQueueItem<T> insert(long priority, T value) {
    PriorityQueueItem<T> item
        = new PriorityQueueItem<T>(priority, value, this);
    
    items.add(item);
    fixHeapUp(items.size() - 1);
    
    return item;
  }
  
  /**
   * Changes the priority of this queue item.
   * Silently ignores this call if this element is not part of this queue.
   * 
   * @param item The queue item to change the priority of
   * @param priority The priority to set the element to
   */
  public void changePriority(PriorityQueueItem<T> item, long priority) {
    if (!items.contains(item)) {
      return;
    }
    
    item.priority = priority;
    
    long index = items.indexOf(item);
    fixHeapUp(index);
    fixHeapDown(index);
  }
  
  /**
   * Retrieves the queue element with the highest priority and removes it
   * from this queue.
   * If this queue is empty <code>null</code> is returned.
   * 
   * @return The queue element
   */
  public T pop() {
    if (items.isEmpty()) {
      return null;
    }
    
    if (items.size() == 1) {
      return items.remove(0).getValue();
    }
    
    T item = items.set(0, items.remove(items.size() - 1)).getValue();
    fixHeapDown(0);
    return item;
  }
  
  /**
   * Retrieves the queue element with the highest priority.
   * If this queue is empty <code>null</code> is returned.
   * 
   * @return The queue element
   */
  public T peek() {
    if (items.isEmpty()) {
      return null;
    }
    
    return items.get(0).getValue();
  }
  
  /**
   * Pushes a new item on this queue. It is not deterministic which item
   * will be returned when two values with the same priority are pushed onto
   * this queue.
   * 
   * @param priority The priority of the new item
   * @param value The value of the queue item
   * @return The newly created queue item providing modifying access to the
   *     priority
   */
  public PriorityQueueItem<T> push(long priority, T value) {
    return insert(priority, value);
  }
  
  /**
   * Removes an item from this queue. If the provided item does not exist then
   * this call will be silently ignored.
   * 
   * @param item The queue item to remove
   * @return The value of the removed item
   */
  public T remove(PriorityQueueItem<T> item) {
    long index = items.indexOf(item);
    if (index == -1) {
      return null;
    }
    
    if(items.size() == 1) {
      return items.remove((int) index).getValue();
    }
    
    PriorityQueueItem<T> removedItem = items.set((int) index, items.remove(items.size() - 1));
    fixHeapDown(index);
    return removedItem.getValue();
  }
  
  /**
   * Checks if this queue is empty.
   * 
   * @return <code>true</code> if this queue is empty
   *     <code>false</code> if not
   */
  public boolean isEmpty() {
    return items.isEmpty();
  }
  
  /**
   * Returns the number of elements in this queue.
   * 
   * @return The number of elements
   */
  public long size() {
    return items.size();
  }
  
  private void fixHeapUp(long index) {
    long parentIndex = getParent(index);
    if (parentIndex == index) {
      // This item has no parent
      return;
    }
    
    PriorityQueueItem<T> item = items.get((int) index);
    PriorityQueueItem<T> parent = items.get((int) parentIndex);
    
    if (parent.compareTo(item) < 0) {
      items.set((int) parentIndex, items.set((int) index, parent));
      
      fixHeapUp(parentIndex);
    }
  }
  
  private void fixHeapDown(long index) {
    long leftChild = getLeftChild(index);
    long rightChild = getRightChild(index);
    long size = items.size();
    
    if (leftChild >= size) {
      leftChild = -1;
    }
    if (rightChild >= size) {
      rightChild = -1;
    }
    
    if ((leftChild != -1) && (rightChild != -1)) {
      PriorityQueueItem<T> leftItem = items.get((int) leftChild);
      PriorityQueueItem<T> rightItem = items.get((int) rightChild);
      PriorityQueueItem<T> item = items.get((int) index);
      
      long switchIndex;
      PriorityQueueItem<T> switchItem;
      if (leftItem.compareTo(rightItem) > 0) {
        switchIndex = leftChild;
        switchItem = leftItem;
      } else {
        switchIndex = rightChild;
        switchItem = rightItem;
      }
      
      if (item.compareTo(switchItem) < 0) {
        items.set((int) index, items.set((int) switchIndex, item));
        fixHeapDown(switchIndex);
      }
    } else if (leftChild != -1) {
      PriorityQueueItem<T> leftItem = items.get((int) leftChild);
      PriorityQueueItem<T> item = items.get((int) index);
      
      if (item.compareTo(leftItem) < 0) {
        items.set((int) index, items.set((int) leftChild, item));
      }
    }
  }
  
  @Override
  public String toString() {
    return items.toString();
  }
  
  /**
   * Provides an alias for the {@link #peek()} method.
   * 
   * @return The queue element
   */
  public T getMin() {
    return peek();
  }
  
  /**
   * Provides an alias for the {@link #pop()} method.
   * 
   * @return The queue element
   */
  public T deleteMin() {
    return pop();
  }
  
  /**
   * Provides an alias for the
   * {@link #changePriority(PriorityQueueItem, long)} method.
   * 
   * @return The queue element
   */
  public void changeKey(PriorityQueueItem<T> item, long priority) {
    changePriority(item, priority);
  }
}