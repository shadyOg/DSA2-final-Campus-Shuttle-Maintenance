package com.campus.optimizer.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MinHeapTest {

    @Test
    public void heapReturnsValuesInAscendingOrder() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.offer(7);
        heap.offer(2);
        heap.offer(9);
        heap.offer(1);
        heap.offer(4);

        assertEquals(Integer.valueOf(1), heap.peek());
        assertEquals(Integer.valueOf(1), heap.poll());
        assertEquals(Integer.valueOf(2), heap.poll());
        assertEquals(Integer.valueOf(4), heap.poll());
        assertEquals(Integer.valueOf(7), heap.poll());
        assertEquals(Integer.valueOf(9), heap.poll());
        assertTrue(heap.isEmpty());
    }

    @Test
    public void heapSupportsClearAndSize() {
        MinHeap<String> heap = new MinHeap<>();
        heap.offer("Science Block");
        heap.offer("Balme Library");
        assertEquals(2, heap.size());
        heap.clear();
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
    }
}