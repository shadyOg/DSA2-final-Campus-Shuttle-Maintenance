package com.campus.optimizer;

import java.util.Arrays;

public class DynamicArray<T> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.data = new Object[Math.max(1, initialCapacity)];
        this.size = 0;
    }

    public void add(T value) {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    public T get(int index) {
        checkIndex(index);
        return elementAt(index);
    }

    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    public T remove(int index) {
        checkIndex(index);
        T removed = elementAt(index);

        int elementsToShift = size - index - 1;
        if (elementsToShift > 0) {
            System.arraycopy(data, index + 1, data, index, elementsToShift);
        }

        data[--size] = null;
        return removed;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    private void ensureCapacity(int requiredCapacity) {
        if (requiredCapacity <= data.length) {
            return;
        }

        int newCapacity = data.length == 0 ? 1 : data.length;
        while (newCapacity < requiredCapacity) {
            newCapacity *= 2;
        }

        data = Arrays.copyOf(data, newCapacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) data[index];
    }
}
