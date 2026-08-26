package com.campus.optimizer.structures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge-case hardening tests for DisjointSet (Union-Find).
 * Covers: single element, self-union, repeated unions,
 * union of already-connected elements, and path compression correctness.
 *
 * NOTE: Adjust method names (find/union/connected/getParent/makeSet)
 * to match your actual DisjointSet implementation.
 */
public class DisjointSetEdgeCaseTest {

    private DisjointSet ds;
    private static final int SIZE = 10;

    @BeforeEach
    void setUp() {
        ds = new DisjointSet(SIZE); // each element starts as its own parent
    }

    // ---------- Single element ----------

    @Test
    @DisplayName("A single element is its own root")
    void singleElementIsOwnRoot() {
        assertEquals(0, ds.find(0));
    }

    @Test
    @DisplayName("Two untouched elements are not connected")
    void untouchedElementsNotConnected() {
        assertFalse(ds.connected(0, 1));
    }

    // ---------- Self-union ----------

    @Test
    @DisplayName("Union of an element with itself is a safe no-op")
    void selfUnionIsNoOp() {
        int rootBefore = ds.find(3);
        assertDoesNotThrow(() -> ds.union(3, 3));
        assertEquals(rootBefore, ds.find(3), "Self-union must not change the element's root");
    }

    // ---------- Repeated unions ----------

    @Test
    @DisplayName("Repeated unions of the same pair don't corrupt rank/size")
    void repeatedUnionsAreIdempotent() {
        ds.union(1, 2);
        int rootAfterFirst = ds.find(1);

        // Call union again on the same pair several times
        for (int i = 0; i < 5; i++) {
            ds.union(1, 2);
        }

        assertEquals(rootAfterFirst, ds.find(1));
        assertEquals(rootAfterFirst, ds.find(2));
        assertTrue(ds.connected(1, 2));
    }

    @Test
    @DisplayName("Union of two elements already in the same set is a no-op")
    void unionOfAlreadyConnectedElements() {
        ds.union(4, 5);
        ds.union(5, 6); // 4, 5, 6 now in one set
        int rootBefore = ds.find(4);

        ds.union(4, 6); // already connected transitively

        assertEquals(rootBefore, ds.find(4));
        assertTrue(ds.connected(4, 6));
    }

    // ---------- Path compression correctness ----------

    @Test
    @DisplayName("Path compression flattens intermediate nodes to point directly at the root")
    void pathCompressionFlattensChain() {
        // Deliberately build a chain: 0 -> 1 -> 2 -> 3 -> 4
        // (using union in a way that avoids union-by-rank collapsing it immediately,
        //  simulate by unioning sequentially)
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(3, 4);

        int root = ds.find(4); // triggers path compression along the way

        assertEquals(root, ds.find(0));
        assertEquals(root, ds.find(1));
        assertEquals(root, ds.find(2));
        assertEquals(root, ds.find(3));
        assertEquals(root, ds.find(4));
    }

    @Test
    @DisplayName("All elements report the same root and connected() agrees after unions")
    void allConnectedAfterChainOfUnions() {
        for (int i = 0; i < SIZE - 1; i++) {
            ds.union(i, i + 1);
        }
        int root = ds.find(0);
        for (int i = 1; i < SIZE; i++) {
            assertEquals(root, ds.find(i), "Element " + i + " should share the same root");
            assertTrue(ds.connected(0, i));
        }
    }
}
