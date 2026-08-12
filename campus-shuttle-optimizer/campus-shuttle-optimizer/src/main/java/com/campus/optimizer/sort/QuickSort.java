package com.campus.optimizer.sort;

import java.util.Comparator;
import java.util.Random;

/**
 * A robust Quicksort sorting engine.
 * 
 * Satisfies the Search/Sort requirements. Implements Median-of-Three pivot
 * selection to avoid O(n^2) worst-case on pre-sorted/reversed arrays.
 */
public class QuickSort {

    /**
     * Sorts an array using the natural ordering of its elements.
     * Time Complexity: O(n log n) average, O(n log n) with Median-of-Three.
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1, Comparator.naturalOrder());
    }

    /**
     * Sorts an array using a custom comparator.
     */
    public static <T> void sort(T[] array, Comparator<? super T> comparator) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1, comparator);
    }

    private static <T> void quickSort(T[] array, int low, int high, Comparator<? super T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<? super T> comparator) {
        // Median-of-three pivot selection
        int mid = low + (high - low) / 2;
        medianOfThree(array, low, mid, high, comparator);
        
        // After medianOfThree, the pivot is placed at high - 1 for partitioning efficiency.
        // Let's swap the pivot to the end (high) if it's not already there.
        swap(array, mid, high - 1);
        T pivot = array[high - 1];
        
        int i = low - 1;
        int j = high - 1;
        
        for (int k = low; k < high - 1; k++) {
            if (comparator.compare(array[k], pivot) <= 0) {
                i++;
                swap(array, i, k);
            }
        }
        // Place the pivot in its correct position
        swap(array, i + 1, high - 1);
        return i + 1;
    }

    /**
     * Sorts low, mid, and high elements of the array.
     * Places the median element at 'mid'.
     */
    private static <T> void medianOfThree(T[] array, int low, int mid, int high, Comparator<? super T> comparator) {
        if (comparator.compare(array[low], array[mid]) > 0) {
            swap(array, low, mid);
        }
        if (comparator.compare(array[low], array[high]) > 0) {
            swap(array, low, high);
        }
        if (comparator.compare(array[mid], array[high]) > 0) {
            swap(array, mid, high);
        }
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Benchmarks Quicksort on random arrays of Integer of specified size.
     * Returns runtime in nanoseconds.
     */
    public static long benchmark(int size) {
        Integer[] array = new Integer[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(1_000_000);
        }

        long start = System.nanoTime();
        sort(array);
        long end = System.nanoTime();
        
        return end - start;
    }
}
