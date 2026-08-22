package com.campus.optimizer.structures;

import java.util.NoSuchElementException;

public class Queue<T> {
    private final LinkedList<T> list = new LinkedList<>();

    public void enqueue(T item) {
        if (item == null) throw new IllegalArgumentException("Queue does not allow null items");
        list.addLast(item);
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow: queue is empty");
        return list.removeFirst();
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return list.getFirst();
    }

    public void clear() { list.clear(); }
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
}
