package org.tpc.uni.algo.exercises.exercise04;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a <code>main</code> method for the <code>GeoNamesAnalyzer</code>
 * class.
 * 
 * @author Tobias Faller
 *
 */
public class GeoNamesAnalyzerMain {
  
  private static final long TEST_RUNS = 25;
  
  /**
   * The main-method called at startup.
   * 
   * @param args The ignored arguments which are provided
   */
  public static void main(String[] args) {
    System.setProperty("java.util.logging.SimpleFormatter.format",
        "[%1$tc] %4$s: %5$s%n");
    
    GeoNamesAnalyzer analyzer = new GeoNamesAnalyzer();
    
    if (Files.isRegularFile(Paths.get("allCountries.zip"))) {
      analyzer.readInfoFromZipFile(Paths.get("allCountries.zip"));
    } else if (Files.isRegularFile(Paths.get("allCountries.txt"))) {
      analyzer.readInfoFromFile(Paths.get("allCountries.txt"));
    } else {
      analyzer.readInfoFromZipUrl("http://download.geonames.org/"
          + "export/dump/allCountries.zip");
    }
    
    Logger logger = analyzer.getLogger();
    long startTime;
    long runTime;
    long totalTimeSort = 0;
    long totalTimeMap = 0;
    // long totalTimeDESort = 0;
    // long totalTimeDEMap = 0;
    
    for (long testRun = 0; testRun < TEST_RUNS; testRun++) {
      logger.log(Level.FINE, "Starting test-run " + testRun + " of "
          + TEST_RUNS + " for sort-algorithm");
      startTime = System.currentTimeMillis();
      
      // Ignore the result
      analyzer.computeMostFrequentCityNamesUsingSorting();
      
      runTime = (System.currentTimeMillis() - startTime);
      totalTimeSort += runTime;
      logger.log(Level.FINE, "Finished test-run " + testRun + " of "
          + TEST_RUNS + " for sort-algorithm in "
          + runTime + " ms");
    }
    
    for (long testRun = 0; testRun < TEST_RUNS; testRun++) {
      logger.log(Level.FINE, "Starting test-run " + testRun + " of "
          + TEST_RUNS + " for map-algorithm");
      startTime = System.currentTimeMillis();
      
      // Ignore the result
      analyzer.computeMostFrequentCityNamesUsingMap();
      
      runTime = (System.currentTimeMillis() - startTime);
      totalTimeMap += runTime;
      logger.log(Level.FINE, "Finished test-run " + testRun + " of "
          + TEST_RUNS + " for map-algorithm in " + runTime + " ms");
    }
    
    // for (long testRun = 0; testRun < TEST_RUNS; testRun++){
    //   logger.log(Level.FINE, "Starting test-run " + testRun + " of "
    //       + TEST_RUNS + " for sort-de-algorithm");
    //   startTime = System.currentTimeMillis();
    //   
    //   // Ignore the result
    //   analyzer.computeMostFrequentCityNamesWithDEUsingSorting();
    //   
    //   runTime = (System.currentTimeMillis() - startTime);
    //   totalTimeDESort += runTime;
    //   logger.log(Level.FINE, "Finished test-run " + testRun + " of "
    //       + TEST_RUNS + " for sort-de-algorithm in " +
    //       runTime + " ms");
    // }
    // 
    // for (long testRun = 0; testRun < TEST_RUNS; testRun++){
    //   logger.log(Level.FINE, "Starting test-run " + testRun + " of "
    //       + TEST_RUNS + " for map-de-algorithm");
    //   startTime = System.currentTimeMillis();
    //   
    //   // Ignore the result
    //   analyzer.computeMostFrequentCityNamesWithDEUsingMap();
    //   
    //   runTime = (System.currentTimeMillis() - startTime);
    //   totalTimeDEMap += runTime;
    //   logger.log(Level.FINE, "Finished test-run " + testRun + " of "
    //       + TEST_RUNS + " for map-de-algorithm in " +
    //       runTime + " ms");
    // }
    
    final Collection<CityCount> cityCountList = analyzer
        .computeMostFrequentCityNamesUsingMap();
    final Collection<CityCount> cityDeCount = analyzer
        .computeMostFrequentCityNamesWithDeUsingMap();
    
    logger.log(Level.INFO, "Time for sort-algorithm: "
        + ((double) totalTimeSort / (double) TEST_RUNS));
    logger.log(Level.INFO, "Time for map-algorithm: "
        + ((double) totalTimeMap / (double) TEST_RUNS));
    // logger.log(Level.INFO, "Time for sort-de-algorithm: " 
    //    + ((double) totalTimeDESort / (double) TEST_RUNS));
    // logger.log(Level.INFO, "Time for map-de-algorithm: "
    //    + ((double) totalTimeDEMap / (double) TEST_RUNS));
    
    
    Iterator<CityCount> cityCountIterator = cityCountList.iterator();
    for (int index = 0; (index < 3) && cityCountIterator.hasNext(); index++) {
      logger.log(Level.INFO, "City name no." + (index + 1) + ": "
          + cityCountIterator.next().toString());
    }
    
    cityCountIterator = cityDeCount.iterator();
    for (int index = 0; (index < 3) && cityCountIterator.hasNext(); index++) {
      logger.log(Level.INFO, "City name in DE no." + (index + 1) + ": "
          + cityCountIterator.next().toString());
    }
  }
}