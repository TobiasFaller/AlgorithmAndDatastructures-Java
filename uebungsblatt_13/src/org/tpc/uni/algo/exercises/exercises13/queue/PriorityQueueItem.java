package org.tpc.uni.algo.exercises.exercises13.queue;

/**
 * Provides an interface for accessing the priority / value of a queue item.
 * 
 * @author Tobias Faller
 *
 * @param <T> The data type of the queue / queue item
 */
public class PriorityQueueItem<T> implements Comparable<PriorityQueueItem<T>> {
  
  T value;
  long priority;
  PriorityQueue<T> queue;
  
  PriorityQueueItem(long priority, PriorityQueue<T> queue) {
    this(priority, null, queue);
  }
  
  PriorityQueueItem(long priority, T value, PriorityQueue<T> queue) {
    this.priority = priority;
    this.value = value;
    this.queue = queue;
  }
  
  /**
   * Returns the value of this queue item.
   * 
   * @return The value of this item
   */
  public T getValue() {
    return value;
  }
  
  /**
   * Sets the value of this queue item.
   * 
   * @param value The new value to set this item to
   * @return The old value of this queue item
   */
  public T setValue(T value) {
    T oldValue = this.value;
    this.value = value;
    return oldValue;
  }
  
  /**
   * Gets the current priority of this queue item.
   * 
   * @return The current priority
   */
  public long getPriority() {
    return priority;
  }
  
  /**
   * Retrieves the queue this item is assigned to.
   * 
   * @return The queue this item is assigned to
   */
  public PriorityQueue<T> getQueue() {
    return queue;
  }
  
  /**
   * Sets the priority of this queue item.
   * 
   * @param priority The new priority of this item
   */
  public long setPriority(long priority) {
    long oldPriority = this.priority;
    if (priority == oldPriority) {
      return oldPriority;
    }
    
    queue.changePriority(this, priority);
    return oldPriority;
  }
  
  @Override
  public int compareTo(PriorityQueueItem<T> other) {
    return Long.compare(priority, other.priority);
  }
  
  @Override
  public String toString() {
    String priority = String.valueOf(this.priority);
    String value = String.valueOf(this.value);
    
    StringBuilder sb = new StringBuilder(priority.length()
        + value.length() + 22);
    
    sb.append("[Priority: ");
    sb.append(priority);
    sb.append(", Value:\"");
    sb.append(value);
    sb.append("\"]");
    
    return sb.toString();
  }
}