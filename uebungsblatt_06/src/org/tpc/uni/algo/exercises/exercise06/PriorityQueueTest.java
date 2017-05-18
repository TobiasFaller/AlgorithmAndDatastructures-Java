package org.tpc.uni.algo.exercises.exercise06;

import org.junit.Assert;
import org.junit.Test;

public class PriorityQueueTest {
  
  @Test
  public void testGeneral() {
    PriorityQueue<Long> queue = new PriorityQueue<>();
    
    queue.insert(103, Long.valueOf(503));
    queue.insert(100, Long.valueOf(500));
    queue.insert(102, Long.valueOf(502));
    queue.insert(101, Long.valueOf(501));
    
    Assert.assertEquals(4, queue.size());
    
    long[] expectedValues = new long[] {
        503, 502, 501, 500
    };
    
    for (int index = 0; index < expectedValues.length; index++) {
      if (queue.isEmpty()) {
        throw new AssertionError(
            "Queue is empty where it shouldn't be!");
      }
      
      Assert.assertEquals(queue.pop().longValue(), expectedValues[index]);
    }
  }
  
  @Test
  public void testRemove() {
    PriorityQueue<Long> queue = new PriorityQueue<>();
    
    queue.insert(103, Long.valueOf(503));
    queue.insert(100, Long.valueOf(500));
    PriorityQueueItem<Long> item = queue.insert(102, Long.valueOf(502));
    queue.insert(101, Long.valueOf(501));
    queue.insert(104, Long.valueOf(504));
    
    queue.remove(item);
    
    Assert.assertEquals(4, queue.size());
    
    long[] expectedValues = new long[] {
        504, 503, 501, 500
    };
    
    for (int index = 0; index < expectedValues.length; index++) {
      if (queue.isEmpty()) {
        throw new AssertionError(
            "Queue is empty where it shouldn't be!");
      }
      
      Assert.assertEquals(queue.pop().longValue(), expectedValues[index]);
    }
  }
  
  @Test
  public void testChangePriority() {
    PriorityQueue<Long> queue = new PriorityQueue<>();
    
    queue.insert(103, Long.valueOf(503));
    queue.insert(100, Long.valueOf(500));
    PriorityQueueItem<Long> item = queue.insert(102, Long.valueOf(502));
    queue.insert(101, Long.valueOf(501));
    queue.insert(104, Long.valueOf(504));
    
    queue.changePriority(item, 105);
    // or
    // item.setPriority(105);
    
    queue.insert(106, Long.valueOf(505));
    queue.insert(102, Long.valueOf(506));
    
    Assert.assertEquals(7, queue.size());
    
    long[] expectedValues = new long[] {
        505, 502, 504, 503, 506, 501, 500
    };
    
    for (int index = 0; index < expectedValues.length; index++) {
      if (queue.isEmpty()) {
        throw new AssertionError(
            "Queue is empty where it shouldn't be!");
      }
      
      Assert.assertEquals(queue.pop().longValue(), expectedValues[index]);
    }
  }
}