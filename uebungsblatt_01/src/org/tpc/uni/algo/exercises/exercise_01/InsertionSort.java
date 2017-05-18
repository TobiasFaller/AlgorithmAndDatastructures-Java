package org.tpc.uni.algo.exercises.exercise_01;

import java.util.List;

/**
 * Provides a basic insertion-sort algorithm provided through the
 * <code>sort(List<T> list)</code> method.
 * 
 * @author Tobias Faller
 *
 * @param <T> Type of elements to compare (has to implement Compareable<T>)
 */
public class InsertionSort<T extends Comparable<T>> {
  
  public List<T> sort(List<T> items) {
    if (items == null) {
      throw new IllegalArgumentException("The provided list cannot be null");
    }
    
    int listSize = items.size();
    for (int unsortedIndex = listSize - 2; unsortedIndex >= 0;
        unsortedIndex--) {
      T element = items.get(unsortedIndex);
      
      int index = listSize - 1;
      for (; index > unsortedIndex; index--) {
        int compareResult = element.compareTo(items.get(index));
        if (compareResult > 0) {
          // Element is smaller -> Insert after
          // (move elements before to left)
          break;
        }
      }
      
      if (index == unsortedIndex) {
        continue; // Smallest element is already on its position (beginning)
      }
      
      // Move elements to left
      for (int i = unsortedIndex; i < index; i++) {
        items.set(i, items.get(i + 1));
      }
      
      // Insert item
      items.set(index, element);
    }
    
    return items;
  }
}