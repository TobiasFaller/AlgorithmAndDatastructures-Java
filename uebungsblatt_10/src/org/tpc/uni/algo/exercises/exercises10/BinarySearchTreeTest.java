package org.tpc.uni.algo.exercises.exercises10;

import org.junit.Assert;
import org.junit.Test;

/**
 * Provides test methods for the implamented <code>BinarySearchTree</code>.
 * 
 * @author Tobias Faller
 *
 */
public class BinarySearchTreeTest {
	
	private BinarySearchTree<String> tree;
	
	public BinarySearchTreeTest() {
		tree = new BinarySearchTree<>();
	}
	
	private BinarySearchTreeTest append(int value) {
		tree.insert(value, "Wert-" + value);
		return this;
	}
	
	@Test
	public void testInsert() {
		append(5).append(4).append(2).append(8).append(3);
		
		Assert.assertEquals("[Wert-2, Wert-3, Wert-4, Wert-5, Wert-8]",
						tree.toString());
	}
	
	@Test
	public void testLookup1() {
		append(20).append(10).append(5).append(50).append(21321);
		
		Assert.assertEquals(null, tree.lookup(15));
		Assert.assertEquals("Wert-21321", tree.lookup(21321));
	}
	
	@Test
	public void testLookup2() {
		append(1).append(456).append(54654).append(546).append(21321);
		
		Assert.assertEquals(null, tree.lookup(54));
		Assert.assertEquals("Wert-456", tree.lookup(456));
	}
}