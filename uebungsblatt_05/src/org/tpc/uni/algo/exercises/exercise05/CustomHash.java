package org.tpc.uni.algo.exercises.exercise05;

import java.util.Random;

/**
 * Stellt eine einfache Hashfunktion mit einer Primzahl und einer begrenzten
 * Bucketgroesse bereit. Die Parameter koennen nach belieben angepasst
 * werden. 
 * 
 * @author Tobias Faller
 *
 */
public class CustomHash implements IHash<Integer, Integer> {
  
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  
  private int parameterA;
  private int parameterB;
  private int primeNumber;
  private int hashTableSize;
  
  /**
   * Erstellt eine neue instanz dieser Klasse. Die anzahl Buckets sollte
   * stets kleiner sein, als die angegebene Primzahl.
   * 
   * @param buckets Die Anzahl an Buckets, auf welche die Werte gemappt
   *     werden.
   */
  public CustomHash(int buckets) {
    hashTableSize = buckets;
  }
  
  /**
   * Wendet diese Hashfunktion auf einen gegebenen Schluessel an.
   */
  @Override
  public Integer apply(Integer value) {
    return Integer.valueOf(
        ((parameterA * value.intValue() + parameterB) % primeNumber)
        % hashTableSize);
  }
  
  /**
   * Initialisiert diese Hashfunktion mit zufaelligen parametern.
   */
  public void setRandomParameters() {
    parameterA = RANDOM.nextInt(10000);
    parameterB = RANDOM.nextInt(10000);
  }
  
  /**
   * Gibt die Primzahl dieses Hashes zurueck.
   * 
   * @return Die momentan verwendete Primzahl
   */
  public int getPrimeNumber() {
    return primeNumber;
  }
  
  /**
   * Setzt die aktuell zu verwendende Primzahl.
   * 
   * @param primeNumber Die zu nutzende Primzahl
   */
  public void setPrimeNumber(int primeNumber) {
    this.primeNumber = primeNumber;
  }
  
  /**
   * Gibt die groesse der zu verwendenden Hashtabelle an.
   * Aka die Bucketgroesse.
   * 
   * @return Gibt die groesse der Hashtabelle zurueck
   */
  public int getHashTableSize() {
    return hashTableSize;
  }
  
  /**
   * Setzt die groesse der Hashtabelle.
   * 
   * @param hashTableSize Die neue zu verwendende groesse fuer die
   *     Hashtabelle
   */
  public void setHashTableSize(int hashTableSize) {
    this.hashTableSize = hashTableSize;
  }
}