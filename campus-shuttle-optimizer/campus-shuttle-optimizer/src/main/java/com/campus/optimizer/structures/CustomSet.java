package com.campus.optimizer.structures;

public class CustomSet<T> {
    private static final Boolean PRESENT = Boolean.TRUE;
    private final HashTable<T, Boolean> values = new HashTable<>();

    public boolean add(T value) {
        if (values.containsKey(value)) {
            return false;
        }
        values.put(value, PRESENT);
        return true;
    }

    public boolean contains(T value) {
        return values.containsKey(value);
    }

    public boolean remove(T value) {
        if (!values.containsKey(value)) {
            return false;
        }
        values.remove(value);
        return true;
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

    public DynamicArray<T> values() {
        return values.keys();
    }
}