package com.campus.optimizer.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    private final Map<String, Integer> nodeToIdx = new LinkedHashMap<>();
    private final Map<Integer, String> idxToNode = new LinkedHashMap<>();
    private int nodeCount = 0;

    private final Map<String, Map<String, Double>> adjList = new LinkedHashMap<>();

    private int maxVertices;
    private double[][] adjMatrix;

    public Graph() {
        this(20);
    }

    public Graph(int expectedMaxVertices) {
        this.maxVertices = expectedMaxVertices;
        this.adjMatrix = new double[maxVertices][maxVertices];
    }

    private void resizeMatrix() {
        int newSize = maxVertices * 2;
        double[][] newMatrix = new double[newSize][newSize];
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                newMatrix[i][j] = adjMatrix[i][j];
            }
        }
        this.adjMatrix = newMatrix;
        this.maxVertices = newSize;
    }

    public void addVertex(String vertex) {
        if (nodeToIdx.containsKey(vertex)) {
            return;
        }
        if (nodeCount >= maxVertices) {
            resizeMatrix();
        }
        nodeToIdx.put(vertex, nodeCount);
        idxToNode.put(nodeCount, vertex);
        nodeCount++;
        adjList.put(vertex, new LinkedHashMap<>());
    }

    public void addEdge(String u, String v, double weight) {
        if (!nodeToIdx.containsKey(u)) {
            addVertex(u);
        }
        if (!nodeToIdx.containsKey(v)) {
            addVertex(v);
        }

        adjList.get(u).put(v, weight);
        adjList.get(v).put(u, weight);

        int uIdx = nodeToIdx.get(u);
        int vIdx = nodeToIdx.get(v);
        adjMatrix[uIdx][vIdx] = weight;
        adjMatrix[vIdx][uIdx] = weight;
    }

    public void addEdge(String u, String v) {
        addEdge(u, v, 1.0);
    }

    public List<String> getNeighbors(String vertex) {
        if (!adjList.containsKey(vertex)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adjList.get(vertex).keySet());
    }

    public List<String> bfs(String startVertex) {
        List<String> traversalOrder = new ArrayList<>();
        if (!nodeToIdx.containsKey(startVertex)) {
            return traversalOrder;
        }

        Set<String> visited = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();

        visited.add(startVertex);
        queue.addLast(startVertex);

        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            traversalOrder.add(current);

            for (String neighbor : adjList.get(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.addLast(neighbor);
                }
            }
        }
        return traversalOrder;
    }

    public List<String> dfs(String startVertex) {
        List<String> traversalOrder = new ArrayList<>();
        if (!adjList.containsKey(startVertex)) {
            return traversalOrder;
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            String current = stack.pop();

            if (!visited.contains(current)) {
                visited.add(current);
                traversalOrder.add(current);

                for (String neighbor : adjList.get(current).keySet()) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        return traversalOrder;
    }

    public Map<String, Double> dijkstra(String startVertex) {
        return Dijkstra.shortestPaths(this, startVertex);
    }

    public void displayMatrix() {
        System.out.println("\nAdjacency matrix representation");

        StringBuilder header = new StringBuilder("     ");
        for (int i = 0; i < nodeCount; i++) {
            header.append(String.format("%8s ", idxToNode.get(i)));
        }
        System.out.println(header.toString().stripTrailing());

        for (int i = 0; i < nodeCount; i++) {
            StringBuilder row = new StringBuilder(String.format("%8s ", idxToNode.get(i)));
            for (int j = 0; j < nodeCount; j++) {
                row.append(String.format("%8s ", formatWeight(adjMatrix[i][j])));
            }
            System.out.println(row.toString().stripTrailing());
        }
    }

    private static String formatWeight(double w) {
        if (w == Math.rint(w) && !Double.isInfinite(w)) {
            return Long.toString((long) w);
        }
        return Double.toString(w);
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getIndex(String vertex) {
        return nodeToIdx.getOrDefault(vertex, -1);
    }

    public String getVertexAt(int index) {
        return idxToNode.get(index);
    }

    public List<String> getVertices() {
        return new ArrayList<>(nodeToIdx.keySet());
    }

    public double getWeight(String u, String v) {
        Map<String, Double> edges = adjList.get(u);
        if (edges == null) {
            return 0.0;
        }
        return edges.getOrDefault(v, 0.0);
    }
}