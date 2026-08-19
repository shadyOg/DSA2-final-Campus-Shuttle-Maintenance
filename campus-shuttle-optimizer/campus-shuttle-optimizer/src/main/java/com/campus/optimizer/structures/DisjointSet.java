package com.campus.optimizer.structures;

public class DisjointSet {
    private final int[] parent;
    private final int[] rank;
    private int componentCount;

    public DisjointSet(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        parent = new int[size];
        rank = new int[size];
        componentCount = size;
        for (int index = 0; index < size; index++) {
            parent[index] = index;
        }
    }

    public int find(int element) {
        checkElement(element);
        if (parent[element] != element) {
            parent[element] = find(parent[element]);
        }
        return parent[element];
    }

    public boolean union(int first, int second) {
        int firstRoot = find(first);
        int secondRoot = find(second);
        if (firstRoot == secondRoot) {
            return false;
        }

        if (rank[firstRoot] < rank[secondRoot]) {
            parent[firstRoot] = secondRoot;
        } else if (rank[firstRoot] > rank[secondRoot]) {
            parent[secondRoot] = firstRoot;
        } else {
            parent[secondRoot] = firstRoot;
            rank[firstRoot]++;
        }
        componentCount--;
        return true;
    }

    public boolean connected(int first, int second) {
        return find(first) == find(second);
    }

    public int componentCount() {
        return componentCount;
    }

    public int size() {
        return parent.length;
    }

    private void checkElement(int element) {
        if (element < 0 || element >= parent.length) {
            throw new IndexOutOfBoundsException("Element: " + element);
        }
    }
}