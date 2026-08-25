package com.campus.optimizer.sort;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.DatabaseConnection;
import com.campus.optimizer.model.AlgorithmRun;

import java.time.LocalDateTime;
import java.util.Arrays;

@FunctionalInterface
interface IntArraySorter {
    int[] sort(int[] input);
}

public final class BenchmarkHarness {

    private BenchmarkHarness() {
        // Utility class
    }

    public static BenchmarkRecord benchmark(String algorithmName, int[] input, IntArraySorter sorter) {
        int[] copy = input == null ? new int[0] : Arrays.copyOf(input, input.length);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime();
        int[] sorted = sorter.sort(copy);
        long runtimeNanos = System.nanoTime() - startTime;

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        double memoryKb = Math.max(0, memoryAfter - memoryBefore) / 1024.0;

        BenchmarkRecord record = new BenchmarkRecord(
            algorithmName,
            copy.length,
            sorted == null ? 0 : sorted.length,
            runtimeNanos
        );

        persist(record, memoryKb);

        return record;
    }

    /**
     * Persists the run to {@code algorithm_runs} so BenchmarkHarness results are
     * actually queryable later instead of only ever printed to stdout. Schema
     * creation is idempotent, so this is safe to call even if nothing has
     * initialized the database yet in this process (e.g. a benchmark-only test).
     */
    private static void persist(BenchmarkRecord record, double memoryKb) {
        DatabaseConnection.getInstance().initializeDatabase();

        AlgorithmRun run = new AlgorithmRun();
        run.setAlgorithmName(record.getAlgorithmName());
        run.setInputSize(record.getInputSize());
        run.setTimeNs(record.getRuntimeNanos());
        run.setMemoryKb(memoryKb);
        run.setDateRun(LocalDateTime.now().toString());

        new AlgorithmRunDAO().insert(run);
    }
}
