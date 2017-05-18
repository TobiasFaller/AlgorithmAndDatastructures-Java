package org.tpc.uni.algo.exercises.exercise_02;

import java.util.List;

/**
 * Simple class implementing the heap-sort algorithm.
 * 
 * @author Tobias Faller
 *
 */
public class HeapSort {
  
  /**
   * Sorts the provided list with the heap-sort algorithm.
   * If <code>null</code> is passed an <code>IllegalArgumentException</code>
   * is thrown.
   * 
   * @param array The list to sort with the heap algorithm
   */
//  public static <T extends Comparable<T>> void heapSort(List<T> array) {
  public static void heapSort(List<Integer> list) {
    if (list == null) {
      throw new IllegalArgumentException("list cannot be null");
    }
    
    heapify(list);
    for (int index = list.size() - 1; index >= 0; index--) {
      list.set(0, list.set(index, list.get(0)));
      
      if (index > 1) {
        repairHeap(list, index, 0);
      }
    }
  }
  
  /**
   * Creates a heap out of a unsorted list.
   * 
   * @param list The list create the heap of
   */
//  protected static <T extends Comparable<T>> void heapify(List<T> array) {
  protected static void heapify(List<Integer> list) {
    int size = list.size();
    for (int index = (size - 1); index > 0; index--) {
      int parentIndex = (index - 1) / 2;
//    T parent = list.get(parentIndex);
      Integer parent = list.get(parentIndex);
      
//      T child = list.get(index);
      Integer child = list.get(index);
      
      if (child.compareTo(parent) > 0) {
        list.set(index, list.set(parentIndex, child));
        repairHeap(list, size - index, parentIndex);
      }
    }
  }
  
  /**
   * Repairs a max-heap with the entry-point given by startIndex.
   * 
   * @param list The list containing the heap-elements
   * @param count Number of elements in this heap
   * @param index The index to start repairing
   */
//  protected static <T extends Comparable<T>> void repairHeap(List<T> array,
//  int count, int startIndex) {
  protected static void repairHeap(List<Integer> list, int count, int index) {
    if (count <= index + 1) { // (startIndex * 2 + 1) - startIndex
      // No childs
      return;
    }
//    T element = list.get(index);
    Integer element = list.get(index);
    
    int leftIndex = index * 2 + 1;
    repairHeap(list, count - index - 1, leftIndex);
//    T leftChild = list.get(leftIndex);
    Integer leftChild = list.get(leftIndex);
    
    if (count == index + 2) {
      // Only one child-element
      if (element.compareTo(leftChild) < 0) {
        list.set(leftIndex, list.set(index, list.get(leftIndex)));
      }
      
      repairHeap(list, count - index - 1, leftIndex);
      return;
    }
    
    int rightIndex = leftIndex + 1;
    repairHeap(list, count - index - 2, rightIndex);
    
//    T rightChild = list.get(rightIndex);
    Integer rightChild = list.get(rightIndex);
    
    if (element.compareTo(leftChild) < 0) {
      // P < L
      if (leftChild.compareTo(rightChild) < 0) {
        // P < L < R
        // Swap parent and right child
        list.set(index, list.set(rightIndex, element));
        repairHeap(list, count - index - 2, rightIndex);
      } else if (element.compareTo(rightChild) < 0) {
        // P < R < L
        // Rotate clockwise
        list.set(index, list.set(leftIndex, list.set(rightIndex, element)));
        repairHeap(list, count - index - 1, leftIndex);
        repairHeap(list, count - index - 2, rightIndex);
      } else {
        // R < P < L
        // Swap parent and left child
        list.set(index, list.set(leftIndex, element));
        repairHeap(list, count - index - 1, leftIndex);
      }
    } else if (element.compareTo(rightChild) < 0) {
      // L < P < R
      // Rotate counter-clockwise
      list.set(index, list.set(rightIndex, list.set(leftIndex, element)));
      repairHeap(list, count - index - 1, leftIndex);
      repairHeap(list, count - index - 2, rightIndex);
    } else if (leftChild.compareTo(rightChild) < 0) {
      // L < R < P
      // Swap left and right
      list.set(rightIndex, list.set(leftIndex, rightChild));
      repairHeap(list, count - index - 1, leftIndex);
      repairHeap(list, count - index - 2, rightIndex);
    }
  }
  
}