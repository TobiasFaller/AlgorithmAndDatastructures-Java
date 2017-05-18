package org.tpc.uni.algo.exercises.exercise_01;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

// Java 8
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Provides test-classes for the <code>InsertionSort</code> class.
 * 
 * @author Tobias Faller
 *
 */
public class InsertionSortTest {
  
  public static List<Integer> intList(int ... values) {
    List<Integer> list = new ArrayList<>(values.length);
    for (int value : values) {
      list.add(Integer.valueOf(value));
    }
    return list;
//    Kein Java-8 Support von Checkstyle
//    return IntStream.of(values).mapToObj(Integer::valueOf)
//      .collect(Collectors.toList());
  }
  
  /**
   * Provides typical tests for the <code>InsertionSort</code> class.
   */
  @Test
  public void testInsertionSortTypical() {
    InsertionSort<Integer> insertionSort = new InsertionSort<>();
    
    List<Integer> items = insertionSort.sort(intList(15, 5, 6, 18, 20, 1, 455));
    assertEquals(intList(1, 5, 6, 15, 18, 20, 455), items);
    
    items = insertionSort.sort(intList(15, 15, 84, 5, 87, 5, 6, 13));
    assertEquals(intList(5, 5, 6, 13, 15, 15, 84, 87), items);
  }
  
  /**
   * Provides critical tests for the <code>InsertionSort</code> class.
   */
  @Test
  public void testInsertionSortCritical() {
    InsertionSort<Integer> insertionSort = new InsertionSort<>();
    
    List<Integer> items = insertionSort.sort(intList());
    assertEquals(intList(), items);
  }
  
  /**
   * Provides a null-critical test for the <code>InsertionSort</code> class.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInsertionSortNull() {
    InsertionSort<Integer> insertionSort = new InsertionSort<>();
    
    insertionSort.sort(null);
  }
}