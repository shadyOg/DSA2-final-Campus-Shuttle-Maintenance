package com.campus.optimizer.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public final class Prim {

    private Prim() {
    }

    public static List<MstEdge> minimumSpanningTree(Graph graph) {
        List<MstEdge> minimumSpanningTree = new ArrayList<>();
        List<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            return minimumSpanningTree;
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<CandidateEdge> queue = new PriorityQueue<>();
        String startVertex = vertices.get(0);
        visited.add(startVertex);
        addEdgesToQueue(graph, startVertex, queue, visited);

        while (!queue.isEmpty() && minimumSpanningTree.size() < vertices.size() - 1) {
            CandidateEdge candidate = queue.remove();
            if (visited.contains(candidate.destination)) {
                continue;
            }

            visited.add(candidate.destination);
            minimumSpanningTree.add(new MstEdge(
                    candidate.source, candidate.destination, candidate.weight));
            addEdgesToQueue(graph, candidate.destination, queue, visited);
        }

        ensureConnected(vertices, minimumSpanningTree);
        return minimumSpanningTree;
    }

    public static double totalWeight(Graph graph) {
        return minimumSpanningTree(graph).stream()
                .mapToDouble(MstEdge::getWeight)
                .sum();
    }

    private static void addEdgesToQueue(
            Graph graph, String source, PriorityQueue<CandidateEdge> queue, Set<String> visited) {
        for (String neighbor : graph.getNeighbors(source)) {
            if (!visited.contains(neighbor)) {
                queue.add(new CandidateEdge(source, neighbor, graph.getWeight(source, neighbor)));
            }
        }
    }

    private static void ensureConnected(List<String> vertices, List<MstEdge> minimumSpanningTree) {
        if (minimumSpanningTree.size() != vertices.size() - 1) {
            throw new IllegalArgumentException("An MST requires a connected graph");
        }
    }

    private static final class CandidateEdge implements Comparable<CandidateEdge> {
        private final String source;
        private final String destination;
        private final double weight;

        private CandidateEdge(String source, String destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public int compareTo(CandidateEdge other) {
            return Double.compare(weight, other.weight);
        }
    }
}
