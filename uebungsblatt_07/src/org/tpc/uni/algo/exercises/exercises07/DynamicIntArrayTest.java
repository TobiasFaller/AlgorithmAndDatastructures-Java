package org.tpc.uni.algo.exercises.exercises07;

import org.junit.Assert;
import org.junit.Test;

public class DynamicIntArrayTest {
  
  private DynamicIntArray dynamicIntArray;
  
  public DynamicIntArrayTest() {
    dynamicIntArray = new DynamicIntArray();
  }
  
  @Test
  public void testInsert() {
    dynamicIntArray.append(5);
    dynamicIntArray.append(8);
    dynamicIntArray.append(10);
    
    Assert.assertEquals(4, dynamicIntArray.getCapacity());
    Assert.assertEquals(3, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10]", dynamicIntArray.toString());
    
    dynamicIntArray.append(20);
    
    Assert.assertEquals(4, dynamicIntArray.getCapacity());
    Assert.assertEquals(4, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10, 20]", dynamicIntArray.toString());
    
    dynamicIntArray.append(9);
    
    Assert.assertEquals(8, dynamicIntArray.getCapacity());
    Assert.assertEquals(5, dynamicIntArray.getSize());
    Assert.assertEquals("[5, 8, 10, 20, 9]", dynamicIntArray.toString());
  }
  
  @Test
  public void testRemove() {
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
    Assert.assertEquals(9, dynamicIntArray.getSize());
    Assert.assertEquals("[3, 8, 9, 10, 2, 3, 5, 6, 7]",
        dynamicIntArray.toString());
    
    dynamicIntArray.remove();
    dynamicIntArray.remove();
    dynamicIntArray.remove();
    dynamicIntArray.remove();
    Assert.assertEquals(16, dynamicIntArray.getCapacity());
    Assert.assertEquals(5, dynamicIntArray.getSize());
    Assert.assertEquals("[3, 8, 9, 10, 2]",
        dynamicIntArray.toString());
    
    dynamicIntArray.remove();
    Assert.assertEquals(8, dynamicIntArray.getCapacity());
    Assert.assertEquals(4, dynamicIntArray.getSize());
    Assert.assertEquals("[3, 8, 9, 10]",
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
  
  /**
   * Test the append method and if our reporting methods work
   * properly with list with actual content.
   */
  @Test
  public void testAppendRemove() {
    Assert.assertEquals(0, dynamicIntArray.getSize());
    Assert.assertEquals(0, dynamicIntArray.getCapacity());
    Assert.assertEquals("[]", dynamicIntArray.toString());
    
    dynamicIntArray.append(1);
    Assert.assertEquals("[1]", dynamicIntArray.toString());
    Assert.assertEquals(1, dynamicIntArray.getSize());
    
    dynamicIntArray.append(2);
    Assert.assertEquals("[1, 2]", dynamicIntArray.toString());
    Assert.assertEquals(2, dynamicIntArray.getSize());
    
    dynamicIntArray.remove();
    Assert.assertEquals("[1]", dynamicIntArray.toString());
    Assert.assertEquals(1, dynamicIntArray.getSize());
    
    dynamicIntArray.append(2);
    Assert.assertEquals("[1, 2]", dynamicIntArray.toString());
    Assert.assertEquals(2, dynamicIntArray.getSize());
    
    dynamicIntArray.append(3);
    Assert.assertEquals("[1, 2, 3]", dynamicIntArray.toString());
    
    dynamicIntArray.append(2);
    Assert.assertEquals("[1, 2, 3, 2]", dynamicIntArray.toString());
    
    dynamicIntArray.append(1);
    Assert.assertEquals("[1, 2, 3, 2, 1]", dynamicIntArray.toString());
    Assert.assertEquals(5, dynamicIntArray.getSize());
    
    dynamicIntArray.remove();
    Assert.assertEquals("[1, 2, 3, 2]", dynamicIntArray.toString());
    Assert.assertEquals(4, dynamicIntArray.getSize());
    
    dynamicIntArray.remove();
    Assert.assertEquals("[1, 2, 3]", dynamicIntArray.toString());
    Assert.assertEquals(3, dynamicIntArray.getSize());
  }
}