package org.tpc.uni.algo.exercises.exercises07;

/**
 * Diese Klasse bietet Unterstuetzung fuer ein dynamisch grosses Array, welches
 * sich der Anzahl seiner Daten anpasst.
 * 
 * @author Tobias Faller
 *
 */
public class DynamicIntArray {
  
  private static final int[] EMPTY_ARRAY = new int[0];
  
  private int size;
  private int[] data;
  private long lastTimeSpan;
  
  /**
   * Erstellt ein neues Array der groesse 0.
   */
  public DynamicIntArray() {
    data = EMPTY_ARRAY;
    size = 0;
    lastTimeSpan = 0;
  }
  
  /**
   * Erstellt ein Array mit vorgegebener Groesse.
   * 
   * @param size Die initiale Groesse des Arrays.
   */
  public DynamicIntArray(int size) {
    data = new int[size];
    this.size = size;
    lastTimeSpan = 0;
  }
  
  /**
   * Gibt das Element an der Position <code>index</code> zurrueck.
   * 
   * @param index Die Position des Elementes
   * @return Der Wert an der Position <code>index</code>
   */
  public int get(int index) {
    if (index >= size) {
      throw new IndexOutOfBoundsException();
    }
    
    return data[index];
  }
  
  /**
   * Fuegt einen Wert an das Array an. Falls die Groesse des Arrays nicht
   * ausreicht wird das Array vergroessert.
   * 
   * @param value Der anzufuegende Wert
   */
  public void append(int value) {
    long startTime = System.currentTimeMillis();
    if (size >= data.length) {
      long deltaT = System.currentTimeMillis();
      int[] newData = new int[Math.max(data.length * 2, 1)];
      startTime += (System.currentTimeMillis() - deltaT);
      
      System.arraycopy(data, 0, newData, 0, data.length);
      data = newData;
    }
    
    data[size++] = value;
    lastTimeSpan = System.currentTimeMillis() - startTime;
  }
  
  /**
   * Loescht das letzte Element in dem Array und gibt moeglicherweise
   * ungenutzten Speicherplatz wieder frei.
   * 
   * @return Der geloeschte Wert
   */
  public int remove() {
    if (size == 0) {
      throw new IndexOutOfBoundsException();
    }
    
    long startTime = System.currentTimeMillis();
    
    int value = data[--size];
    if (((data.length / 3) > size) && (data.length > 1)) {
      int[] newData = new int[data.length / 2];
      long deltaT = System.currentTimeMillis();
      startTime += (System.currentTimeMillis() - deltaT);
      
      System.arraycopy(data, 0, newData, 0, newData.length);
      data = newData;
    }
    
    lastTimeSpan = System.currentTimeMillis() - startTime;
    
    return value;
  }
  
  public int getSize() {
    return size;
  }
  
  public int getCapacity() {
    return data.length;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(2 + size * 5);
    
    sb.append('[');
    
    for (int index = 0; index < size; index++) {
      if (index != 0) {
        sb.append(", ");
      }
      
      sb.append(String.valueOf(data[index]));
    }
    
    sb.append(']');
    
    return sb.toString();
  }
  
  public long getLastOperationTimeSpan() {
    return lastTimeSpan;
  }
}