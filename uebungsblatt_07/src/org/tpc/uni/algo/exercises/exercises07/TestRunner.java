package org.tpc.uni.algo.exercises.exercises07;

import java.io.BufferedWriter;
import java.io.IOException;

@FunctionalInterface
public interface TestRunner {
  
  public void run(BufferedWriter writer) throws IOException;
  
}