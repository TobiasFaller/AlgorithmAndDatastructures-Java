package org.tpc.uni.algo.exercises.exercise04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Provides a analyzer of most-used city-names based on a textual database.
 * 
 * @author Tobias Faller
 *
 */
public class GeoNamesAnalyzer {
  
  protected class Tupel<FirstT, SecondT> {
    
    private FirstT first;
    private SecondT second;
    
    public Tupel(FirstT first, SecondT second) {
      this.first = first;
      this.second = second;
    }
    
    public FirstT getFirst() {
      return first;
    }
    
    public SecondT getSecond() {
      return second;
    }
    
    public void setFirst(FirstT first) {
      this.first = first;
    }
    
    public void setSecond(SecondT second) {
      this.second = second;
    }
  }
  
  private Logger logger;
  private Collection<CityInfo> cities;
  
  /**
   * Creates a new <code>GeoNamesAnalyzer</code> with no cities loaded.
   */
  public GeoNamesAnalyzer() {
    logger = Logger.getLogger(getClass().getName());
    logger.setLevel(Level.FINEST);
    
    cities = new LinkedList<>();
    
    try {
      FileHandler handler = new FileHandler("GeoNamesAnalyzer_%g_%u.log");
      handler.setFormatter(new SimpleFormatter());
      logger.addHandler(handler);
    } catch (SecurityException | IOException e) {
      logger.log(Level.WARNING, "Could not initialize file-logger!");
    }
  }
  
  /**
   * Reads the city-names from a file.
   * 
   * @param file The path to the file to load
   */
  public void readInfoFromFile(Path file) {
    try (InputStream in = Files.newInputStream(file)) {
      logger.info("Loading data from " + file.toString());
      
      long start = System.currentTimeMillis();
      readInfoFromStream(in);
      
      logger.info("Loaded data from " + file.toString() + " in "
          + (System.currentTimeMillis() - start) + " ms");
    } catch (IOException ioException) {
      logger.log(Level.WARNING, "Could not load data", ioException);
    }
  }
  
  /**
   * Reads the city-names from a zip-file.
   * 
   * @param file The path to the file to load
   */
  public void readInfoFromZipFile(Path file) {
    try (InputStream in = Files.newInputStream(file);
        ZipInputStream zIn = new ZipInputStream(in)) {
      ZipEntry entry;
      while ((zIn.available() > 0) && ((entry = zIn.getNextEntry()) != null)) {
        String name = entry.getName();
        if (name.endsWith("/") || !name.endsWith(".txt")) {
          continue;
        }
        
        logger.info("Loading " + name + " from zip-file " + file.toString());
        long startTime = System.currentTimeMillis();
        try {
          readInfoFromStream(zIn);
          logger.info("Loaded " + name + " from zip-file " + file.toString()
              + " in " + (System.currentTimeMillis() - startTime) + " ms");
        } catch (IOException innerIoException) {
          logger.log(Level.WARNING, "Could not load " + name + " from zip-file "
              + file.toString(), innerIoException);
          return;
        } finally {
          try {
            zIn.closeEntry();
          } catch (IOException ioException) {
            // Ignore
          }
        }
      }
    } catch (IOException ioException) {
      logger.log(Level.WARNING, "Could not load data from zip-file "
          + file.toString(), ioException);
    }
  }
  
  /**
   * Reads the city-names from a zip-stream.
   * 
   * @param in The stream to read from
   */
  public void readInfoFromZipStream(InputStream in) {
    try (ZipInputStream zIn = new ZipInputStream(in)) {
      ZipEntry entry;
      while ((zIn.available() > 0) && ((entry = zIn.getNextEntry()) != null)) {
        String name = entry.getName();
        if (name.endsWith("/") || !name.endsWith(".txt")) {
          continue;
        }
        
        logger.info("Loading " + name + " from zip-stream");
        long startTime = System.currentTimeMillis();
        try {
          readInfoFromStream(zIn);
          logger.info("Loaded " + name + " from zip-stream in "
              + (System.currentTimeMillis() - startTime) + " ms");
        } catch (IOException innerIoException) {
          logger.log(Level.WARNING, "Could not load " + name
              + " from zip-stream", innerIoException);
          return;
        } finally {
          try {
            zIn.closeEntry();
          } catch (IOException ioException) {
            // Ignore
          }
        }
      }
    } catch (IOException ioException) {
      logger.log(Level.WARNING, "Could not load data from zip-stream",
          ioException);
    }
  }
  
  /**
   * Reads the city-names from a stream.
   * 
   * @param in The stream to read from
   */
  public void readInfoFromStream(InputStream in) throws IOException {
    try (InputStream ncIn = new NonChainingInputStream(in);
        Reader reader = new InputStreamReader(ncIn, StandardCharsets.UTF_8);
        BufferedReader bReader = new BufferedReader(reader)) {
      
      String line;
      while ((line = bReader.readLine()) != null) {
        if ((line = line.trim()).isEmpty() || line.startsWith("#")) {
          continue; // Ignore empty and starting with #
        }
        
        String[] data = line.split("\t");
        if (data.length < 15) {
          continue; // Line too short
        }
        
        String type = data[6];
        if ((type == null)
            || (type = type.trim()).isEmpty() || !type.equalsIgnoreCase("P")) {
          continue; // Invalid type
        }
        
        String countString = data[14];
        if ((countString == null)
            || (countString = countString.trim()).isEmpty()) {
          continue; // No inhabitants
        }
        
        long count;
        try {
          count = Long.parseLong(countString);
        } catch (NumberFormatException e) {
          continue; // Invalid number
        }
        
        if (count <= 0) {
          continue; // No inhabitants
        }
        
        String name = data[1];
        String code = data[8];
        if ((name == null) || (name = name.trim()).isEmpty()
            || (code == null) || (code = code.trim()).isEmpty()) {
          continue; // No name or country-code
        }
        
        cities.add(new CityInfo(name, code));
      }
    }
  }
  
  /**
   * Reads the city-names from a url-stream.
   * 
   * @param address The address to read the file from
   */
  public void readInfoFromUrl(String address) {
    try {
      URL url = new URL(address);
      
      try (InputStream in = url.openStream()) {
        logger.info("Loading data from url " + address);
        long startTime = System.currentTimeMillis();
        
        readInfoFromStream(in);
        
        logger.info("Loaded data from url " + address + " in "
            + (System.currentTimeMillis() - startTime) + " ms");
      } catch (IOException ioException) {
        logger.log(Level.WARNING, "Could not load data from url " + address,
            ioException);
      }
    } catch (MalformedURLException malformedUrlException) {
      logger.log(Level.WARNING, "Could not load data from invalid url "
          + address, malformedUrlException);
    }
  }
  
  /**
   * Reads the city-names from a url-stream compressed in zip-format.
   * 
   * @param address The address to read the file from
   */
  public void readInfoFromZipUrl(String address) {
    try {
      URL url = new URL(address);
      
      try (InputStream in = url.openStream()) {
        logger.info("Loading data from zip-url " + address);
        long startTime = System.currentTimeMillis();
        
        readInfoFromZipStream(in);
        
        logger.info("Loaded data from zip-url " + address + " in "
            + (System.currentTimeMillis() - startTime) + " ms");
      } catch (IOException ioException) {
        logger.log(Level.WARNING, "Could not load data from zip-url "
            + address, ioException);
      }
    } catch (MalformedURLException malformedUrlException) {
      logger.log(Level.WARNING, "Could not load data from invalid zip-url "
          + address, malformedUrlException);
    }
  }
  
  /**
   * Computes the most frequent city names based on a sort-algorithm.
   * 
   * @return The city-name information in descending order
   */
  public Collection<CityCount> computeMostFrequentCityNamesUsingSorting() {
    final List<CityInfo> cities = new ArrayList<>(getCities().size());
    cities.addAll(getCities());
    
    logger.log(Level.FINE, "Started sorting city-name list");
    long startTime = System.currentTimeMillis();
    Collections.sort(cities);
    logger.log(Level.FINE, "Finished sorting city-name list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    final List<CityCount> cityCount = new LinkedList<>();
    final Iterator<CityInfo> cityInfoIterator = cities.iterator();
    
    logger.log(Level.FINE, "Started creation of city-name-count list");
    startTime = System.currentTimeMillis();
    
    CityInfo currentCityInfo;
    CityCount currentCityCount = null;
    while (cityInfoIterator.hasNext()) {
      currentCityInfo = cityInfoIterator.next();
      
      if ((currentCityCount == null)
          || !currentCityCount.getName().equals(currentCityInfo.getName())) {
        currentCityCount = new CityCount(currentCityInfo.getName());
        cityCount.add(currentCityCount);
      } else {
        currentCityCount.increment();
      }
    }
    
    logger.log(Level.FINE, "Finished creation of city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    logger.log(Level.FINE, "Started sorting city-name-count list");
    startTime = System.currentTimeMillis();
    Collections.sort(cityCount, Comparator.reverseOrder());
    logger.log(Level.FINE, "Finished sorting city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    return cityCount;
  }
  
  /**
   * Computes the most frequent city names based on a map-algorithm.
   * 
   * @return The city-name information in descending order
   */
  public Collection<CityCount> computeMostFrequentCityNamesUsingMap() {
    final Map<String, CityCount> cityCounts = new TreeMap<>();
    final Iterator<CityInfo> cityInfoIterator = cities.iterator();
    
    logger.log(Level.FINE, "Started creation of city-name map");
    long startTime = System.currentTimeMillis();
    
    CityInfo currentCityInfo;
    String currentCityName;
    while (cityInfoIterator.hasNext()) {
      currentCityInfo = cityInfoIterator.next();
      currentCityName = currentCityInfo.getName();
      
      if (!cityCounts.containsKey(currentCityName)) {
        cityCounts.put(currentCityName, new CityCount(currentCityName));
      } else {
        cityCounts.get(currentCityName).increment();
      }
    }
    
    logger.log(Level.FINE, "Finished creation of city-name map in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    final Collection<CityCount> unorderedCityCounts = cityCounts.values();
    final List<CityCount> orderedCityCounts
        = new ArrayList<>(unorderedCityCounts.size());
    orderedCityCounts.addAll(unorderedCityCounts);
    
    logger.log(Level.FINE, "Started sorting city-name-count list");
    startTime = System.currentTimeMillis();
    Collections.sort(orderedCityCounts, Comparator.reverseOrder());
    logger.log(Level.FINE, "Finished sorting city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    return orderedCityCounts;
  }
  
  /**
   * Computes the most frequent city names based on a sort-algorithm.
   * This method excludes all city-names not used in Germany.
   * 
   * @return The city-name information in descending order
   */
  public Collection<CityCount>
        computeMostFrequentCityNamesWithDeUsingSorting() {
    final List<CityInfo> cities = new ArrayList<>(getCities().size());
    cities.addAll(getCities());
    
    logger.log(Level.FINE, "Started sorting city-name list");
    long startTime = System.currentTimeMillis();
    Collections.sort(cities);
    logger.log(Level.FINE, "Finished sorting city-name list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    final List<CityCount> cityCount = new LinkedList<>();
    final Iterator<CityInfo> cityInfoIterator = cities.iterator();
    
    logger.log(Level.FINE, "Started creation of city-name-count list");
    startTime = System.currentTimeMillis();
    
    CityInfo currentCityInfo;
    CityCount currentCityCount = null;
    String currentCityName;
    
    boolean hasDe = false;
    while (cityInfoIterator.hasNext()) {
      currentCityInfo = cityInfoIterator.next();
      currentCityName = currentCityInfo.getName();
      
      if ((currentCityCount == null)
          || !currentCityCount.getName().equals(currentCityName)) {
        currentCityCount = new CityCount(currentCityName);
        hasDe = false;
      } else {
        currentCityCount.increment();
      }
      
      if (!hasDe && currentCityInfo.getCountryName().equalsIgnoreCase("DE")) {
        hasDe = true;
        cityCount.add(currentCityCount);
      }
    }
    
    logger.log(Level.FINE, "Finished creation of city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    logger.log(Level.FINE, "Started sorting city-name-count list");
    startTime = System.currentTimeMillis();
    Collections.sort(cityCount, Comparator.reverseOrder());
    logger.log(Level.FINE, "Finished sorting city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    return cityCount;
  }
  
  /**
   * Computes the most frequent city names based on a map-algorithm.
   * This method excludes all city-names not used in Germany.
   * 
   * @return The city-name information in descending order
   */
  public Collection<CityCount> computeMostFrequentCityNamesWithDeUsingMap() {
    final Map<String, Tupel<Boolean, CityCount>> cityCounts = new TreeMap<>();
    final Iterator<CityInfo> cityInfoIterator = cities.iterator();
    
    logger.log(Level.FINE, "Started creation of city-name map");
    long startTime = System.currentTimeMillis();
    
    CityInfo currentCityInfo;
    Tupel<Boolean, CityCount> currentTupel;
    String currentCityName;
    while (cityInfoIterator.hasNext()) {
      currentCityInfo = cityInfoIterator.next();
      currentCityName = currentCityInfo.getName();
      
      if (!cityCounts.containsKey(currentCityName)) {
        currentTupel
          = new Tupel<>(Boolean.FALSE, new CityCount(currentCityName));
        cityCounts.put(currentCityName, currentTupel);
      } else {
        currentTupel = cityCounts.get(currentCityName);
        currentTupel.getSecond().increment();
      }
      
      if (!currentTupel.getFirst().booleanValue()
          && currentCityInfo.getCountryName().equalsIgnoreCase("DE")) {
        currentTupel.setFirst(Boolean.TRUE);
      }
    }
    
    logger.log(Level.FINE, "Finished creation of city-name map in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    final Collection<Tupel<Boolean, CityCount>> unorderedCityCounts
        = cityCounts.values();
    
    logger.log(Level.FINE, "Started filtering city-name list");
    startTime = System.currentTimeMillis();
    
    final List<CityCount> filteredCityCounts = new LinkedList<>();
    for (Tupel<Boolean, CityCount> t : unorderedCityCounts) {
      if (t.getFirst().booleanValue()) {
        filteredCityCounts.add(t.getSecond());
      }
    }
    
    logger.log(Level.FINE, "Finished filtering city-name list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    
    final List<CityCount> orderedCityCounts
        = new ArrayList<>(filteredCityCounts.size());
    orderedCityCounts.addAll(filteredCityCounts);
    
    logger.log(Level.FINE, "Started sorting city-name-count list");
    startTime = System.currentTimeMillis();
    Collections.sort(orderedCityCounts, Comparator.reverseOrder());
    logger.log(Level.FINE, "Finished sorting city-name-count list in "
        + (System.currentTimeMillis() - startTime) + " ms");
    
    return orderedCityCounts;
  }
  
  /**
   * Returns all loaded cities.
   * 
   * @return All loaded cities
   */
  public Collection<CityInfo> getCities() {
    return Collections.unmodifiableCollection(cities);
  }
  
  /**
   * Wipes all loaded city-data from the internal storage.
   * 
   * @return This instance
   */
  public GeoNamesAnalyzer reset() {
    cities.clear();
    
    return this;
  }
  
  /**
   * Returns the logger used by this analyzer.
   * 
   * @return The logger currently used
   */
  public Logger getLogger() {
    return logger;
  }
}