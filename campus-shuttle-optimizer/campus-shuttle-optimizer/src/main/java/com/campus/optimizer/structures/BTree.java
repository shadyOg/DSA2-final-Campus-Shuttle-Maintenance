package com.campus.optimizer.structures;

import java.util.ArrayList;
import java.util.List;

public class BTree<T extends Comparable<? super T>> {
    private final int minimumDegree;
    private Node<T> root;
    private int size;

    public BTree() {
        this(2);
    }

    public BTree(int minimumDegree) {
        if (minimumDegree < 2) {
            throw new IllegalArgumentException("Minimum degree must be at least 2");
        }
        this.minimumDegree = minimumDegree;
        this.root = new Node<>(true);
    }

    public boolean insert(T value) {
        requireValue(value);
        if (contains(value)) {
            return false;
        }
        if (root.keys.size() == maxKeys()) {
            Node<T> newRoot = new Node<>(false);
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        insertNonFull(root, value);
        size++;
        return true;
    }

    public boolean contains(T value) {
        return search(root, value) != null;
    }

    public boolean remove(T value) {
        if (value == null || !contains(value)) {
            return false;
        }
        remove(root, value);
        if (!root.leaf && root.keys.isEmpty()) {
            root = root.children.get(0);
        }
        size--;
        return true;
    }

    public List<T> traverse() {
        List<T> values = new ArrayList<>();
        traverse(root, values);
        return values;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int minimumDegree() {
        return minimumDegree;
    }

    public void clear() {
        root = new Node<>(true);
        size = 0;
    }

    private void insertNonFull(Node<T> node, T value) {
        int index = node.keys.size() - 1;
        if (node.leaf) {
            node.keys.add(null);
            while (index >= 0 && value.compareTo(node.keys.get(index)) < 0) {
                node.keys.set(index + 1, node.keys.get(index));
                index--;
            }
            node.keys.set(index + 1, value);
            return;
        }

        while (index >= 0 && value.compareTo(node.keys.get(index)) < 0) {
            index--;
        }
        index++;
        if (node.children.get(index).keys.size() == maxKeys()) {
            splitChild(node, index);
            if (value.compareTo(node.keys.get(index)) > 0) {
                index++;
            }
        }
        insertNonFull(node.children.get(index), value);
    }

    private void splitChild(Node<T> parent, int childIndex) {
        Node<T> fullChild = parent.children.get(childIndex);
        Node<T> rightChild = new Node<>(fullChild.leaf);
        T middle = fullChild.keys.get(minimumDegree - 1);

        for (int index = minimumDegree; index < fullChild.keys.size(); index++) {
            rightChild.keys.add(fullChild.keys.get(index));
        }
        if (!fullChild.leaf) {
            for (int index = minimumDegree; index < fullChild.children.size(); index++) {
                rightChild.children.add(fullChild.children.get(index));
            }
        }
        while (fullChild.keys.size() >= minimumDegree) {
            fullChild.keys.remove(fullChild.keys.size() - 1);
        }
        if (!fullChild.leaf) {
            while (fullChild.children.size() > minimumDegree) {
                fullChild.children.remove(fullChild.children.size() - 1);
            }
        }

        parent.children.add(childIndex + 1, rightChild);
        parent.keys.add(childIndex, middle);
    }

    private Node<T> search(Node<T> node, T value) {
        if (value == null) {
            return null;
        }
        int index = 0;
        while (index < node.keys.size() && value.compareTo(node.keys.get(index)) > 0) {
            index++;
        }
        if (index < node.keys.size() && value.compareTo(node.keys.get(index)) == 0) {
            return node;
        }
        return node.leaf ? null : search(node.children.get(index), value);
    }

    private void remove(Node<T> node, T value) {
        int index = lowerBound(node, value);
        if (index < node.keys.size() && value.compareTo(node.keys.get(index)) == 0) {
            if (node.leaf) {
                node.keys.remove(index);
            } else {
                removeFromInternal(node, index);
            }
            return;
        }
        if (node.leaf) {
            return;
        }

        boolean wasLastChild = index == node.keys.size();
        if (node.children.get(index).keys.size() < minimumDegree) {
            fillChild(node, index);
        }
        if (wasLastChild && index > node.keys.size()) {
            remove(node.children.get(index - 1), value);
        } else {
            remove(node.children.get(index), value);
        }
    }

    private void removeFromInternal(Node<T> node, int index) {
        T value = node.keys.get(index);
        Node<T> leftChild = node.children.get(index);
        Node<T> rightChild = node.children.get(index + 1);
        if (leftChild.keys.size() >= minimumDegree) {
            T predecessor = predecessor(leftChild);
            node.keys.set(index, predecessor);
            remove(leftChild, predecessor);
        } else if (rightChild.keys.size() >= minimumDegree) {
            T successor = successor(rightChild);
            node.keys.set(index, successor);
            remove(rightChild, successor);
        } else {
            mergeChildren(node, index);
            remove(leftChild, value);
        }
    }

    private void fillChild(Node<T> parent, int childIndex) {
        if (childIndex > 0
                && parent.children.get(childIndex - 1).keys.size() >= minimumDegree) {
            borrowFromPrevious(parent, childIndex);
        } else if (childIndex < parent.children.size() - 1
                && parent.children.get(childIndex + 1).keys.size() >= minimumDegree) {
            borrowFromNext(parent, childIndex);
        } else if (childIndex < parent.children.size() - 1) {
            mergeChildren(parent, childIndex);
        } else {
            mergeChildren(parent, childIndex - 1);
        }
    }

    private void borrowFromPrevious(Node<T> parent, int childIndex) {
        Node<T> child = parent.children.get(childIndex);
        Node<T> previous = parent.children.get(childIndex - 1);
        child.keys.add(0, parent.keys.get(childIndex - 1));
        parent.keys.set(childIndex - 1, previous.keys.remove(previous.keys.size() - 1));
        if (!previous.leaf) {
            child.children.add(0, previous.children.remove(previous.children.size() - 1));
        }
    }

    private void borrowFromNext(Node<T> parent, int childIndex) {
        Node<T> child = parent.children.get(childIndex);
        Node<T> next = parent.children.get(childIndex + 1);
        child.keys.add(parent.keys.get(childIndex));
        parent.keys.set(childIndex, next.keys.remove(0));
        if (!next.leaf) {
            child.children.add(next.children.remove(0));
        }
    }

    private void mergeChildren(Node<T> parent, int leftIndex) {
        Node<T> left = parent.children.get(leftIndex);
        Node<T> right = parent.children.remove(leftIndex + 1);
        left.keys.add(parent.keys.remove(leftIndex));
        left.keys.addAll(right.keys);
        if (!left.leaf) {
            left.children.addAll(right.children);
        }
    }

    private T predecessor(Node<T> node) {
        while (!node.leaf) {
            node = node.children.get(node.children.size() - 1);
        }
        return node.keys.get(node.keys.size() - 1);
    }

    private T successor(Node<T> node) {
        while (!node.leaf) {
            node = node.children.get(0);
        }
        return node.keys.get(0);
    }

    private int lowerBound(Node<T> node, T value) {
        int index = 0;
        while (index < node.keys.size() && value.compareTo(node.keys.get(index)) > 0) {
            index++;
        }
        return index;
    }

    private void traverse(Node<T> node, List<T> values) {
        for (int index = 0; index < node.keys.size(); index++) {
            if (!node.leaf) {
                traverse(node.children.get(index), values);
            }
            values.add(node.keys.get(index));
        }
        if (!node.leaf) {
            traverse(node.children.get(node.keys.size()), values);
        }
    }

    private int maxKeys() {
        return 2 * minimumDegree - 1;
    }

    private void requireValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("B-tree values cannot be null");
        }
    }

    private static final class Node<T> {
        private final boolean leaf;
        private final List<T> keys = new ArrayList<>();
        private final List<Node<T>> children = new ArrayList<>();

        private Node(boolean leaf) {
            this.leaf = leaf;
        }
    }
}