package com.campus.optimizer.linear;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A custom generic Double-Ended Queue (Deque) implementation using a doubly linked list.
 * 
 * Satisfies the Linear Structures requirements. All insertions and removals 
 * have O(1) time complexity.
 * 
 * @param <T> the type of elements held in this deque
 */
public class Deque<T> implements Iterable<T> {

    // Helper node class for doubly linked list
    private static class Node<E> {
        E data;
        Node<E> prev;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public Deque() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Inserts the specified element at the front of this deque.
     * Time Complexity: O(1)
     */
    public void insertFront(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
        size++;
    }

    /**
     * Inserts the specified element at the rear of this deque.
     * Time Complexity: O(1)
     */
    public void insertRear(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.prev = rear;
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the element at the front of this deque.
     * Time Complexity: O(1)
     */
    public T removeFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null; // Deque is now empty
        } else {
            front.prev = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the element at the rear of this deque.
     * Time Complexity: O(1)
     */
    public T removeRear() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        T data = rear.data;
        rear = rear.prev;
        if (rear == null) {
            front = null; // Deque is now empty
        } else {
            rear.next = null;
        }
        size--;
        return data;
    }

    /**
     * Retrieves, but does not remove, the first element of this deque.
     * Time Complexity: O(1)
     */
    public T peekFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return front.data;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     * Time Complexity: O(1)
     */
    public T peekRear() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return rear.data;
    }

    /**
     * Returns true if this deque contains no elements.
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this deque.
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Removes all of the elements from this deque.
     * Time Complexity: O(n)
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Returns an iterator over the elements in this deque in proper sequence from front to rear.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = front;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
