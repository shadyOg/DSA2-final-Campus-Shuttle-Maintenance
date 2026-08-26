package com.campus.optimizer.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BTreeAndDisjointSetTest {

    @Test
    public void bTreeHandlesInvalidDegreeEmptyStateAndClearReuse() {
        try {
            new BTree<Integer>(1);
            org.junit.Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }

        BTree<Integer> tree = new BTree<>();
        assertTrue(tree.isEmpty());
        assertEquals(Collections.emptyList(), tree.traverse());
        assertFalse(tree.contains(null));
        assertFalse(tree.remove(null));
        assertTrue(tree.insert(5));
        assertTrue(tree.remove(5));
        tree.clear();
        assertTrue(tree.isEmpty());
        assertTrue(tree.insert(6));
        assertEquals(Collections.singletonList(6), tree.traverse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bTreeRejectsNullInsertions() {
        new BTree<Integer>().insert(null);
    }

    @Test
    public void bTreeSplitsNodesAndReturnsSortedTraversal() {
        BTree<Integer> tree = new BTree<>(2);
        for (int value = 1; value <= 40; value++) {
            assertTrue(tree.insert(value));
        }
        assertFalse(tree.insert(20));
        assertEquals(40, tree.size());
        assertEquals(expectedRange(1, 40), tree.traverse());
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(40));
        assertFalse(tree.contains(99));
    }

    @Test
    public void bTreeDeletionPreservesContentsThroughBorrowAndMergeCases() {
        BTree<Integer> tree = new BTree<>(3);
        for (int value = 1; value <= 50; value++) {
            tree.insert(value);
        }
        for (int value = 1; value <= 50; value += 2) {
            assertTrue(tree.remove(value));
        }

        assertEquals(expectedEvenRange(2, 50), tree.traverse());
        assertEquals(25, tree.size());
        assertFalse(tree.remove(1));
        assertTrue(tree.remove(26));
        assertFalse(tree.contains(26));
    }

    @Test
    public void disjointSetUsesUnionFindOperations() {
        DisjointSet set = new DisjointSet(6);
        assertEquals(6, set.componentCount());
        assertTrue(set.union(0, 1));
        assertTrue(set.union(1, 2));
        assertFalse(set.union(0, 2));
        assertTrue(set.connected(0, 2));
        assertFalse(set.connected(0, 3));
        assertEquals(4, set.componentCount());
        assertEquals(set.find(0), set.find(2));
    }

    @Test
    public void disjointSetHandlesSingletonRepeatedUnionAndBounds() {
        DisjointSet set = new DisjointSet(1);
        assertEquals(1, set.size());
        assertEquals(1, set.componentCount());
        assertTrue(set.connected(0, 0));
        assertFalse(set.union(0, 0));
        assertEquals(1, set.componentCount());

        try {
            new DisjointSet(-1);
            org.junit.Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
        try {
            set.find(1);
            org.junit.Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // expected
        }
    }

    private List<Integer> expectedRange(int start, int end) {
        List<Integer> values = new ArrayList<>();
        for (int value = start; value <= end; value++) {
            values.add(value);
        }
        return values;
    }

    private List<Integer> expectedEvenRange(int start, int end) {
        List<Integer> values = new ArrayList<>();
        for (int value = start; value <= end; value += 2) {
            values.add(value);
        }
        return values;
    }
}