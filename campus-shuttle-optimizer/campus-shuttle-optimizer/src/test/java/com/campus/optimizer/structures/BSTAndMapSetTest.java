package com.campus.optimizer.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class BSTAndMapSetTest {

    @Test
    public void bstProvidesAllFourTraversals() {
        BST<Integer> tree = new BST<>();
        for (int value : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            assertTrue(tree.insert(value));
        }

        assertEquals(Arrays.asList(50, 30, 20, 40, 70, 60, 80), tree.preOrder());
        assertEquals(Arrays.asList(20, 30, 40, 50, 60, 70, 80), tree.inOrder());
        assertEquals(Arrays.asList(20, 40, 30, 60, 80, 70, 50), tree.postOrder());
        assertEquals(Arrays.asList(50, 30, 70, 20, 40, 60, 80), tree.levelOrder());
    }

    @Test
    public void bstRejectsDuplicatesAndRemovesEachNodeShape() {
        BST<Integer> tree = new BST<>();
        for (int value : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            tree.insert(value);
        }

        assertFalse(tree.insert(30));
        assertTrue(tree.remove(20));
        assertTrue(tree.remove(70));
        assertFalse(tree.remove(999));
        assertEquals(Arrays.asList(30, 40, 50, 60, 80), tree.inOrder());
        assertEquals(5, tree.size());
    }

    @Test
    public void customMapAndSetProvideHashMapStyleOperations() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertEquals(null, map.put("requests", 3));
        assertEquals(Integer.valueOf(3), map.put("requests", 4));
        assertTrue(map.containsKey("requests"));
        assertEquals(Integer.valueOf(4), map.get("requests"));
        assertEquals(1, map.keySet().size());
        assertEquals("requests", map.keySet().get(0));
        assertEquals(Integer.valueOf(4), map.remove("requests"));
        assertTrue(map.isEmpty());

        CustomSet<String> set = new CustomSet<>();
        assertTrue(set.add("Bani Hostel"));
        assertFalse(set.add("Bani Hostel"));
        assertTrue(set.contains("Bani Hostel"));
        assertEquals(1, set.values().size());
        assertEquals("Bani Hostel", set.values().get(0));
        assertTrue(set.remove("Bani Hostel"));
        assertTrue(set.isEmpty());
    }
}