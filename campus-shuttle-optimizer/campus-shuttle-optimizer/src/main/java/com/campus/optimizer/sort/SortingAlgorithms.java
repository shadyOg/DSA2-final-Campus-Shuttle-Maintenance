package com.campus.optimizer.sort;

public final class SortingAlgorithms {

    private SortingAlgorithms() {
        // Utility class
    }

    public static int[] insertionSort(int[] input) {
        if (input == null) {
            return null;
        }

        int[] sorted = input.clone();
        for (int i = 1; i < sorted.length; i++) {
            int current = sorted[i];
            int j = i - 1;

            while (j >= 0 && sorted[j] > current) {
                sorted[j + 1] = sorted[j];
                j--;
            }

            sorted[j + 1] = current;
        }

        return sorted;
    }

    public static int[] mergeSort(int[] input) {
        if (input == null) {
            return null;
        }

        int[] sorted = input.clone();
        if (sorted.length <= 1) {
            return sorted;
        }

        mergeSortRange(sorted, 0, sorted.length - 1);
        return sorted;
    }

    private static void mergeSortRange(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int middle = left + (right - left) / 2;
        mergeSortRange(array, left, middle);
        mergeSortRange(array, middle + 1, right);
        merge(array, left, middle, right);
    }

    private static void merge(int[] array, int left, int middle, int right) {
        int[] leftArray = new int[middle - left + 1];
        int[] rightArray = new int[right - middle];

        for (int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[left + i];
        }

        for (int i = 0; i < rightArray.length; i++) {
            rightArray[i] = array[middle + 1 + i];
        }

        int leftIndex = 0;
        int rightIndex = 0;
        int targetIndex = left;

        while (leftIndex < leftArray.length && rightIndex < rightArray.length) {
            if (leftArray[leftIndex] <= rightArray[rightIndex]) {
                array[targetIndex] = leftArray[leftIndex];
                leftIndex++;
            } else {
                array[targetIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            targetIndex++;
        }

        while (leftIndex < leftArray.length) {
            array[targetIndex] = leftArray[leftIndex];
            leftIndex++;
            targetIndex++;
        }

        while (rightIndex < rightArray.length) {
            array[targetIndex] = rightArray[rightIndex];
            rightIndex++;
            targetIndex++;
        }
    }

    public static int[] quickSort(int[] input) {
        if (input == null) {
            return null;
        }

        int[] sorted = input.clone();
        if (sorted.length <= 1) {
            return sorted;
        }

        quickSortRange(sorted, 0, sorted.length - 1);
        return sorted;
    }

    private static void quickSortRange(int[] array, int low, int high) {
        if (low >= high) {
            return;
        }

        int pivotIndex = partition(array, low, high);
        quickSortRange(array, low, pivotIndex - 1);
        quickSortRange(array, pivotIndex + 1, high);
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int boundary = low - 1;

        for (int i = low; i < high; i++) {
            if (array[i] <= pivot) {
                boundary++;
                swap(array, boundary, i);
            }
        }

        swap(array, boundary + 1, high);
        return boundary + 1;
    }

    private static void swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }
}
