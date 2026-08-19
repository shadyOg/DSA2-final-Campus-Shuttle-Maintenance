package com.campus.optimizer.structures;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<T extends Comparable<? super T>> {
    private Node<T> root;
    private int size;

    public boolean insert(T value) {
        requireValue(value);
        Node<T> parent = null;
        Node<T> current = root;
        while (current != null) {
            parent = current;
            int comparison = value.compareTo(current.value);
            if (comparison == 0) {
                return false;
            }
            current = comparison < 0 ? current.left : current.right;
        }

        Node<T> node = new Node<>(value, true);
        node.parent = parent;
        if (parent == null) {
            root = node;
        } else if (value.compareTo(parent.value) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        size++;
        fixAfterInsert(node);
        return true;
    }

    public boolean contains(T value) {
        return find(value) != null;
    }

    public boolean remove(T value) {
        Node<T> node = find(value);
        if (node == null) {
            return false;
        }

        Node<T> replacement;
        Node<T> parent;
        boolean removedWasRed;
        if (node.left != null && node.right != null) {
            Node<T> successor = minimum(node.right);
            node.value = successor.value;
            node = successor;
        }

        replacement = node.left != null ? node.left : node.right;
        parent = node.parent;
        removedWasRed = node.red;
        replaceNode(node, replacement);
        if (!removedWasRed) {
            if (replacement != null && replacement.red) {
                replacement.red = false;
            } else {
                fixAfterDelete(replacement, parent);
            }
        }
        size--;
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public List<T> inOrder() {
        List<T> values = new ArrayList<>();
        inOrder(root, values);
        return values;
    }

    public boolean rootIsBlack() {
        return root == null || !root.red;
    }

    public boolean hasValidRedBlackProperties() {
        if (!rootIsBlack()) {
            return false;
        }
        return blackHeight(root) >= 0;
    }

    private void fixAfterInsert(Node<T> node) {
        node.red = true;
        while (node != root && isRed(node.parent)) {
            Node<T> parent = node.parent;
            Node<T> grandparent = parent.parent;
            if (parent == grandparent.left) {
                Node<T> uncle = grandparent.right;
                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grandparent.red = true;
                    node = grandparent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                        parent = node.parent;
                        grandparent = parent.parent;
                    }
                    parent.red = false;
                    grandparent.red = true;
                    rotateRight(grandparent);
                }
            } else {
                Node<T> uncle = grandparent.left;
                if (isRed(uncle)) {
                    parent.red = false;
                    uncle.red = false;
                    grandparent.red = true;
                    node = grandparent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                        parent = node.parent;
                        grandparent = parent.parent;
                    }
                    parent.red = false;
                    grandparent.red = true;
                    rotateLeft(grandparent);
                }
            }
        }
        root.red = false;
    }

    private void fixAfterDelete(Node<T> node, Node<T> parent) {
        while (node != root && !isRed(node)) {
            if (parent == null) {
                break;
            }
            if (node == parent.left) {
                Node<T> sibling = parent.right;
                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateLeft(parent);
                    sibling = parent.right;
                }
                if (!isRed(leftOf(sibling)) && !isRed(rightOf(sibling))) {
                    if (sibling != null) {
                        sibling.red = true;
                    }
                    node = parent;
                    parent = node.parent;
                } else {
                    if (!isRed(rightOf(sibling))) {
                        if (leftOf(sibling) != null) {
                            leftOf(sibling).red = false;
                        }
                        if (sibling != null) {
                            sibling.red = true;
                            rotateRight(sibling);
                        }
                        sibling = parent.right;
                    }
                    if (sibling != null) {
                        sibling.red = parent.red;
                    }
                    parent.red = false;
                    if (rightOf(sibling) != null) {
                        rightOf(sibling).red = false;
                    }
                    rotateLeft(parent);
                    node = root;
                    parent = null;
                }
            } else {
                Node<T> sibling = parent.left;
                if (isRed(sibling)) {
                    sibling.red = false;
                    parent.red = true;
                    rotateRight(parent);
                    sibling = parent.left;
                }
                if (!isRed(rightOf(sibling)) && !isRed(leftOf(sibling))) {
                    if (sibling != null) {
                        sibling.red = true;
                    }
                    node = parent;
                    parent = node.parent;
                } else {
                    if (!isRed(leftOf(sibling))) {
                        if (rightOf(sibling) != null) {
                            rightOf(sibling).red = false;
                        }
                        if (sibling != null) {
                            sibling.red = true;
                            rotateLeft(sibling);
                        }
                        sibling = parent.left;
                    }
                    if (sibling != null) {
                        sibling.red = parent.red;
                    }
                    parent.red = false;
                    if (leftOf(sibling) != null) {
                        leftOf(sibling).red = false;
                    }
                    rotateRight(parent);
                    node = root;
                    parent = null;
                }
            }
        }
        if (node != null) {
            node.red = false;
        }
    }

    private void replaceNode(Node<T> node, Node<T> replacement) {
        if (node.parent == null) {
            root = replacement;
        } else if (node == node.parent.left) {
            node.parent.left = replacement;
        } else {
            node.parent.right = replacement;
        }
        if (replacement != null) {
            replacement.parent = node.parent;
        }
    }

    private void rotateLeft(Node<T> node) {
        Node<T> right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        right.parent = node.parent;
        if (node.parent == null) {
            root = right;
        } else if (node == node.parent.left) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }
        right.left = node;
        node.parent = right;
    }

    private void rotateRight(Node<T> node) {
        Node<T> left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        left.parent = node.parent;
        if (node.parent == null) {
            root = left;
        } else if (node == node.parent.right) {
            node.parent.right = left;
        } else {
            node.parent.left = left;
        }
        left.right = node;
        node.parent = left;
    }

    private Node<T> find(T value) {
        if (value == null) {
            return null;
        }
        Node<T> current = root;
        while (current != null) {
            int comparison = value.compareTo(current.value);
            if (comparison == 0) {
                return current;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    private Node<T> minimum(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private int blackHeight(Node<T> node) {
        if (node == null) {
            return 1;
        }
        if (node.red && (isRed(node.left) || isRed(node.right))) {
            return -1;
        }
        int leftHeight = blackHeight(node.left);
        int rightHeight = blackHeight(node.right);
        if (leftHeight < 0 || rightHeight < 0 || leftHeight != rightHeight) {
            return -1;
        }
        return leftHeight + (node.red ? 0 : 1);
    }

    private void inOrder(Node<T> node, List<T> values) {
        if (node == null) {
            return;
        }
        inOrder(node.left, values);
        values.add(node.value);
        inOrder(node.right, values);
    }

    private boolean isRed(Node<T> node) {
        return node != null && node.red;
    }

    private Node<T> leftOf(Node<T> node) {
        return node == null ? null : node.left;
    }

    private Node<T> rightOf(Node<T> node) {
        return node == null ? null : node.right;
    }

    private void requireValue(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Tree values cannot be null");
        }
    }

    private static final class Node<T> {
        private T value;
        private Node<T> parent;
        private Node<T> left;
        private Node<T> right;
        private boolean red;

        private Node(T value, boolean red) {
            this.value = value;
            this.red = red;
        }
    }
}