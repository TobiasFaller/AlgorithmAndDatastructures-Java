package org.tpc.uni.algo.exercises.exercise05;

public class Counter {
  
  /**
   * Creates a new counter with the given initial value.
   * 
   * @param value The value to assign to
   * @return The newly created counter
   */
  public static Counter create(long value) {
    return new Counter(value);
  }
  
  private long value;
  
  /**
   * Creates a new counter with the given initial value.
   * 
   * @param initialValue The value to assign to
   */
  public Counter(long initialValue) {
    value = initialValue;
  }
  
  /**
   * Increments this counter by one.
   */
  public void increment() {
    value++;
  }
  
  /**
   * Decrements this counter by one.
   */
  public void decrement() {
    value--;
  }
  
  /**
   * Retrieves the current value of this counter.
   * 
   * @return The current value of this counter
   */
  public long getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}