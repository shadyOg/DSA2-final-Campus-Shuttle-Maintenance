package com.campus.optimizer.graph;

import com.campus.optimizer.structures.CustomMap;
import com.campus.optimizer.structures.CustomSet;
import com.campus.optimizer.structures.DynamicArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class Graph {

    private final CustomMap<String, Integer> nodeToIdx = new CustomMap<>();
    private final CustomMap<Integer, String> idxToNode = new CustomMap<>();
    private int nodeCount = 0;

    private final CustomMap<String, CustomMap<String, Double>> adjList = new CustomMap<>();

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
        adjList.put(vertex, new CustomMap<>());
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
        DynamicArray<String> neighbors = adjList.get(vertex).keySet();
        List<String> result = new ArrayList<>();
        for (int index = 0; index < neighbors.size(); index++) {
            result.add(neighbors.get(index));
        }
        return result;
    }

    public List<String> bfs(String startVertex) {
        List<String> traversalOrder = new ArrayList<>();
        if (!nodeToIdx.containsKey(startVertex)) {
            return traversalOrder;
        }

        CustomSet<String> visited = new CustomSet<>();
        Deque<String> queue = new ArrayDeque<>();

        visited.add(startVertex);
        queue.addLast(startVertex);

        while (!queue.isEmpty()) {
            String current = queue.removeFirst();
            traversalOrder.add(current);

            DynamicArray<String> neighbors = adjList.get(current).keySet();
            for (int index = 0; index < neighbors.size(); index++) {
                String neighbor = neighbors.get(index);
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

        CustomSet<String> visited = new CustomSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            String current = stack.pop();

            if (!visited.contains(current)) {
                visited.add(current);
                traversalOrder.add(current);

                DynamicArray<String> neighbors = adjList.get(current).keySet();
                for (int index = 0; index < neighbors.size(); index++) {
                    String neighbor = neighbors.get(index);
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
        DynamicArray<String> vertices = nodeToIdx.keySet();
        List<String> result = new ArrayList<>();
        for (int index = 0; index < vertices.size(); index++) {
            result.add(vertices.get(index));
        }
        return result;
    }

    public double getWeight(String u, String v) {
        CustomMap<String, Double> edges = adjList.get(u);
        if (edges == null) {
            return 0.0;
        }
        return edges.getOrDefault(v, 0.0);
    }
}