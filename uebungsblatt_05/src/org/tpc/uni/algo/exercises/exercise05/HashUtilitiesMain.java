package org.tpc.uni.algo.exercises.exercise05;

/**
 * Bietet eine ausfuehrbare Hauptklasse zum Testen der
 * <code>HashUtilities</code> Klasse.
 * 
 * @author Tobias Faller
 *
 */
public class HashUtilitiesMain {
  
  /**
   * Die ausfuehrbare Main-Methode dieses Programms.
   * 
   * @param args Die ignorierten uebergebenen Argumente
   */
  public static void main(String[] args) {
    CustomHash hash = new CustomHash(10);
    hash.setPrimeNumber(101);
    
    System.out.println("Universell:");
    testHash(hash);
    
    hash.setPrimeNumber(10);
    
    System.out.println();
    System.out.println("Nicht univerrsel:");
    testHash(hash);
  }
  
  /**
   * Vereinfachende Methode zur Hashanalyse.
   * 
   * @param hash Die zu pruefende Hashfunktion
   */
  private static void testHash(CustomHash hash) {
    double[] valuesC = HashUtilities.estimateCForMultipleSets(1000L, 20, 100, 10,
        hash, 1000, new Runnable() {
          @Override
          public void run() {
            hash.setRandomParameters();
          }
        });
    
    System.out.println("Min: " + valuesC[0]);
    System.out.println("Max: " + valuesC[1]);
    System.out.println("Mid: " + valuesC[2]);
  }
}