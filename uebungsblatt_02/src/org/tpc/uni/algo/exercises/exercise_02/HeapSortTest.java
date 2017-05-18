package org.tpc.uni.algo.exercises.exercise_02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Simple class providing some test for the heap-sort algorithm.
 * 
 * @author Tobias Faller
 *
 */
public class HeapSortTest {
  
  @Test
  public void testHeapSortTypical() {
    List<Integer> list = new ArrayList<>(Arrays.asList(5, 10, 20, 100, 32, 1));
    HeapSort.heapSort(list);
    Assert.assertEquals("[1, 5, 10, 20, 32, 100]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(454, 487, 2, 41, 6, 781, 5));
    HeapSort.heapSort(list);
    Assert.assertEquals("[2, 5, 6, 41, 454, 487, 781]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(45, 44, 45, 71, 872, 71, 864, 871, 546,
        8, 1, 89564, 1, 54, 564));
    HeapSort.heapSort(list);
    Assert.assertEquals("[1, 1, 8, 44, 45, 45, 54, 71, 71, 546, 564, 864, 871,"
        + " 872, 89564]", list.toString());
  }
  
  @Test
  public void testHeapSortCritical() {
    List<Integer> list = new ArrayList<>(Arrays.asList());
    HeapSort.heapSort(list);
    Assert.assertEquals("[]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(2));
    HeapSort.heapSort(list);
    Assert.assertEquals("[2]", list.toString());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testHeapSortCritical2() {
    HeapSort.heapSort(null);
  }
  
  @Test
  public void testHeapifyTypical() {
    List<Integer> list = new ArrayList<>(Arrays.asList(5, 10, 20, 100, 32, 1));
    HeapSort.heapify(list);
    Assert.assertEquals("[100, 32, 5, 20, 10, 1]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(5, 10, 552, 100, 32));
    HeapSort.heapify(list);
    Assert.assertEquals("[552, 100, 5, 32, 10]", 
        list.toString());
  }
  
  @Test
  public void testHeapifyCritical() {
    List<Integer> list = new ArrayList<>(Arrays.asList());
    HeapSort.heapify(list);
    Assert.assertEquals("[]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(8));
    HeapSort.heapify(list);
    Assert.assertEquals("[8]", list.toString());
  }
  
  @Test
  public void testHeapRepairTypical() {
    List<Integer> list = new ArrayList<>(Arrays.asList(5, 10, 2, 8));
    HeapSort.repairHeap(list, 4, 0);
    Assert.assertEquals("[10, 8, 2, 5]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(5, 10, 2, 8, 152, 4));
    HeapSort.repairHeap(list, 6, 0);
    Assert.assertEquals("[152, 10, 4, 8, 5, 2]", list.toString());
  }
  
  @Test
  public void testHeapRepairCritical() {
    List<Integer> list = new ArrayList<>(Arrays.asList());
    HeapSort.repairHeap(list, 0, 0);
    Assert.assertEquals("[]", list.toString());
    
    list = new ArrayList<>(Arrays.asList(56));
    HeapSort.repairHeap(list, 1, 0);
    Assert.assertEquals("[56]", list.toString());
  }
}