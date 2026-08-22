package com.campus.optimizer.structures;

import org.junit.Test;
import org.junit.Assert;
import java.util.NoSuchElementException;

public class LinearStructuresTest {

    // --- LinkedList Edge Cases ---
    @Test
    public void testLinkedListEdgeCases() {
        LinkedList<String> list = new LinkedList<>();
        Assert.assertTrue(list.isEmpty());
        Assert.assertEquals(0, list.size());

        try {
            list.removeFirst();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            list.getFirst();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            list.addFirst(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        list.addFirst("Night Market");
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("Night Market", list.removeFirst());
        Assert.assertTrue(list.isEmpty());
    }

    // --- Queue Edge Cases ---
    @Test
    public void testQueueEdgeCases() {
        Queue<String> queue = new Queue<>();

        try {
            queue.dequeue();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            queue.peek();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            queue.enqueue(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        queue.enqueue("Req-001");
        Assert.assertEquals("Req-001", queue.peek());
        Assert.assertEquals("Req-001", queue.dequeue());
        Assert.assertTrue(queue.isEmpty());
    }

    // --- CircularQueue Edge Cases ---
    @Test
    public void testCircularQueueEdgeCasesAndWrapAround() {
        try {
            new CircularQueue<String>(0);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
        
        CircularQueue<String> cq = new CircularQueue<>(3);

        try {
            cq.dequeue();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            cq.enqueue(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        Assert.assertTrue(cq.enqueue("A"));
        Assert.assertTrue(cq.enqueue("B"));
        Assert.assertTrue(cq.enqueue("C"));
        Assert.assertTrue(cq.isFull());
        Assert.assertFalse(cq.enqueue("D")); // Full capacity

        Assert.assertEquals("A", cq.dequeue()); // Head wraps
        Assert.assertTrue(cq.enqueue("D"));
        Assert.assertEquals("B", cq.dequeue());
        Assert.assertEquals("C", cq.dequeue());
        Assert.assertEquals("D", cq.dequeue());
        Assert.assertTrue(cq.isEmpty());
    }

    // --- Deque Edge Cases ---
    @Test
    public void testDequeEdgeCases() {
        Deque<Integer> deque = new Deque<>();

        try {
            deque.removeFirst();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            deque.removeLast();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}

        try {
            deque.addFirst(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        deque.addFirst(10);
        Assert.assertEquals(1, deque.size());
        Assert.assertEquals(Integer.valueOf(10), deque.removeLast());
        Assert.assertTrue(deque.isEmpty());

        try {
            deque.peekFirst();
            Assert.fail("Expected NoSuchElementException");
        } catch (NoSuchElementException expected) {}
    }
}
