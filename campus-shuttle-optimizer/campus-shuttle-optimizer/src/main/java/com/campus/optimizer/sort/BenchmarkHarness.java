package com.campus.optimizer.sort;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.model.AlgorithmRun;
import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.Arrays;

@FunctionalInterface
interface IntArraySorter {
    int[] sort(int[] input);
}

public final class BenchmarkHarness {

    private BenchmarkHarness() {
        // Utility class
    }

    public static BenchmarkRecord benchmark(
        String algorithmName,
        int[] input,
        IntArraySorter sorter) {

    int[] copy = input == null
            ? new int[0]
            : Arrays.copyOf(input, input.length);

    Runtime runtime = Runtime.getRuntime();

    runtime.gc();
    long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

    long startTime = System.nanoTime();

    int[] sorted = sorter.sort(copy);

    long runtimeNanos = System.nanoTime() - startTime;

    long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

    double memoryKb = (memoryAfter - memoryBefore) / 1024.0;

    BenchmarkRecord record = new BenchmarkRecord(
            algorithmName,
            copy.length,
            sorted == null ? 0 : sorted.length,
            runtimeNanos
    );

    // Convert BenchmarkRecord → AlgorithmRun
    AlgorithmRun run = new AlgorithmRun();

    run.setAlgorithmName(record.getAlgorithmName());
    run.setInputSize(record.getInputSize());
    run.setTimeNs(record.getRuntimeNanos());
    run.setMemoryKb(memoryKb);
    run.setDateRun(LocalDateTime.now().toString());

    // Save the result to algorithm_runs
    AlgorithmRunDAO dao = new AlgorithmRunDAO();
    dao.insert(run);

    return record;
}

        return new BenchmarkRecord(
            algorithmName,
            copy.length,
            sorted == null ? 0 : sorted.length,
            runtimeNanos
        );
    }
}
