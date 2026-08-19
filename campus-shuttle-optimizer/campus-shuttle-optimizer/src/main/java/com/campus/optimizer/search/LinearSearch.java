package com.campus.optimizer.search;

public class LinearSearch {

    // Returns the index of target, or -1 if target is not found.
    public static int search(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }

        return -1;
    }
}
