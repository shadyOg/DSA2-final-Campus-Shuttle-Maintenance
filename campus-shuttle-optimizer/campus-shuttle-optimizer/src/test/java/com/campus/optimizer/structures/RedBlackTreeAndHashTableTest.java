package com.campus.optimizer.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class RedBlackTreeAndHashTableTest {

    @Test
    public void redBlackTreeMaintainsOrderingAndBalanceAfterInsertions() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int value : new int[] {10, 20, 30, 15, 5, 1, 7, 25, 40, 50}) {
            assertTrue(tree.insert(value));
            assertTrue(tree.rootIsBlack());
            assertTrue(tree.hasValidRedBlackProperties());
        }

        assertEquals(Arrays.asList(1, 5, 7, 10, 15, 20, 25, 30, 40, 50), tree.inOrder());
        assertTrue(tree.contains(25));
        assertFalse(tree.insert(25));
    }

    @Test
    public void redBlackTreeMaintainsBalanceAfterDeletions() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int value = 1; value <= 30; value++) {
            tree.insert(value);
        }

        for (int value : new int[] {1, 15, 30, 8, 22, 4}) {
            assertTrue(tree.remove(value));
            assertTrue(tree.rootIsBlack());
            assertTrue(tree.hasValidRedBlackProperties());
        }
        assertFalse(tree.remove(999));
        assertEquals(24, tree.size());
    }

    @Test
    public void hashTableHandlesCollisionsUpdatesAndResize() {
        HashTable<CollisionKey, String> table = new HashTable<>(3);
        CollisionKey first = new CollisionKey("first");
        CollisionKey second = new CollisionKey("second");

        assertNull(table.put(first, "A"));
        assertNull(table.put(second, "B"));
        assertEquals("A", table.get(first));
        assertEquals("B", table.get(second));
        assertEquals("A", table.put(first, "updated"));
        assertEquals("updated", table.get(first));

        for (int index = 0; index < 10; index++) {
            table.put(new CollisionKey("extra-" + index), "value-" + index);
        }
        assertTrue(table.capacity() > 3);
        assertEquals(12, table.size());
        assertEquals("B", table.remove(second));
        assertFalse(table.containsKey(second));
        assertEquals(11, table.size());
    }

    private static final class CollisionKey {
        private final String value;

        private CollisionKey(String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 7;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof CollisionKey key && value.equals(key.value);
        }
    }
}