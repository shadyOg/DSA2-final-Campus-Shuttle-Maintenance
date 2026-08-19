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
        this.capacity = capacity;
        this.data = (T[]) new Object[capacity];
    }

    public boolean enqueue(T item) {
        if (isFull()) {
            return false;
        }
        data[tail] = item;
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T item = data[head];
        data[head] = null; // Clean reference
        head = (head + 1) % capacity;
        size--;
        return item;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return data[head];
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}