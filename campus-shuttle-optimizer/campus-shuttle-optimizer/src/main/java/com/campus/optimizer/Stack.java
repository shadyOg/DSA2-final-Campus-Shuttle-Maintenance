package com.campus.optimizer;

public class Stack<T> {
    private final DynamicArray<T> data;

    public Stack() {
        this.data = new DynamicArray<>();
    }

    public void push(T value) {
        data.add(value);
    }

    public T pop() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return data.remove(data.size() - 1);
    }

    public T peek() {
        if (data.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return data.get(data.size() - 1);
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
