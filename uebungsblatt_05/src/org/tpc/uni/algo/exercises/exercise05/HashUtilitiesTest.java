package org.tpc.uni.algo.exercises.exercise05;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

/**
 * Stellt Tests fuer die HashUtilities-Klasse bereit.
 * 
 * @author Tobias Faller
 *
 */
public class HashUtilitiesTest {
  
  /**
   * Testet die Methode <code>meanBucketSize</code> der Klasse
   * <code>HashUtilities</code>.
   */
  @Test
  public void testMeanBucketSize() {
    CustomHash hash = new CustomHash(10);
    hash.setRandomParameters();
    
    Collection<Integer> keys = new ArrayList<>(100);
    for (int number = 0; number < 100; number++) {
      keys.add(new Integer(number));
    }
    
    hash.setPrimeNumber(101);
    
    Assert.assertEquals(10.0D, HashUtilities.meanBucketSize(keys, hash),
        0.001D);
  }
  
  /**
   * Testet die Methode <code>estimateCForSingleSet</code> der Klasse
   * <code>HashUtilities</code>.
   */
  @Test
  public void testEstimateCForSingleSet() {
    CustomHash hash = new CustomHash(10);
    
    Collection<Integer> keys = new ArrayList<>(100);
    for (int number = 0; number < 100; number++) {
      keys.add(new Integer(number));
    }
    
    hash.setPrimeNumber(101);
    
    // Java 8
    // HashUtilities.estimateCForSingleSet(keys, 10, hash, 10000,
    //     hash::setRandomParameters);
    
    double valueC = HashUtilities.estimateCForSingleSet(keys, 10, hash, 10000,
        new Runnable() {
          @Override
          public void run() {
            hash.setRandomParameters();
          }
        });
    
    // c sollte nahe an dem idealen Wert liegen, daher ~1
    Assert.assertEquals(1.0D, valueC, 0.1D);
  }
  
  /**
   * Testet die Methode <code>estimateCForMultipleSets</code> der Klasse
   * <code>HashUtilities</code>.
   */
  @Test
  public void testEstimateCForMultipleSets() {
    CustomHash hash = new CustomHash(10);
    hash.setPrimeNumber(101);
    
    double[] valueC = HashUtilities.estimateCForMultipleSets(20, 100, 500, 10,
        hash, 10000, new Runnable() {
          @Override
          public void run() {
            hash.setRandomParameters();
          }
        });
    
    // c_mid sollte nahe an dem idealen Wert liegen, daher ~1
    Assert.assertEquals(1.0D, valueC[2], 0.1D);
    
    System.out.println("Min: " + valueC[0]);
    System.out.println("Max: " + valueC[1]);
    System.out.println("Mid: " + valueC[2]);
  }
}