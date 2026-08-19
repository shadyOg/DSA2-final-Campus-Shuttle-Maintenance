package com.campus.optimizer.sort;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SortingAlgorithmsTest {

    @Test
    public void insertionSort_sortsNumbersAscending() {
        int[] input = {9, 4, 1, 7, 3, 10, 2};

        int[] result = SortingAlgorithms.insertionSort(input);

        assertArrayEquals(new int[] {1, 2, 3, 4, 7, 9, 10}, result);
    }

    @Test
    public void mergeSort_handlesDuplicatesAndNegatives() {
        int[] input = {5, -3, 8, -3, 0, 12, 8};

        int[] result = SortingAlgorithms.mergeSort(input);

        assertArrayEquals(new int[] {-3, -3, 0, 5, 8, 8, 12}, result);
    }

    @Test
    public void quickSort_sortsNumbersAscending() {
        int[] input = {40, 12, 7, 28, 19, 35, 9};

        int[] result = SortingAlgorithms.quickSort(input);

        assertArrayEquals(new int[] {7, 9, 12, 19, 28, 35, 40}, result);
    }

    @Test
    public void benchmarkHarness_recordsRuntimePerInputSize() {
        int[] input = {9, 4, 1, 7, 3, 10, 2};

        BenchmarkRecord record = BenchmarkHarness.benchmark("insertion-sort", input, SortingAlgorithms::insertionSort);

        assertNotNull(record);
        assertEquals("insertion-sort", record.getAlgorithmName());
        assertEquals(7, record.getInputSize());
        assertEquals(7, record.getSortedSize());
        assertTrue(record.getRuntimeNanos() >= 0L);
    }
}
