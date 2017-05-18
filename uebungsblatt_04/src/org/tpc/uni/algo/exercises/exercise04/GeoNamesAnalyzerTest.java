package org.tpc.uni.algo.exercises.exercise04;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Provides basic test for the <code>GeoNamesAnalyzer</code> class.
 * 
 * @author Tobias Faller
 *
 */
public class GeoNamesAnalyzerTest {
  
  private static final String[] PARTIAL_RESULTS
    = { "K\u00f6ln: 4", "Freiburg: 3", "Fruiburg: 3" };
  private static final String[] PARTIAL_RESULTS_DE
    = { "K\u00f6ln: 4", "Freiburg: 3", "Friburg: 2" };
  
  private static GeoNamesAnalyzer analyzer;
  
  /**
   * Initializes the test-class with the necessary test-data.
   */
  @BeforeClass
  public static void init() {
    analyzer = new GeoNamesAnalyzer();
    analyzer.readInfoFromFile(Paths.get("JUnitData.txt"));
    
    Assert.assertEquals(16, analyzer.getCities().size());
    
    // if (Files.isRegularFile(Paths.get("allCountries.zip"))) {
    //   analyzer.readInfoFromZipFile(Paths.get("allCountries.zip"));
    // } else if (Files.isRegularFile(Paths.get("allCountries.txt"))) {
    //   analyzer.readInfoFromFile(Paths.get("allCountries.txt"));
    // } else {
    //   analyzer.readInfoFromZipUrl("http://download.geonames.org"
    //     + "/export/dump/allCountries.zip");
    // }
  }
  
  /**
   * Test the class for the correct result with all country-names.
   */
  @Test
  public void testGeoNamesAnalyzer() {
    Collection<CityCount> cityCount = analyzer
        .computeMostFrequentCityNamesUsingMap();
    Collection<CityCount> cityCountSort = analyzer
        .computeMostFrequentCityNamesUsingSorting();
    
    Assert.assertEquals(8, cityCount.size());
    Assert.assertEquals(8, cityCountSort.size());
    
    Iterator<CityCount> iterator = cityCount.iterator();
    Iterator<CityCount> iteratorSort = cityCountSort.iterator();
    for (String result : PARTIAL_RESULTS) {
      if (!iterator.hasNext() || !iteratorSort.hasNext()) {
        Assert.fail("Output is too short");
      }
      
      Assert.assertEquals(result, iterator.next().toString());
      Assert.assertEquals(result, iteratorSort.next().toString());
    }
  }
  
  /**
   * Test the class for the correct result with all country-names occurring
   * in Germany.
   */
  @Test
  public void testGeoNamesAnalyzerDe() {
    Collection<CityCount> cityCount = analyzer
        .computeMostFrequentCityNamesWithDeUsingMap();
    Collection<CityCount> cityCountSort = analyzer
        .computeMostFrequentCityNamesWithDeUsingSorting();
    
    Assert.assertEquals(6, cityCount.size());
    Assert.assertEquals(6, cityCountSort.size());
    
    Iterator<CityCount> iterator = cityCount.iterator();
    Iterator<CityCount> iteratorSort = cityCountSort.iterator();
    for (String result : PARTIAL_RESULTS_DE) {
      if (!iterator.hasNext() || !iteratorSort.hasNext()) {
        Assert.fail("Output is too short");
      }
      
      Assert.assertEquals(result, iterator.next().toString());
      Assert.assertEquals(result, iteratorSort.next().toString());
    }
  }
}