package org.tpc.uni.algo.exercises.exercises08;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provides different executable programs in one class.
 * These programs can be used to analyze the performance of the java
 * <code>Arrays.sort</code> method compared to the insertion into an
 * <code>HashMap</code>.
 * 
 * @author Tobias Faller
 *
 */
public class MemAccPerfTestMain {
  
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  
  /**
   * Provides an executable <code>main</code>-method to analyze the performance
   * of two different collections.
   * 
   * @author Tobias Faller
   *
   */
  public static class SimpleMeasurementMain {
    
    /**
     * Starts a performance test of different sorting/access-methods.
     * 
     * @param args The provided program arguments
     */
    public static void main(String[] args) {
      Integer arraySize = parseSize(args);
      if (arraySize == null) {
        printHelp();
        return;
      }
      
      test(arraySize);
    }
  }
  
  /**
   * Provides an executable <code>main</code>-method to analyze the performance
   * of two different collections with different data-sizes.
   * 
   * @author Tobias Faller
   *
   */
  public static class AdvancedMeasurementMain {
    
    /**
     * Starts a performance test of different sorting/access-methods
     * starting at a size of <code>Math.pow(2, 5)</code> to
     * <code>Math.pow(2,n)</code>. <code>n</code> is defined by the passed
     * program parameter.
     * 
     * @param args The provided program arguments
     */
    public static void main(String[] args) {
      Integer maxArraySize = parseSize(args);
      if (maxArraySize == null) {
        printHelp();
        return;
      }
      
      String newLine = "\n";
      //System.getProperty("line.separator");
      
      try (BufferedWriter sortWriter
            = Files.newBufferedWriter(Paths.get("sort.measure"));
          BufferedWriter mapWriter
            = Files.newBufferedWriter(Paths.get("map.measure"))) {
        for (double size = 64; size <= Math.pow(2, maxArraySize.intValue());
            size *= Math.sqrt(2)) {
          // Define size as final because of anonymous function
          final int currentSize = (int) size;
          
          System.out.format(Locale.GERMAN, "Elements: %d", currentSize);
          System.out.println();
          
          // Test sort
          double time = averageRuntime((data) -> testArraySort(data),
              () -> createRandomIntegerArray(currentSize), 5);
          System.out.format(Locale.GERMAN, "\tAverage time for sort: %.2f ms",
              time);
          System.out.println();
          sortWriter.append(String.format(Locale.GERMAN, "%d\t%.2f" + newLine,
              currentSize, time));
          
          // Test the map
          time = averageRuntime((data) -> testMap(data, HashMap::new),
              () -> createRandomIntegerArray(currentSize), 5);
          System.out.format(Locale.GERMAN, "\tAverage time for map:  %.2f ms",
              time);
          System.out.println();
          System.out.println();
          mapWriter.append(String.format(Locale.GERMAN, "%d\t%.2f" + newLine,
              currentSize, time));
          
          sortWriter.flush();
          mapWriter.flush();
        }
      } catch (IOException ioException) {
        ioException.printStackTrace(System.err);
      }
    }
  }
  
  /**
   * Tests two different sorting / data-access algorithms multiple times
   * with random data having the size <code>arraySize</code>.
   * 
   * @param arraySize The size of the data array
   */
  protected static void test(int arraySize) {
    // Test sort
    System.out.format(Locale.GERMAN, "Average time for sort: %.2f ms",
        averageRuntime((data) -> testArraySort(data),
            () -> createRandomIntegerArray(arraySize), 5));
    System.out.println();
    
    // Test the map
    System.out.format(Locale.GERMAN, "Average time for map:  %.2f ms",
        averageRuntime((data) -> testMap(data, HashMap::new),
            () -> createRandomIntegerArray(arraySize), 5));
  }
  
  /**
   * Sorts the provided array with <code>Arrays.sort</code>.
   * 
   * @param data The provided data to sort
   */
  protected static void testArraySort(Integer[] data) {
    Arrays.sort(data, Integer::compare);
  }
  
  /**
   * Inserts the provided array into a <code>Map</code>.
   * 
   * @param data The data to insert as keys
   * @param mapSupplier The map creator to insert the data to
   */
  protected static void testMap(Integer[] data,
      Supplier<Map<Integer, ?>> mapSupplier) {
    
    Map<Integer, ?> map = mapSupplier.get();
    for (Integer value : data) {
      map.put(value, null);
    }
  }
  
  /**
   * Parses the first provided parameter as integer and returns the number.
   * If the number is 0 or negative <code>null</code> is returned.
   * 
   * @param args The provided program-arguments
   * @return The parsed array-size
   */
  private static Integer parseSize(String[] args) {
    if (args.length < 1) {
      return null;
    }
    
    try {
      int number = Integer.parseInt(args[0]);
      if (number <= 0) {
        return null;
      }
      
      return Integer.valueOf(number);
    } catch (NumberFormatException formatException) {
      return null;
    }
  }
  
  /**
   * Prints help information for this program.
   */
  private static void printHelp() {
    System.out.println("Usage: MemoryAccessPerfomanceTestMain size");
    System.out.println();
    System.out.println("\tsize: The array size to use for testing");
    System.out.flush();
  }
  
  /**
   * Measures the average runtime of an executed function.
   * 
   * @param function The function to execute
   * @param initializer The function to create the data
   * @param times How many times this function should be called
   * @return The average runtime of the called function
   */
  private static <T> double averageRuntime(Consumer<T> function,
      Supplier<T> initializer, int times) {
    long sum = 0;
    
    for (int run = times; run >= 0; run--) {
      T data = initializer.get();
      long time = System.currentTimeMillis();
      function.accept(data);
      sum += (System.currentTimeMillis() - time);
    }
    
    return ((double) sum / (double) times);
  }
  
  /**
   * Creates a new <code>Integer[]</code> with the size of <code>size</code>.
   * Each entry has a random value between <code>0</code> and
   * <code>n - 1</code>.
   * 
   * @param size The size of the created array
   * @return The newly allocated array
   */
  private static Integer[] createRandomIntegerArray(int size) {
    final Integer[] data = new Integer[size];
    
    for (int index = data.length - 1; index >= 0; index--) {
      data[index] = RANDOM.nextInt(size);
    }
    
    return data;
  }
}