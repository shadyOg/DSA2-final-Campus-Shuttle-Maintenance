package com.campus.optimizer.structures;

import java.util.NoSuchElementException;

public class CircularQueue<T> {
    private final T[] data;
    private int head = 0;
    private int tail = 0;
    private int size = 0;
    private final int capacity;

    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than 0");
        this.capacity = capacity;
        this.data = (T[]) new Object[capacity];
    }

    public boolean enqueue(T item) {
        if (item == null) throw new IllegalArgumentException("Null values cannot be enqueued");
        if (isFull()) return false;
        data[tail] = item;
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("CircularQueue underflow: queue is empty");
        T item = data[head];
        data[head] = null;
        head = (head + 1) % capacity;
        size--;
        return item;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("CircularQueue is empty");
        return data[head];
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) data[i] = null;
        head = 0;
        tail = 0;
        size = 0;
    }

    public boolean isFull() { return size == capacity; }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public int capacity() { return capacity; }
}
