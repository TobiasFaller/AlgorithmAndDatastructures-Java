package org.tpc.uni.algo.exercises.exercises07;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

public class DynamicIntArrayMain {
  
  /**
   * Hilfsklasse um laufende Tests darzustellen.
   * 
   * @author Tobias Faller
   *
   */
  protected static class PerformanceTest extends Thread {
    
    private boolean stop;
    private Runnable callback;
    private TestRunner test;
    private String fileName;
    
    /**
     * Erstellt einen neuen Performanz-Test.
     * 
     * @param callback Aufzurufende funktion bei beendung des Tests
     * @param test Den aufzurufenden Test
     */
    public PerformanceTest(Runnable callback, String fileName, TestRunner test) {
      this.callback = callback;
      this.test = test;
      this.fileName = fileName;
      stop = false;
    }
    
    @Override
    public void run() {
      try (OutputStream fOut = Files.newOutputStream(
          Paths.get(fileName));
          GZIPOutputStream gzipInnerOut = new GZIPOutputStream(fOut);
          GZIPOutputStream gzipOut = new GZIPOutputStream(gzipInnerOut);
          Writer writer = new OutputStreamWriter(gzipOut);
          BufferedWriter bWriter = new BufferedWriter(writer)) {
        test.run(bWriter);
      } catch (IOException e) {
        e.printStackTrace(System.err);
      } finally {
        callback.run();
      }
    }
    
    /**
     * Emuliert das Windows-Betriebssystem.
     */
    public void stopWorking() {
      stop = true;
    }
    
    /**
     * Gibt an, ob der Test gestoppt wurde.
     * 
     * @return Status des Tests
     */
    public boolean isStopped() {
      return stop;
    }
  }
  
  private static PerformanceTest runningTest = null;
  
  /**
   * Stellt die Hauptmethode fuer einen Performanztest der Klasse
   * <code>DynamicIntArray</code> bereit. Die ausgegebenen Dateien sind zweifach
   * komprimiert um Speicherplatz zu sparen.
   * 
   * <code>120KiB -> 20KiB -> 5KiB</code>
   * 
   * @param args Die verworfenen Argumente uebergeordneter Programme
   */
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
      printHelp();
      
      while (sc.hasNextLine()) {
        String nextLine = sc.nextLine().trim().toLowerCase();
        if (nextLine.isEmpty() || nextLine.equals("help")) {
          printHelp();
        }
        
        if (runningTest != null) {
          System.out.println("Test is running!");
        }
        
        String[] commandArgs = nextLine.split(" ");
        
        if (commandArgs.length > 0) {
          if (commandArgs[0].equals("run") && commandArgs.length >= 2) {
            if (runningTest != null) {
              System.out.println("Test is already running!");
              break;
            }
            
            try {
              switch (Integer.parseInt(commandArgs[1])) {
                case 1:
                  runningTest = new PerformanceTest(() -> onTestFinish(),
                      "performanceTest1.measure",
                      (writer) -> runTest1Or2(writer, true));
                  runningTest.start();
                  continue;
                case 2:
                  runningTest = new PerformanceTest(() -> onTestFinish(),
                      "performanceTest2.measure",
                      (writer) -> runTest1Or2(writer, false));
                  runningTest.start();
                  continue;
                case 3:
                  runningTest = new PerformanceTest(() -> onTestFinish(),
                      "performanceTest3.measure",
                      (writer) -> runTest3Or4(writer, true));
                  runningTest.start();
                  continue;
                case 4:
                  runningTest = new PerformanceTest(() -> onTestFinish(),
                      "performanceTest4.measure",
                      (writer) -> runTest3Or4(writer, false));
                  runningTest.start();
                  continue;
                case 5:
                  runningTest = new PerformanceTest(() -> onTestFinish(),
                      "performanceTest5.measure",
                      (writer) -> runTest5(writer));
                  runningTest.start();
                  continue;
                default:
                  break;
              }
            } catch (NumberFormatException formatException) {
              ;
            }
            printHelp();
            
            continue;
          } else if (commandArgs[0].equals("stop")) {
            if (runningTest != null) {
              System.out.println("Stopping test ...");
              runningTest.stopWorking();
            } else {
              System.out.println("No test to stop!");
            }
            continue;
          } else if (commandArgs[0].equals("exit")) {
            break;
          }
        }
        
        System.out.println("Unknown command! Use the command help for help!");
      }
    }
  }
  
  /**
   * Wird bei beenden eines Testes aufgerufen.
   */
  private static final void onTestFinish() {
    System.out.println(runningTest.isStopped() ? "Test stopped!" :
        "Test finished!");
    runningTest = null;
  }
  
  /**
   * Fuehrt die Tests 1 oder 2 aus.
   */
  private static final void runTest1Or2(BufferedWriter writer, boolean append)
        throws IOException {
    long sum = 0;
    DynamicIntArray array;
    if (append) {
      array = new DynamicIntArray();
    } else {
      array = new DynamicIntArray(10 * 1000 * 1000);
    }
    
    for (int index = 0; (index < (10 * 1000 * 1000))
        && !runningTest.isStopped(); index++) {
      if (append) {
        array.append(0);
      } else {
        array.remove();
      }
      
      sum += array.getLastOperationTimeSpan();
      if ((index % 1000) == 0) {
        writer.append(String.valueOf(index)).append('\t')
          .append(String.valueOf(sum)).append("\r\n");
      }
    }
    
    writer.flush();
  }
  
  /**
   * Fuehrt die Tests 3 und 4 durch.
   * 
   * @param startState Gibt die Operation an, welche als erste ausgefuehrt wird.
   */
  private static final void
      runTest3Or4(BufferedWriter writer, boolean startState)
        throws IOException {
    long sum = 0;
    DynamicIntArray array = new DynamicIntArray(10 * 1000 * 1000);
    
    boolean state = startState;
    int lastSize = array.getCapacity();
    
    for (int index = 0; (index < (10 * 1000 * 1000))
        && !runningTest.isStopped(); index++) {
      
      int newSize = array.getCapacity();
      state ^= (newSize != lastSize);
      lastSize = newSize;
      
      if (state) {
        array.append(0);
      } else {
        array.remove();
      }
      
      sum += array.getLastOperationTimeSpan();
      if ((index % 1000) == 0) {
        writer.append(String.valueOf(index)).append('\t')
          .append(String.valueOf(sum)).append("\r\n");
      }
    }
    
    writer.flush();
  }
  
  /**
   * Fuehrt Test 5 aus.
   */
  private static final void runTest5(BufferedWriter writer) throws IOException {
    long sum = 0;
    WorstCaseConstantAccessTimeArray array
        = new WorstCaseConstantAccessTimeArray();
    
    for (int index = 0; (index < (10 * 1000 * 1000))
        && !runningTest.isStopped(); index++) {
      array.append(0);
      
      sum += array.getLastOperationTimeSpan();
      if ((index % 1000) == 0) {
        writer.append(String.valueOf(index)).append('\t')
          .append(String.valueOf(sum)).append("\r\n");
      }
    }
    
    writer.flush();
  }
  
  /**
   * Gibt die Hilfe zu diesem Programm aus.
   */
  private static final void printHelp() {
    System.out.println("DynamicIntArray Performance Test");
    System.out.println();
    System.out.println("\trun: runs a testcase");
    System.out.println("\tstop: stops all running tests");
    System.out.println("\texit: closes this program and terminates all test");
    System.out.println();
    System.out.println("Testcases");
    System.out.println("\t1: 10 million appends to empty array");
    System.out.println("\t2: 10 million removes to an array of size 10 million");
    System.out.println("\t3: 10 million appends/removes to an array of size 10"
        + " million; switching operation at reallocation and starting with"
        + " append");
    System.out.println("\t4: 10 million appends/removes to an array of size 10"
        + " million; switching operation at reallocation and starting with"
        + " remove");
    System.out.println();
    System.out.println("\t5: test the WorstCaseConstantAccessTimeArray with 10"
        + " million appends");
  }
}