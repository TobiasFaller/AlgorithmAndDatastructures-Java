package org.tpc.uni.algo.exercises.exercises10;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This program can be used to analyze the performance of a binary search tree.
 * 
 * @author Tobias Faller
 *
 */
public class BinarySearchTreeMain {
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	
	/**
	 * Provides an executable <code>main</code>-method to analyze the performance
	 * of two different insertions into a binary tree.
	 * 
	 * @author Tobias Faller
	 *
	 */
	public static void main(String[] args) {
		Integer maxArraySize = parseSize(args);
		if (maxArraySize == null) {
			printHelp();
			return;
		}
		
		String newLine = "\n";
		// System.getProperty("line.separator");
		
		try (BufferedWriter randomWriter
				= Files.newBufferedWriter(Paths.get("random.measure"));
				BufferedWriter sortedWriter
				= Files.newBufferedWriter(Paths.get("ordered.measure"))) {
			for (double size = 64;
							(long) size <= (long) Math.pow(2, maxArraySize.intValue());
							size *= Math.sqrt(2)) {
				// Define size as final because of anonymous function
				final int currentSize = (int) size;
				
				System.out.format(Locale.GERMAN, "Elements: %d", currentSize);
				System.out.println();
				
				// Test random
				double[] ret = averageRuntime((data) -> testInsert(data),
								() -> createRandomIntegerArray(currentSize), 5);
				System.out.format(Locale.GERMAN,
								"\tAverage time for random: %.2f ms\tdepth: %.2f",
								ret[0], ret[1]);
				System.out.println();
				randomWriter.append(String.format(Locale.GERMAN, "%d\t%.2f\t%.2f"
								+ newLine, currentSize, ret[0], ret[1]));
				
				// Test sorted
				ret = averageRuntime((data) -> testInsert(data),
						() -> createOrderedIntegerArray(currentSize), 5);
				System.out.format(Locale.GERMAN,
								"\tAverage time for sorted: %.2f ms\tdepth: %.2f", ret[0],
								ret[1]);
				System.out.println();
				System.out.println();
				sortedWriter.append(String.format(Locale.GERMAN, "%d\t%.2f\t%.2f"
								+ newLine, currentSize, ret[0], ret[1]));
				
				randomWriter.flush();
				sortedWriter.flush();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace(System.err);
		}
	}

	/**
	 * Parses the first provided parameter as integer and returns the number. If
	 * the number is 0 or negative <code>null</code> is returned.
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
		System.out.println("Usage: BinarySearchTreeMain size");
		System.out.println();
		System.out.println("\tsize: The data size to use for testing");
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
	private static <T> double[] averageRuntime(Function<T, Long> function,
					Supplier<T> initializer, int times) {
		long sum = 0;
		long depthSum = 0;
		
		for (int run = times - 1; run >= 0; run--) {
			T data = initializer.get();
			long time = System.currentTimeMillis();
			depthSum += function.apply(data);
			sum += (System.currentTimeMillis() - time);
		}
		
		return new double[] {
				((double) sum / (double) times),
				((double) depthSum / (double) times)
		};
	}
	
	/**
	 * Creates a new <code>int[]</code> with the size of <code>size</code>.
	 * Each entry has a random value between <code>0</code>
	 * and <code>n - 1</code>.
	 * 
	 * @param size The size of the created array
	 * @return The newly allocated array
	 */
	private static int[] createRandomIntegerArray(int size) {
		final int[] data = createOrderedIntegerArray(size);
		for (int i = data.length - 1; i > 0; i--) {
			int index = RANDOM.nextInt(i + 1);
			if (index != i) {
				// Elegant version of swapping an int-array
				data[index] ^= data[i];
				data[i] ^= data[index];
				data[index] ^= data[i];
			}
		}
		
		return data;
	}
	
	/**
	 * Creates a new <code>int[]</code> with the size of <code>size</code>.
	 * Each entry has a random value between <code>0</code>
	 * and <code>n - 1</code> in ascending order.
	 * 
	 * @param size The size of the created array
	 * @return The newly allocated array
	 */
	private static int[] createOrderedIntegerArray(int size) {
		final int[] data = new int[size];
		
		for (int index = 0; index < size; index++) {
			data[index] = index;
		}
		
		return data;
	}
	
	/**
	 * Provides the test-method to insert the data into the tree.
	 * 
	 * @param data The data to insert
	 * @return The maximum depth of the tree
	 */
	private static long testInsert(int[] data) {
		BinarySearchTree<Void> searchTree = new BinarySearchTree<>();
		
		for (int index = 0; index < data.length; index++) {
			searchTree.insert(data[index], null);
		}
		
		return searchTree.getMaxDepth();
	}
}