package com.campus.optimizer.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Kruskal {

    private Kruskal() {
    }

    public static List<MstEdge> minimumSpanningTree(Graph graph) {
        List<String> vertices = graph.getVertices();
        List<EdgeCandidate> edges = getUniqueEdges(graph, vertices);
        edges.sort(Comparator.comparingDouble(edge -> edge.weight));

        DisjointSet disjointSet = new DisjointSet(vertices.size());
        List<MstEdge> minimumSpanningTree = new ArrayList<>();

        for (EdgeCandidate edge : edges) {
            int sourceIndex = graph.getIndex(edge.source);
            int destinationIndex = graph.getIndex(edge.destination);
            if (disjointSet.union(sourceIndex, destinationIndex)) {
                minimumSpanningTree.add(new MstEdge(edge.source, edge.destination, edge.weight));
                if (minimumSpanningTree.size() == vertices.size() - 1) {
                    break;
                }
            }
        }

        if (!vertices.isEmpty() && minimumSpanningTree.size() != vertices.size() - 1) {
            throw new IllegalArgumentException("An MST requires a connected graph");
        }
        return minimumSpanningTree;
    }

    public static double totalWeight(Graph graph) {
        return minimumSpanningTree(graph).stream()
                .mapToDouble(MstEdge::getWeight)
                .sum();
    }

    private static List<EdgeCandidate> getUniqueEdges(Graph graph, List<String> vertices) {
        List<EdgeCandidate> edges = new ArrayList<>();
        for (String source : vertices) {
            for (String destination : graph.getNeighbors(source)) {
                if (graph.getIndex(source) < graph.getIndex(destination)) {
                    edges.add(new EdgeCandidate(source, destination, graph.getWeight(source, destination)));
                }
            }
        }
        return edges;
    }

    private static final class EdgeCandidate {
        private final String source;
        private final String destination;
        private final double weight;

        private EdgeCandidate(String source, String destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static final class DisjointSet {
        private final int[] parent;
        private final int[] rank;

        private DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int index = 0; index < size; index++) {
                parent[index] = index;
            }
        }

        private boolean union(int first, int second) {
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
            return true;
        }

        private int find(int vertex) {
            if (parent[vertex] != vertex) {
                parent[vertex] = find(parent[vertex]);
            }
            return parent[vertex];
        }
    }
}
