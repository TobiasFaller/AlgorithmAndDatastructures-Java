package org.tpc.uni.algo.exercises.exercise_02;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Tests the performance of the implementation of the heapsort-algorithm.
 * 
 * @author Tobias Faller
 */
public class HeapSortPerformanceMeasure implements Runnable {
  
  private static final long ITERATIONS = 200;
  private static final long[] SIZE = {
    1, 10, 100, 200, 300, 500, 750, 1000, 2000
  };
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  
  public static void main(String[] args) {
    HeapSortPerformanceMeasure heapSortPerformanceTest
      = new HeapSortPerformanceMeasure();
    
    try (Scanner scanner = new Scanner(System.in)) {
      for (;;) {
        String line = scanner.nextLine().trim().toLowerCase();
        
        switch (line) {
        case "stop":
          heapSortPerformanceTest.cancel();
          break;
        case "start":
          heapSortPerformanceTest.start();
          break;
        default:
          System.out.println("Invalid command!");
          break;
        }
      }
    }
  }
  
  private ReentrantLock lock;
  
  public HeapSortPerformanceMeasure() {
    lock = null;
  }
  
  public void cancel() {
    lock.lock();
    lock = null;
  }
  
  public void start() {
    if (lock != null) {
      return; // Already running
    }
    lock = new ReentrantLock(true);
    new Thread(this).start();
  }
  
  @Override
  public void run() {
    ReentrantLock lock = this.lock;
    int sizes = SIZE.length;
    for (int index = 0; index < sizes; index++) {
      long size = SIZE[index];
      System.out.print(size + ";");
      
      long sum = 0;
      for (long iteration = 0; iteration < ITERATIONS; iteration++) {
        try {
          if (!lock.tryLock()) {
            return; // Terminate
          }
        } finally {
          try {
            lock.unlock();
          } catch (IllegalMonitorStateException e) {
            System.out.print(""); // Ignore
          }
        }
        
        List<Integer> elements = new ArrayList<>((int) size);
        
        for (int position = 0; position < size; position++) {
          elements.add(Integer.valueOf(RANDOM.nextInt((int) size)));
        }
        
        long time = System.currentTimeMillis();
        HeapSort.heapSort(elements);
        
        sum += System.currentTimeMillis() - time;
      }
      
      double average = (double) sum / (double) ITERATIONS;
      System.out.println(average);
    }
  }
}