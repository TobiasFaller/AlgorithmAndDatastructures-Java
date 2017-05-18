package org.tpc.uni.algo.exercises.exercises07;

/**
 * Stellt ein Array bereit, welches mit einer garantierten zugriffszeit von
 * O(n) arbeitet.
 * 
 * @author Tobias Faller
 *
 */
public class WorstCaseConstantAccessTimeArray {
  
  private static final int[] EMPTY_ARRAY = new int[0];
  
  private int[] smallArray;
  private int[] largeArray;
  private int size;
  private long lastTimeSpan;
  
  /**
   * Erstellt eine neue Instanz des Arrays.
   */
  public WorstCaseConstantAccessTimeArray() {
    smallArray = EMPTY_ARRAY;
    largeArray = EMPTY_ARRAY;
    size = 0;
  }
  
  /**
   * Fuegt einen Wert am ende des Arrays ein und vergroessert den Speicherplatz
   * wenn noetig.
   * @param item Das anzufuegende Item
   */
  public void append(int item) {
    long startTime = System.currentTimeMillis();
    
    if (size == 0) {
      long deltaT = System.currentTimeMillis();
      smallArray = new int[1];
      largeArray = new int[2];
      startTime += (System.currentTimeMillis() - deltaT);
      
      smallArray[size] = item;
      largeArray[size] = item;
    } else if (size * 2 >= smallArray.length) { // [***|*__]
      if (size >= smallArray.length) { // [***|***] Array full
        smallArray = largeArray;
        
        long deltaT = System.currentTimeMillis();
        largeArray = new int[Math.max(smallArray.length * 2, 1)];
        startTime += (System.currentTimeMillis() - deltaT);
      } 
      
      // Copy one part of the small array
      int copyIndex = (size + (smallArray.length / 2)) % smallArray.length;
      largeArray[copyIndex] = smallArray[copyIndex];
      largeArray[size] = item;
    } else {
      smallArray[size] = item;
    }
    
    size++;
    lastTimeSpan = System.currentTimeMillis() - startTime;
  }
  
  /**
   * Holt den Datenwert aus diesem Array, welcher an der Stelle
   * <code>index</code> steht.
   * 
   * @param index Der Speicherindex der Daten
   * @return Den Datenwert an der Position <code>index</code>
   */
  public int get(int index) {
    if (index >= size) {
      throw new IndexOutOfBoundsException();
    }
    if (index * 2 >= smallArray.length) {
      return largeArray[index];
    } else {
      return smallArray[index];
    }
  }
  
  /**
   * Ersetz einen Datenwert an der angegebenen Position in dem Array.
   * 
   * @param index Die zu ersetzende Position
   * @param item Der mit dem zu ersetzende Datenwert
   */
  public void put(int index, int item) {
    if (index >= size) {
      throw new IndexOutOfBoundsException();
    }
    
    if (index * 2 >= smallArray.length) {
      largeArray[index] = item;
    } else {
      smallArray[index] = item;
    }
  }
  
  public int getSize() {
    return size;
  }
  
  public int getCapacity() {
    return smallArray.length;
  }
  
  public int getLargeCapacity() {
    return largeArray.length;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(2 + size * 5);
    
    sb.append('[');
    
    for (int index = 0; index < size; index++) {
      if (index != 0) {
        sb.append(", ");
      }
      if (index * 2 >= smallArray.length) {
        sb.append(String.valueOf(largeArray[index]));
      } else {
        sb.append(String.valueOf(smallArray[index]));
      }
    }
    
    sb.append(']');
    
    return sb.toString();
  }
  
  public long getLastOperationTimeSpan() {
    return lastTimeSpan;
  }
}