import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge-case hardening tests for BTree.
 * Covers: empty tree, single element, duplicate keys, node splitting,
 * root growth, deletion causing underflow, and deleting down to empty.
 *
 * NOTE: Adjust method names (insert/search/delete/isEmpty/size/height)
 * and generic type to match your actual BTree<T> implementation.
 */
public class BTreeEdgeCaseTest {

    private BTree<Integer> tree;
    private static final int MIN_DEGREE = 3; // match your implementation's t value

    @BeforeEach
    void setUp() {
        tree = new BTree<>(MIN_DEGREE);
    }

    // ---------- Empty tree ----------

    @Test
    @DisplayName("Search on empty tree returns not-found, doesn't throw")
    void searchOnEmptyTree() {
        assertFalse(tree.search(42), "Searching an empty tree should return false");
    }

    @Test
    @DisplayName("Delete on empty tree is a safe no-op")
    void deleteOnEmptyTree() {
        assertDoesNotThrow(() -> tree.delete(42));
        assertTrue(tree.isEmpty());
    }

    // ---------- Single element ----------

    @Test
    @DisplayName("Insert one key, then search finds it")
    void singleElementInsertAndSearch() {
        tree.insert(10);
        assertTrue(tree.search(10));
        assertFalse(tree.search(99));
    }

    @Test
    @DisplayName("Insert one key, delete it, tree becomes empty again")
    void singleElementInsertThenDelete() {
        tree.insert(10);
        tree.delete(10);
        assertTrue(tree.isEmpty(), "Tree should be empty after deleting its only key");
        assertFalse(tree.search(10));
    }

    // ---------- Duplicate keys ----------

    @Test
    @DisplayName("Inserting a duplicate key does not corrupt the tree")
    void duplicateKeyInsertion() {
        tree.insert(5);
        // Decide/confirm your implementation's contract here:
        // either it rejects duplicates, or overwrites, but it must not throw
        // or create two entries that break search.
        assertDoesNotThrow(() -> tree.insert(5));
        assertTrue(tree.search(5));
    }

    // ---------- Leaf split ----------

    @Test
    @DisplayName("Inserting enough keys forces a leaf node to split")
    void leafNodeSplitsOnOverflow() {
        // With min degree t, a leaf node holds up to 2t-1 keys before splitting.
        int keysToOverflow = (2 * MIN_DEGREE - 1) + 1;
        for (int i = 1; i <= keysToOverflow; i++) {
            tree.insert(i);
        }
        for (int i = 1; i <= keysToOverflow; i++) {
            assertTrue(tree.search(i), "Key " + i + " should still be findable after split");
        }
    }

    // ---------- Root growth ----------

    @Test
    @DisplayName("Root splits and tree height increases when root overflows")
    void rootSplitsAndHeightIncreases() {
        int initialHeight = tree.height();
        int enoughToSplitRoot = (2 * MIN_DEGREE - 1) * 3; // rough over-provision
        for (int i = 1; i <= enoughToSplitRoot; i++) {
            tree.insert(i);
        }
        assertTrue(tree.height() > initialHeight, "Height should grow after root split(s)");
    }

    // ---------- Deletion causing underflow / merge ----------

    @Test
    @DisplayName("Deleting keys down to a minimal tree triggers merge/borrow without corruption")
    void deletionCausesUnderflowAndRebalances() {
        int[] keys = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int k : keys) tree.insert(k);

        // Delete enough to force underflow in a child node
        tree.delete(6);
        tree.delete(7);

        for (int k : new int[]{10, 20, 5, 12, 30, 17}) {
            assertTrue(tree.search(k), "Remaining key " + k + " must still be found after rebalancing");
        }
        assertFalse(tree.search(6));
        assertFalse(tree.search(7));
    }

    @Test
    @DisplayName("Deleting the only key in a leaf root leaves an empty, valid tree")
    void deleteOnlyKeyInRootLeaf() {
        tree.insert(1);
        tree.delete(1);
        assertTrue(tree.isEmpty());
        assertDoesNotThrow(() -> tree.insert(2)); // tree must still be usable afterward
        assertTrue(tree.search(2));
    }
}
