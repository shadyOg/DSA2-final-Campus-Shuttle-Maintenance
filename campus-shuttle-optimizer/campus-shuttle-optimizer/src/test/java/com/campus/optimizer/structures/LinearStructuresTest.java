package com.campus.optimizer.structures;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class LinearStructuresTest {

    
    @Test
    void testLinkedListEdgeCases() {
        LinkedList<String> list = new LinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        assertThrows(NoSuchElementException.class, list::removeFirst);
        assertThrows(NoSuchElementException.class, list::getFirst);
        assertThrows(IllegalArgumentException.class, () -> list.addFirst(null));

        list.addFirst("Night Market");
        assertEquals(1, list.size());
        assertEquals("Night Market", list.removeFirst());
        assertTrue(list.isEmpty());
    }

    
    @Test
    void testQueueEdgeCases() {
        Queue<String> queue = new Queue<>();
        assertThrows(NoSuchElementException.class, queue::dequeue);
        assertThrows(NoSuchElementException.class, queue::peek);
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(null));

        queue.enqueue("Req-001");
        assertEquals("Req-001", queue.peek());
        assertEquals("Req-001", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    
    @Test
    void testCircularQueueEdgeCasesAndWrapAround() {
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<String>(0));
        
        CircularQueue<String> cq = new CircularQueue<>(3);
        assertThrows(NoSuchElementException.class, cq::dequeue);
        assertThrows(IllegalArgumentException.class, () -> cq.enqueue(null));

        assertTrue(cq.enqueue("A"));
        assertTrue(cq.enqueue("B"));
        assertTrue(cq.enqueue("C"));
        assertTrue(cq.isFull());
        assertFalse(cq.enqueue("D")); // Full capacity

        assertEquals("A", cq.dequeue()); // Head wraps
        assertTrue(cq.enqueue("D"));
        assertEquals("B", cq.dequeue());
        assertEquals("C", cq.dequeue());
        assertEquals("D", cq.dequeue());
        assertTrue(cq.isEmpty());
    }

    
    @Test
    void testDequeEdgeCases() {
        Deque<Integer> deque = new Deque<>();
        assertThrows(NoSuchElementException.class, deque::removeFirst);
        assertThrows(NoSuchElementException.class, deque::removeLast);
        assertThrows(IllegalArgumentException.class, () -> deque.addFirst(null));

        deque.addFirst(10);
        assertEquals(1, deque.size());
        assertEquals(10, deque.removeLast());
        assertTrue(deque.isEmpty());
        assertThrows(NoSuchElementException.class, deque::peekFirst);
    }
}
