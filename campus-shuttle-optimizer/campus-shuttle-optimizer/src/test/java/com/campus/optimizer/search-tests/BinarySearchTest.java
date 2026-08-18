package com.campus.optimizer.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinarySearchTest {

    @Test
    public void shouldFindTargetInSortedArray() {
        int[] array = {4, 8, 12, 25, 42, 68};

        assertEquals(4, BinarySearch.search(array, 42));
    }

    @Test
    public void shouldReturnMinusOneWhenTargetIsNotFound() {
        int[] array = {4, 8, 12, 25, 42, 68};

        assertEquals(-1, BinarySearch.search(array, 100));
    }

    @Test
    public void shouldFindTargetAtFirstPosition() {
        int[] array = {4, 8, 12, 25, 42, 68};

        assertEquals(0, BinarySearch.search(array, 4));
    }

    @Test
    public void shouldFindTargetAtLastPosition() {
        int[] array = {4, 8, 12, 25, 42, 68};

        assertEquals(5, BinarySearch.search(array, 68));
    }

    @Test
    public void shouldFindTargetInSingleElementArray() {
        int[] array = {42};

        assertEquals(0, BinarySearch.search(array, 42));
    }

    @Test
    public void shouldReturnMinusOneForEmptyArray() {
        int[] array = {};

        assertEquals(-1, BinarySearch.search(array, 42));
    }
}
