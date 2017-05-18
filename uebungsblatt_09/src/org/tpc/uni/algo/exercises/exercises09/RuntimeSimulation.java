package org.tpc.uni.algo.exercises.exercises09;

import java.util.function.Function;

/**
 * Provides a class helping to identify the runtime length of a function with
 * different data sizes.
 * 
 * @author Tobias Faller
 *
 */
public class RuntimeSimulation {
  
  private static final long STEPS = 50000;
  
  /**
   * Provides the main program helping to identify the runtime of a provided
   * function.
   * 
   * @param args The provided program arguments
   */
  public static void main(String[] args) {
    System.out.format("Estimating c with %d steps!", STEPS);
    try {
      double constant = estimateCForAlgorithm(
          RuntimeSimulation::estimatedRuntime,
          RuntimeSimulation::simulatedAlgorithm,
          STEPS);
      
      System.out.format("Estimated the minimal c to %.5f in %d steps!", constant,
          STEPS).println();
      System.out.println("The algorithm is element of the provided"
          + " function set.");
    } catch (EstimateException e) {
      System.out.println("The estimate function is not approximating the"
          + " tested funtion!");
    }
  }
  
  /**
   * Estimates the constant c for a specified algorithm. This function
   * tries to estimate the c normed to the provided estimate provider.
   * If c isn't converging to a constant then this method is cancelled with
   * a <code>EstimazeException</code>.
   * 
   * @param estimateProvider The estimate provider to use
   * @param function The function to test
   * @param steps The maximum number of steps to iterate over n of the
   *     function
   * @return The estimated constant
   * @throws EstimateException The exception if this algorithm is not converging
   */
  protected static double estimateCForAlgorithm(
      Function<Long, Double> estimateProvider,
      Function<Long, Long> function, long steps)
          throws EstimateException {
    
    double lastC = 0.0D;
    double lastDelta = Double.MAX_VALUE;
    
    double average = 0.0D;
    long notConvergingSteps = 0;
    
    for (long step = 0; step < steps; step++) {
      final long startTime = System.currentTimeMillis();
      long nextStep = (step + 1);
      long stepOffset = (step + 2);
      System.out.format("Estimating c in step %d/%d.", nextStep, steps)
        .println();
      
      double newC = (function.apply(stepOffset)
          / estimateProvider.apply(stepOffset));
      
      if (step == 0) {
        average = newC;
      } else {
        if (lastDelta < (lastDelta = Math.abs(lastC - newC))) {
          notConvergingSteps++;
        }
        
        average *= step;
        average /= nextStep;
        average += newC / nextStep;
      }
      lastC = newC;
      
      if ((2 * notConvergingSteps >= steps) && (steps > 10)) {
        throw new EstimateException();
      }
      
      System.out.format("Estimated c to %.5f in step %d/%d in %d ms.",
          average, nextStep, steps,
          (System.currentTimeMillis() - startTime)).println();
      System.out.println();
    }
    
    return average;
  }
  
  /**
   * Provides a function to return the estimated runtime to compare the
   * tested algorithm runtime with.
   * 
   * @param size The data size provided to this algorithm
   * @return The estimated runtime for the algorithm
   */
  protected static double estimatedRuntime(long size) {
    return size * size * Math.log(size);
  }
  
  /**
   * Provides the algorithm to be simulated. This method should return the
   * simulated "time" this algorithm ran.
   * 
   * @param size The data size provided to this algorithm
   * @return The needed time for this algorithm
   */
  protected static long simulatedAlgorithm(long size) {
    return (size <= 1) ? 1 : (4 * simulatedAlgorithm(size / 2) + size * size);
  }
}