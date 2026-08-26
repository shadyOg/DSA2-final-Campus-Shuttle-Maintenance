package com.campus.optimizer.graph;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.DatabaseLoader;
import com.campus.optimizer.db.RoadDAO;
import com.campus.optimizer.model.AlgorithmRun;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * Runs BFS and DFS against the real DB-loaded campus graph (locations + roads,
 * via {@link GraphLoader}) and benchmarks traversal time, persisting the results
 * to the algorithm_runs table through {@link AlgorithmRunDAO}.
 *
 * Each traversal is timed once per vertex as the start point, since the campus
 * graph (~55 locations) is too small for a single run to give a stable
 * nanoTime reading; the average across all starts is what gets logged.
 */
public final class GraphTraversalBenchmark {

    private GraphTraversalBenchmark() {
    }

    public static void main(String[] args) {
        DatabaseLoader.initDatabase();

        Graph graph = GraphLoader.loadFromDatabase();
        List<String> vertices = graph.getVertices();
        int roadCount = new RoadDAO().findAll().size();

        System.out.println("Loaded campus graph: " + vertices.size() + " locations, " + roadCount + " roads.");

        if (vertices.isEmpty()) {
            System.err.println("Graph is empty - nothing to traverse.");
            return;
        }

        checkConnectivity(graph, vertices);

        TraversalResult bfsResult = benchmark("BFS", vertices, graph::bfs);
        TraversalResult dfsResult = benchmark("DFS", vertices, graph::dfs);

        System.out.println("BFS from \"" + vertices.get(0) + "\": " + graph.bfs(vertices.get(0)));
        System.out.println("DFS from \"" + vertices.get(0) + "\": " + graph.dfs(vertices.get(0)));

        report(bfsResult);
        report(dfsResult);

        persist(bfsResult, vertices.size());
        persist(dfsResult, vertices.size());
    }

    private static void checkConnectivity(Graph graph, List<String> vertices) {
        String start = vertices.get(0);
        int reachable = graph.bfs(start).size();
        if (reachable < vertices.size()) {
            System.out.println("Connectivity check: only " + reachable + "/" + vertices.size()
                    + " locations reachable from \"" + start + "\" - campus graph has disconnected components.");
        } else {
            System.out.println("Connectivity check: all " + vertices.size()
                    + " locations reachable from \"" + start + "\".");
        }
    }

    private static TraversalResult benchmark(String algorithmName, List<String> vertices,
                                              Function<String, List<String>> traversal) {
        // Warm-up run so JIT compilation doesn't skew the first measured pass.
        traversal.apply(vertices.get(0));

        long totalNanos = 0;
        for (String start : vertices) {
            long begin = System.nanoTime();
            traversal.apply(start);
            totalNanos += System.nanoTime() - begin;
        }

        long averageNanos = totalNanos / vertices.size();
        return new TraversalResult(algorithmName, vertices.size(), totalNanos, averageNanos);
    }

    private static void report(TraversalResult result) {
        System.out.printf("%s benchmark: %d runs (one per start vertex), total %,d ns, average %,d ns/run%n",
                result.algorithmName, result.runs, result.totalNanos, result.averageNanos);
    }

    private static void persist(TraversalResult result, int inputSize) {
        AlgorithmRun run = new AlgorithmRun(
                0,
                result.algorithmName,
                inputSize,
                result.averageNanos,
                0.0,
                LocalDate.now().toString());
        new AlgorithmRunDAO().insert(run);
        System.out.println("Persisted " + result.algorithmName + " run to algorithm_runs (runId=" + run.getRunId() + ").");
    }

    private static final class TraversalResult {
        private final String algorithmName;
        private final int runs;
        private final long totalNanos;
        private final long averageNanos;

        private TraversalResult(String algorithmName, int runs, long totalNanos, long averageNanos) {
            this.algorithmName = algorithmName;
            this.runs = runs;
            this.totalNanos = totalNanos;
            this.averageNanos = averageNanos;
        }
    }
}
