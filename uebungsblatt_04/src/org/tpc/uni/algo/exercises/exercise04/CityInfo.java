package org.tpc.uni.algo.exercises.exercise04;

/**
 * Provides basic storage for a city and country name.
 * 
 * @author Tobias Faller
 *
 */
public class CityInfo implements Comparable<CityInfo> {
  
  private final String name;
  private final String countryName;
  
  public CityInfo(String name, String countryName) {
    this.name = name;
    this.countryName = countryName;
  }
  
  public String getName() {
    return name;
  }
  
  public String getCountryName() {
    return countryName;
  }
  
  @Override
  public int compareTo(CityInfo other) {
    if (other == null) {
      return 1;
    }
    
    return name.compareTo(other.name);
  }
  
  @Override
  public String toString() {
    StringBuilder sb
        = new StringBuilder(name.length() + countryName.length() + 2);
    
    sb.append(countryName);
    sb.append(": ");
    sb.append(name);
    
    return sb.toString();
  }
}