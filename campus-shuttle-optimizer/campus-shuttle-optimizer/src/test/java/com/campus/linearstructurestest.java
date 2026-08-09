package com.campus.optimizer.structures;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class LinearStructuresTest {

    @Test
    void testLinkedListOperations() {
        LinkedList<String> list = new LinkedList<>();
        list.addLast("Main Gate");
        list.addFirst("Night Market");

        assertEquals("Night Market", list.getFirst());
        assertEquals("Main Gate", list.getLast());
        assertEquals(2, list.size());

        assertEquals("Night Market", list.removeFirst());
        assertEquals(1, list.size());
    }

    @Test
    void testQueueOperations() {
        Queue<String> queue = new Queue<>();
        queue.enqueue("Req-101");
        queue.enqueue("Req-102");

        assertEquals("Req-101", queue.peek());
        assertEquals("Req-101", queue.dequeue());
        assertEquals("Req-102", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertThrows(NoSuchElementException.class, queue::dequeue);
    }

    @Test
    void testCircularQueueBoundary() {
        CircularQueue<String> cq = new CircularQueue<>(2);
        assertTrue(cq.enqueue("Shuttle A"));
        assertTrue(cq.enqueue("Shuttle B"));
        assertFalse(cq.enqueue("Shuttle C")); // Should fail when full

        assertEquals("Shuttle A", cq.dequeue());
        assertTrue(cq.enqueue("Shuttle C")); // Wrap-around check
        assertEquals("Shuttle B", cq.dequeue());
    }

    @Test
    void testDequeDoubleEndedOperations() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(10);
        deque.addLast(20);

        assertEquals(10, deque.removeFirst());
        assertEquals(20, deque.removeLast());
        assertTrue(deque.isEmpty());
    }
}