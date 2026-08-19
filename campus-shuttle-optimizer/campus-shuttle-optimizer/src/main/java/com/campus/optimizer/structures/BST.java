package com.campus.optimizer.structures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BST<T extends Comparable<? super T>> {
    private Node<T> root;
    private int size;

    public boolean insert(T value) {
        requireValue(value);
        if (root == null) {
            root = new Node<>(value);
            size++;
            return true;
        }

        Node<T> current = root;
        while (true) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) {
                return false;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node<>(value);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public boolean contains(T value) {
        if (value == null) {
            return false;
        }
        Node<T> current = root;
        while (current != null) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) {
                return true;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    public boolean remove(T value) {
        if (value == null || !contains(value)) {
            return false;
        }
        root = remove(root, value);
        size--;
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public List<T> inOrder() {
        List<T> values = new ArrayList<>();
        inOrder(root, values);
        return values;
    }

    public List<T> preOrder() {
        List<T> values = new ArrayList<>();
        preOrder(root, values);
        return values;
    }

    public List<T> postOrder() {
        List<T> values = new ArrayList<>();
        postOrder(root, values);
        return values;
    }

    public List<T> levelOrder() {
        List<T> values = new ArrayList<>();
        if (root == null) {
            return values;
        }

        Deque<Node<T>> queue = new ArrayDeque<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            Node<T> current = queue.removeFirst();
            values.add(current.value);
            if (current.left != null) {
                queue.addLast(current.left);
            }
            if (current.right != null) {
                queue.addLast(current.right);
            }
        }
        return values;
    }

    private Node<T> remove(Node<T> node, T value) {
        int comparison = value.compareTo(node.value);
        if (comparison < 0) {
            node.left = remove(node.left, value);
        } else if (comparison > 0) {
            node.right = remove(node.right, value);
        } else if (node.left == null) {
            return node.right;
        } else if (node.right == null) {
            return node.left;
        } else {
            Node<T> successor = minimum(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private Node<T> minimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void inOrder(Node<T> node, List<T> values) {
        if (node == null) {
            return;
        }
        inOrder(node.left, values);
        values.add(node.value);
        inOrder(node.right, values);
    }

    private void preOrder(Node<T> node, List<T> values) {
        if (node == null) {
            return;
        }
        values.add(node.value);
        preOrder(node.left, values);
        preOrder(node.right, values);
    }

    private void postOrder(Node<T> node, List<T> values) {
        if (node == null) {
            return;
        }
        postOrder(node.left, values);
        postOrder(node.right, values);
        values.add(node.value);
    }

    private void requireValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("BST values cannot be null");
        }
    }

    private static final class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;

        private Node(T value) {
            this.value = value;
        }
    }
}