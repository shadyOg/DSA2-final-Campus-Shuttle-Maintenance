package com.campus.optimizer.sort;

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
        long startTime = System.nanoTime();
        int[] sorted = sorter.sort(copy);
        long runtimeNanos = System.nanoTime() - startTime;

        return new BenchmarkRecord(
            algorithmName,
            copy.length,
            sorted == null ? 0 : sorted.length,
            runtimeNanos
        );
    }
}
