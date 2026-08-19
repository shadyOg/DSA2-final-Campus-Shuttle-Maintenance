package com.campus.optimizer.graph;

import com.campus.optimizer.structures.CustomSet;
import com.campus.optimizer.structures.MinHeap;

import java.util.ArrayList;
import java.util.List;

public final class Prim {

    private Prim() {
    }

    public static List<MstEdge> minimumSpanningTree(Graph graph) {
        List<MstEdge> minimumSpanningTree = new ArrayList<>();
        List<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            return minimumSpanningTree;
        }

        CustomSet<String> visited = new CustomSet<>();
        MinHeap<CandidateEdge> queue = new MinHeap<>();
        String startVertex = vertices.get(0);
        visited.add(startVertex);
        addEdgesToQueue(graph, startVertex, queue, visited);

        while (!queue.isEmpty() && minimumSpanningTree.size() < vertices.size() - 1) {
            CandidateEdge candidate = queue.poll();
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
            Graph graph, String source, MinHeap<CandidateEdge> queue, CustomSet<String> visited) {
        for (String neighbor : graph.getNeighbors(source)) {
            if (!visited.contains(neighbor)) {
                queue.offer(new CandidateEdge(source, neighbor, graph.getWeight(source, neighbor)));
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
