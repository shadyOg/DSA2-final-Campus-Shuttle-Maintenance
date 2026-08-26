package com.campus.optimizer.graph;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.DatabaseLoader;
import com.campus.optimizer.model.AlgorithmRun;

import java.time.LocalDate;
import java.util.List;

/** Benchmarks database-loaded routing and spanning-tree algorithms. */
public final class GraphOptimizationBenchmark {

    private GraphOptimizationBenchmark() {
    }

    public static void main(String[] args) {
        DatabaseLoader.initDatabase();
        Graph graph = GraphLoader.loadFromDatabase();
        List<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            System.err.println("Graph is empty - nothing to benchmark.");
            return;
        }

        benchmarkDijkstra(graph, vertices);
        benchmarkMst("Prim", graph, vertices, true);
        benchmarkMst("Kruskal", graph, vertices, false);
    }

    private static void benchmarkDijkstra(Graph graph, List<String> vertices) {
        graph.dijkstra(vertices.get(0));
        long start = System.nanoTime();
        for (String vertex : vertices) {
            graph.dijkstra(vertex);
        }
        long elapsed = System.nanoTime() - start;
        persist("Dijkstra", vertices.size(), elapsed / (double) vertices.size());
    }

    private static void benchmarkMst(String name, Graph graph, List<String> vertices, boolean prim) {
        runMst(graph, prim);
        long start = System.nanoTime();
        runMst(graph, prim);
        long elapsed = System.nanoTime() - start;
        persist(name, vertices.size(), elapsed);
    }

    private static void runMst(Graph graph, boolean prim) {
        if (prim) {
            Prim.minimumSpanningTree(graph);
        } else {
            Kruskal.minimumSpanningTree(graph);
        }
    }

    private static void persist(String algorithmName, int inputSize, double elapsedNanos) {
        AlgorithmRun run = new AlgorithmRun(
                0, algorithmName, inputSize, elapsedNanos, 0.0, LocalDate.now().toString());
        new AlgorithmRunDAO().insert(run);
        System.out.printf("%s: %,d ns (persisted runId=%d)%n",
                algorithmName, (long) elapsedNanos, run.getRunId());
    }
}