package org.tpc.uni.algo.exercises.exercises09;

/**
 * Provides an exception interface for not approximable functions.
 * 
 * @author Tobias Faller
 *
 */
public class EstimateException extends Exception {
  
  private static final long serialVersionUID = -188480761324482980L;
  
  public EstimateException() { }
  
  public EstimateException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public EstimateException(String message) {
    super(message);
  }
  
  public EstimateException(Throwable cause) {
    super(cause);
  }
}