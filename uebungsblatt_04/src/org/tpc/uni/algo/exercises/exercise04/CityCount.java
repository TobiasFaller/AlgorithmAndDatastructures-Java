package org.tpc.uni.algo.exercises.exercise04;

/**
 * Provides a city-name counter initialized with a count of 1.
 * 
 * @author Tobias Faller
 *
 */
public class CityCount implements Comparable<CityCount> {
  
  private final String name;
  private long count;
  
  public CityCount(String name) {
    this.name = name;
    this.count = 1;
  }
  
  public long getCount() {
    return count;
  }
  
  public String getName() {
    return name;
  }
  
  public long increment() {
    return ++count;
  }
  
  @Override
  public int compareTo(CityCount other) {
    if (other == null) {
      return 1;
    }
    
    int res = Long.compare(count, other.count);
    if (res == 0) {
      return other.name.compareTo(name);
    }
    return res;
  }
  
  @Override
  public String toString() {
    String countString = String.valueOf(count);
    
    StringBuilder sb
        = new StringBuilder(name.length() + countString.length() + 2);
    
    sb.append(name);
    sb.append(": ");
    sb.append(countString);
    
    return sb.toString();
  }
}