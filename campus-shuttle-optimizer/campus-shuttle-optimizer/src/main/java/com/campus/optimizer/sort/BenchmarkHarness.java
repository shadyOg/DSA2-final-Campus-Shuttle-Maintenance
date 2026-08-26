package com.campus.optimizer.sort;

import com.campus.optimizer.db.AlgorithmRunDAO;
import com.campus.optimizer.db.DatabaseLoader;
import com.campus.optimizer.model.AlgorithmRun;
import com.campus.optimizer.search.BinarySearch;
import com.campus.optimizer.search.LinearSearch;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /** Runs all required search and sort benchmarks for the supplied input sizes. */
    public static List<BenchmarkRecord> runSuite(int... inputSizes) {
        List<BenchmarkRecord> records = new ArrayList<>();
        for (int inputSize : inputSizes) {
            if (inputSize < 0) {
                throw new IllegalArgumentException("Input sizes cannot be negative");
            }

            int[] input = createInput(inputSize);
            int[] sortedInput = SortingAlgorithms.mergeSort(input);
            int target = inputSize == 0 ? 0 : sortedInput[inputSize - 1];

            records.add(benchmarkSearch("Linear Search", input, target, LinearSearch::search));
            records.add(benchmarkSearch("Binary Search", sortedInput, target, BinarySearch::search));
            records.add(benchmark("Selection Sort", input, values -> {
                SelectionSort.sort(values);
                return values;
            }));
            records.add(benchmark("Insertion Sort", input, SortingAlgorithms::insertionSort));
            records.add(benchmark("Merge Sort", input, SortingAlgorithms::mergeSort));
            records.add(benchmark("Quick Sort", input, SortingAlgorithms::quickSort));
        }
        return records;
    }

    /** Runs the Phase 2 suite for N = 10, 100, 1000, and 10000 and persists its rows. */
    public static List<BenchmarkRecord> runAndPersistDefaultSuite() {
        DatabaseLoader.initDatabase();
        List<BenchmarkRecord> records = runSuite(10, 100, 1000, 10000);
        AlgorithmRunDAO dao = new AlgorithmRunDAO();
        for (BenchmarkRecord record : records) {
            AlgorithmRun run = new AlgorithmRun(
                    0,
                    record.getAlgorithmName(),
                    record.getInputSize(),
                    record.getRuntimeNanos(),
                    0.0,
                    java.time.LocalDate.now().toString());
            dao.insert(run);
        }
        return records;
    }

    public static void main(String[] args) {
        List<BenchmarkRecord> records = runAndPersistDefaultSuite();
        for (BenchmarkRecord record : records) {
            System.out.printf("%-16s N=%-5d time=%,d ns%n",
                    record.getAlgorithmName(), record.getInputSize(), record.getRuntimeNanos());
        }
    }

    private static BenchmarkRecord benchmarkSearch(
            String algorithmName, int[] input, int target, IntArraySearcher searcher) {
        long startTime = System.nanoTime();
        int result = searcher.search(input, target);
        long runtimeNanos = System.nanoTime() - startTime;
        return new BenchmarkRecord(algorithmName, input.length, result >= 0 ? 1 : 0, runtimeNanos);
    }

    private static int[] createInput(int size) {
        int[] input = new int[size];
        Random random = new Random(20260826L + size);
        for (int index = 0; index < size; index++) {
            input[index] = random.nextInt(Math.max(1, size * 10));
        }
        return input;
    }

    @FunctionalInterface
    private interface IntArraySearcher {
        int search(int[] input, int target);
    }
}
