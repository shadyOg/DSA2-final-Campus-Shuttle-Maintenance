package com.campus.optimizer.graph;

import com.campus.optimizer.structures.MinHeap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public final class Dijkstra {

    private Dijkstra() {
    }

    public static Map<String, Double> shortestPaths(Graph graph, String startVertex) {
        return calculate(graph, startVertex).distances;
    }

    public static RouteResult shortestRoute(Graph graph, String startVertex, String destinationVertex) {
        if (destinationVertex == null || graph.getIndex(destinationVertex) < 0) {
            return RouteResult.unreachable(startVertex, destinationVertex);
        }

        Calculation calculation = calculate(graph, startVertex);
        Double distance = calculation.distances.get(destinationVertex);
        if (distance == null || distance.isInfinite()) {
            return RouteResult.unreachable(startVertex, destinationVertex);
        }

        List<String> path = new ArrayList<>();
        String current = destinationVertex;
        while (current != null) {
            path.add(current);
            current = calculation.predecessors.get(current);
        }
        Collections.reverse(path);
        return new RouteResult(startVertex, destinationVertex, path, distance);
    }

    private static Calculation calculate(Graph graph, String startVertex) {
        Map<String, Double> distances = new LinkedHashMap<>();
        Map<String, String> predecessors = new LinkedHashMap<>();
        if (graph.getIndex(startVertex) < 0) {
            return new Calculation(distances, predecessors);
        }

        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(startVertex, 0.0);

        MinHeap<VertexDistance> queue = new MinHeap<>();
        queue.offer(new VertexDistance(startVertex, 0.0));

        while (!queue.isEmpty()) {
            VertexDistance current = queue.poll();
            if (current.distance > distances.get(current.vertex)) {
                continue;
            }

            for (String neighbor : graph.getNeighbors(current.vertex)) {
                double weight = graph.getWeight(current.vertex, neighbor);
                if (weight < 0) {
                    throw new IllegalArgumentException("Dijkstra's algorithm requires non-negative edge weights");
                }

                double newDistance = current.distance + weight;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    predecessors.put(neighbor, current.vertex);
                    queue.offer(new VertexDistance(neighbor, newDistance));
                }
            }
        }

        return new Calculation(distances, predecessors);
    }

    private static final class Calculation {
        private final Map<String, Double> distances;
        private final Map<String, String> predecessors;

        private Calculation(Map<String, Double> distances, Map<String, String> predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }
    }

    private static final class VertexDistance implements Comparable<VertexDistance> {
        private final String vertex;
        private final double distance;

        private VertexDistance(String vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance other) {
            return Double.compare(distance, other.distance);
        }
    }
}
