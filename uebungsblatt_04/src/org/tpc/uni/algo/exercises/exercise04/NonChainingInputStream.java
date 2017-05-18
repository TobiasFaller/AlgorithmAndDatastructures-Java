package org.tpc.uni.algo.exercises.exercise04;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides an <code>InputStream</code>-class which mirrors all operations
 * exception the <code>close</code> operation on the sub-stream which
 * it is reading from.
 * 
 * @author Tobias Faller
 *
 */
public class NonChainingInputStream extends InputStream {
  
  private InputStream in;
  
  public NonChainingInputStream(InputStream in) {
    this.in = in;
  }
  
  @Override
  public int read() throws IOException {
    return in.read();
  }
  
  @Override
  public int read(byte[] array, int off, int len) throws IOException {
    return in.read(array, off, len);
  }
  
  @Override
  public int read(byte[] array) throws IOException {
    return in.read(array);
  }
  
  @Override
  public int available() throws IOException {
    return in.available();
  }
  
  @Override
  public boolean markSupported() {
    return in.markSupported();
  }
  
  @Override
  public synchronized void mark(int readlimit) {
    in.mark(readlimit);
  }
  
  @Override
  public synchronized void reset() throws IOException {
    in.reset();
  }
  
  @Override
  public long skip(long count) throws IOException {
    return in.skip(count);
  }
  
  @Override
  public void close() throws IOException {
    // Do nothing
  }
}