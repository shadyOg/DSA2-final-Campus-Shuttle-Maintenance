package com.campus.optimizer.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearSearchTest {

    @Test
    public void shouldFindTargetInArray() {
        int[] array = {68, 25, 42, 8, 12};

        assertEquals(2, LinearSearch.search(array, 42));
    }

    @Test
    public void shouldReturnMinusOneWhenTargetIsNotFound() {
        int[] array = {68, 25, 42, 8, 12};

        assertEquals(-1, LinearSearch.search(array, 100));
    }

    @Test
    public void shouldFindTargetAtFirstPosition() {
        int[] array = {68, 25, 42, 8, 12};

        assertEquals(0, LinearSearch.search(array, 68));
    }

    @Test
    public void shouldFindTargetAtLastPosition() {
        int[] array = {68, 25, 42, 8, 12};

        assertEquals(4, LinearSearch.search(array, 12));
    }

    @Test
    public void shouldReturnMinusOneForEmptyArray() {
        int[] array = {};

        assertEquals(-1, LinearSearch.search(array, 42));
    }

    @Test
    public void shouldReturnFirstOccurrenceWhenTargetAppearsMoreThanOnce() {
        int[] array = {68, 25, 42, 25, 12};

        assertEquals(1, LinearSearch.search(array, 25));
    }
}
