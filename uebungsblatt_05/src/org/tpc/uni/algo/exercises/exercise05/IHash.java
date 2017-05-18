package org.tpc.uni.algo.exercises.exercise05;

/**
 * Dient als Schnittstelle fuer Hashfunktionen (Java 7).
 * 
 * @author Tobias Faller
 *
 * @param <Key> Der zu verwendende Keytyp
 * @param <Mapping> Die erzeugten Schluessel der Hashtabelle
 */
public interface IHash<Key, Mapping> {
  
  /**
   * Wendet die Hashfunktion auf den gegebenen Key an.
   * 
   * @param key Den zu nutzenden Key
   * @return Der generierte Hash des Keys
   */
  public Mapping apply(Key key);
}