package com.campus.optimizer.graph;

import com.campus.optimizer.structures.MinHeap;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Dijkstra {

    private Dijkstra() {
    }

    public static Map<String, Double> shortestPaths(Graph graph, String startVertex) {
        Map<String, Double> distances = new LinkedHashMap<>();
        if (graph.getIndex(startVertex) < 0) {
            return distances;
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
                    queue.offer(new VertexDistance(neighbor, newDistance));
                }
            }
        }

        return distances;
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
