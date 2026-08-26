package com.campus.optimizer.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import java.util.Collections;

import org.junit.Test;

public class BSTAndMapSetTest {

    @Test
    public void bstHandlesEmptySingleNodeAndClearStates() {
        BST<Integer> tree = new BST<>();

        assertTrue(tree.isEmpty());
        assertEquals(Collections.emptyList(), tree.inOrder());
        assertEquals(Collections.emptyList(), tree.preOrder());
        assertEquals(Collections.emptyList(), tree.postOrder());
        assertEquals(Collections.emptyList(), tree.levelOrder());
        assertFalse(tree.contains(null));
        assertFalse(tree.remove(null));
        assertFalse(tree.remove(1));

        assertTrue(tree.insert(10));
        assertFalse(tree.insert(10));
        assertEquals(1, tree.size());
        assertTrue(tree.remove(10));
        assertTrue(tree.isEmpty());
        tree.clear();
        assertEquals(Collections.emptyList(), tree.inOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bstRejectsNullInsertions() {
        new BST<Integer>().insert(null);
    }

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

    @Test
    public void customMapDistinguishesMissingKeysFromPresentNullValues() {
        CustomMap<String, Integer> map = new CustomMap<>();

        assertEquals(Integer.valueOf(7), map.getOrDefault("missing", 7));
        assertNull(map.put("present", null));
        assertTrue(map.containsKey("present"));
        assertNull(map.getOrDefault("present", 7));
        assertEquals(1, map.size());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void customSetHandlesMissingRemovalAndClearReuse() {
        CustomSet<Integer> set = new CustomSet<>();

        assertFalse(set.contains(1));
        assertFalse(set.remove(1));
        assertTrue(set.add(1));
        assertFalse(set.add(1));
        set.clear();
        assertTrue(set.isEmpty());
        assertTrue(set.add(2));
        assertTrue(set.contains(2));
    }
}