package com.campus.optimizer.structures;

public class MinHeap<T extends Comparable<? super T>> {
    private final DynamicArray<T> values = new DynamicArray<>();

    public void offer(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Heap values cannot be null");
        }
        values.add(value);
        siftUp(values.size() - 1);
    }

    public T poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        T minimum = values.get(0);
        T last = values.remove(values.size() - 1);
        if (!values.isEmpty()) {
            values.set(0, last);
            siftDown(0);
        }
        return minimum;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return values.get(0);
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void clear() {
        values.clear();
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (values.get(parent).compareTo(values.get(index)) <= 0) {
                return;
            }
            swap(parent, index);
            index = parent;
        }
    }

    private void siftDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = left + 1;
            int smallest = index;
            if (left < values.size()
                    && values.get(left).compareTo(values.get(smallest)) < 0) {
                smallest = left;
            }
            if (right < values.size()
                    && values.get(right).compareTo(values.get(smallest)) < 0) {
                smallest = right;
            }
            if (smallest == index) {
                return;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int first, int second) {
        T value = values.get(first);
        values.set(first, values.get(second));
        values.set(second, value);
    }
}