package com.campus.optimizer.graph;

import com.campus.optimizer.structures.DisjointSet;

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

}
