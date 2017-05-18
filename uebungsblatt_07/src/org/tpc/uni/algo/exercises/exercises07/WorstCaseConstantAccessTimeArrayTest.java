package org.tpc.uni.algo.exercises.exercises07;

import org.junit.Assert;
import org.junit.Test;

public class WorstCaseConstantAccessTimeArrayTest {
  
  private WorstCaseConstantAccessTimeArray dynamicIntArray;
  
  public WorstCaseConstantAccessTimeArrayTest() {
    dynamicIntArray = new WorstCaseConstantAccessTimeArray();
  }
  
  @Test
  public void testAppend() {
    dynamicIntArray.append(5);
    dynamicIntArray.append(8);
    dynamicIntArray.append(10);
    
    Assert.assertEquals(4, dynamicIntArray.getCapacity());
    Assert.assertEquals(8, dynamicIntArray.getLargeCapacity());
    Assert.assertEquals(3, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10]", dynamicIntArray.toString());
    
    dynamicIntArray.append(20);
    
    Assert.assertEquals(4, dynamicIntArray.getCapacity());
    Assert.assertEquals(8, dynamicIntArray.getLargeCapacity());
    Assert.assertEquals(4, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10, 20]", dynamicIntArray.toString());
    
    dynamicIntArray.append(9);
    
    Assert.assertEquals(8, dynamicIntArray.getCapacity());
    Assert.assertEquals(16, dynamicIntArray.getLargeCapacity());
    Assert.assertEquals(5, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10, 20, 9]", dynamicIntArray.toString());
  }
  
  @Test
  public void testSimpleAppend() {
    dynamicIntArray.append(3);
    dynamicIntArray.append(8);
    dynamicIntArray.append(9);
    dynamicIntArray.append(10);
    dynamicIntArray.append(2);
    dynamicIntArray.append(3);
    dynamicIntArray.append(5);
    dynamicIntArray.append(6);
    dynamicIntArray.append(7);
    
    Assert.assertEquals(16, dynamicIntArray.getCapacity());
    Assert.assertEquals(32, dynamicIntArray.getLargeCapacity());
    Assert.assertEquals(9, dynamicIntArray.getSize());
    Assert.assertEquals("[3, 8, 9, 10, 2, 3, 5, 6, 7]",
        dynamicIntArray.toString());
  }
  
  /**
   * Test the constructor and if 
   * methods getSize(), getCapacity() and toString()
   * work as expected on such a fresh instance.
   */
  @Test
  public void testFreshList() {
    Assert.assertEquals(0, dynamicIntArray.getSize());
    Assert.assertEquals(0, dynamicIntArray.getCapacity());
    Assert.assertEquals("[]", dynamicIntArray.toString());
  }
}