package org.tpc.uni.algo.exercises.exercises13.queue;

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
		
		long[] expectedValues = new long[] { 500, 501, 502, 503 };
		
		for (int index = 0; index < expectedValues.length; index++) {
			if (queue.isEmpty()) {
				throw new AssertionError("Queue is empty where it shouldn't be!");
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
		
		long[] expectedValues = new long[] { 500, 501, 503, 504 };
		
		for (int index = 0; index < expectedValues.length; index++) {
			if (queue.isEmpty()) {
				throw new AssertionError("Queue is empty where it shouldn't be!");
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
		
		long[] expectedValues = new long[] { 500, 501, 506, 503, 504, 502, 505 };
		
		for (int index = 0; index < expectedValues.length; index++) {
			if (queue.isEmpty()) {
				throw new AssertionError("Queue is empty where it shouldn't be!");
			}
			
			Assert.assertEquals(queue.pop().longValue(), expectedValues[index]);
		}
	}
}