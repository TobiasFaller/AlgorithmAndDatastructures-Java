package org.tpc.uni.algo.exercises.exercise05;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Stellt Hilfsfunktionen zur analyse von Hashfunktionen bereit.
 * 
 * @author Tobias Faller
 *
 */
public class HashUtilities {
  
  private static final Random RANDOM = new Random(System.currentTimeMillis());
  
  /**
   * Berechnet die durchschnittliche Bucketgroesse fuer eine gegebene
   * Hashfunktion. Dabei werden unbelegte Buckets nicht beruecksichtigt,
   * da diese nicht zu Ausgabemenge gehoeren.
   * 
   * @param keys Die Keys, welche als Eingabe dienen koennen
   * @param hash Die Hashfunktion, welche die Keys transformiert
   * @return Die durchschnittliche Bucketbesetztung
   */
  public static <MappingT, KeyT> double
      meanBucketSize(Iterable<KeyT> keys, IHash<KeyT, MappingT> hash) {
    
    Map<MappingT, Counter> mappings = new TreeMap<>();
    
    for (KeyT key : keys) {
      MappingT bucket = hash.apply(key);
      
      if (mappings.containsKey(bucket)) {
        mappings.get(bucket).increment();
      } else {
        mappings.put(bucket, Counter.create(1));
      }
    }
    
    long sum = 0;
    long count = 0;
    for (Counter counter : mappings.values()) {
      sum += counter.getValue();
      count++;
    }
    
    return ((double) sum / (double) count);
  }
  
  /**
   * Berechnet die konstante c fuer eine gegebene Hashfunktion.
   * Dabei wird fuer jeden gegebenen Schluessel der entsprechende Hashwert
   * ermittelt. Der Parameter iterationCount gibt die Anzahl der
   * auszufuehrenden wiederholungen mit zufaelligen Werten an.
   * Vor jeder wiederholung wird die Funktion randomizer aufgerufen, um
   * den Hashmit zufaelligen Werten zu intialisieren.
   * 
   * @param keys Die Schluesselmenge, welche zur Berechnung genutzt wird
   * @param hash Die Hashfunktion, welche als Tranformationsfunktion genutzt
   *     wird
   * @param iterationCount Anzahl an zufaellig generierten Hashfunktionen
   * @param buckets Anzahl an Buckets, auf die die Hashfunktion potentiell
   *     mappen kann
   * @param randomizer Funktion um die Hashfunktion mit zufaelligen Werten
   *     zu initialisieren
   * @return Das abgeschaetzte c fuer die gegebene Hashfunktion
   */
  public static <MappingT, KeyT> double estimateCForSingleSet(
        Iterable<KeyT> keys, long buckets,
        IHash<KeyT, MappingT> hash, long iterationCount,
        Runnable randomizer) {
    
    Set<KeyT> keySet = new HashSet<>();
    for (KeyT key : keys) {
      keySet.add(key);
    }
    
    double averageC = 0.0D;
    final Map<MappingT, Counter> mappings = new TreeMap<>();
    for (long index = iterationCount; index > 0; index--) {
      mappings.clear();
      
      randomizer.run();
      
      for (KeyT key : keySet) {
        MappingT bucket = hash.apply(key);
        
        if (mappings.containsKey(bucket)) {
          mappings.get(bucket).increment();
        } else {
          mappings.put(bucket, Counter.create(1));
        }
      }
      
      long sum = 0;
      long count = 0;
      for (Counter counter : mappings.values()) {
        sum += counter.getValue();
        count++;
      }
      
      double averageBucketSize = ((double) sum / (double) count);
      double valueC = (averageBucketSize - 1.0D) / keySet.size() * buckets;
      
      averageC += (valueC / iterationCount);
    }
    
    return averageC;
  }
  
  /**
   * Berechnet fuer einen gegebenen Hash den mittleren, oberen und unteren
   * Wert fuer c. Die Funktion <code>estimateCForSingleSet</code> wird
   * dabei fuer ein zufaellig generiertes Schluesselset aufgerufen.
   * Dabei wird fuer jeden gegebenen Schluessel der entsprechende
   * Hashwert ermittelt. Der Parameter iterationCount gibt die Anzahl der
   * auszufuehrenden wiederholungen mit zufaelligen Werten an.
   * Vor jeder wiederholung wird die Funktion randomizer aufgerufen, um
   * den Hashmit zufaelligen Werten zu intialisieren.
   * 
   * @param numSets Die Anzahl an verschiedenen Durchlaufen, welche die
   *     Funktion macht
   * @param setSize Die groesse des key-sets
   * @param keys Die groesse des key-universums
   * @param buckets Anzahl an Buckets, auf die die Hashfunktion potentiell
   *     mappen kann
   * @param hash Die Hashfunktion, welche als Tranformationsfunktion genutzt
   *     wird
   * @param iterationCount Anzahl an zufaellig generierten Hashfunktionen
   * @param randomizer Funktion um die Hashfunktion mit zufaelligen Werten
   *     zu initialisieren
   * @return Das abgeschaetzte c fuer die gegebene Hashfunktion
   */
  public static <MappingT> double[] estimateCForMultipleSets(
      long numSets, int setSize, int keys, long buckets,
      IHash<Integer, MappingT> hash, long iterationCount,
      Runnable randomizer) {
    
    Set<Integer> possibleKeys = new HashSet<>();
    for (int number = keys - 1; number >= 0; number--) {
      possibleKeys.add(new Integer(number));
    }
    
    double minC = Double.MAX_VALUE;
    double maxC = Double.MIN_VALUE;
    double sumC = 0;
    for (long setNumber = (numSets - 1); setNumber >= 0; setNumber--) {
      List<Integer> workingCopy = new ArrayList<>(possibleKeys);
      Set<Integer> chosenKeys = new HashSet<>();
      for (int i = (setSize - 1); i >= 0; i--) {
        chosenKeys.add(
            workingCopy.remove(RANDOM.nextInt(workingCopy.size())));
      }
      
      double valueC = estimateCForSingleSet(chosenKeys, buckets, hash,
          iterationCount, randomizer);
      
      sumC += valueC;
      minC = Math.min(minC, valueC);
      maxC = Math.max(maxC, valueC);
    }
    
    return new double[] {
        minC, maxC, (sumC / (double) numSets)
        };
  }
}