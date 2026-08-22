package com.campus.optimizer.structures;

import java.util.NoSuchElementException;

public class Deque<T> {
    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void addFirst(T item) {
        if (item == null) throw new IllegalArgumentException("Null elements not permitted");
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T item) {
        if (item == null) throw new IllegalArgumentException("Null elements not permitted");
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow: empty front");
        T data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow: empty rear");
        T data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    public T peekFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return head.data;
    }

    public T peekLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
        return tail.data;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}
