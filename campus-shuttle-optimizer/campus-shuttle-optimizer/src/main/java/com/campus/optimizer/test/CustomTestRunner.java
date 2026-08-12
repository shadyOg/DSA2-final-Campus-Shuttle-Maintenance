package com.campus.optimizer.test;

import com.campus.optimizer.linear.Deque;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.ServiceRequest;
import com.campus.optimizer.sort.QuickSort;

import java.util.NoSuchElementException;

/**
 * A lightweight custom unit testing framework for Deque and QuickSort.
 * Runs assertions and outputs test results in a clear format.
 */
public class CustomTestRunner {

    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("    RUNNING UNIT TESTS FOR DEQUE & QUICKSORT");
        System.out.println("==================================================");

        try {
            // Deque Tests
            runTest("Deque: Empty Check", CustomTestRunner::testDequeEmpty);
            runTest("Deque: Insert Front & Rear", CustomTestRunner::testDequeInsert);
            runTest("Deque: Remove Front & Rear", CustomTestRunner::testDequeRemove);
            runTest("Deque: Exception Handling", CustomTestRunner::testDequeExceptions);
            runTest("Deque: Clear & Re-use", CustomTestRunner::testDequeClear);

            // Quicksort Tests
            runTest("QuickSort: Empty & Single Element", CustomTestRunner::testSortEdgeCases);
            runTest("QuickSort: Random Array Sorting", CustomTestRunner::testSortRandom);
            runTest("QuickSort: Sorted & Reversed Arrays", CustomTestRunner::testSortSortedReversed);
            runTest("QuickSort: Service Request Priority Sorting", CustomTestRunner::testSortServiceRequests);

        } catch (Exception e) {
            System.err.println("Fatal error during test run: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n==================================================");
        System.out.printf("  TEST SUMMARY: %d / %d PASSED\n", passedTests, totalTests);
        System.out.println("==================================================");

        if (passedTests != totalTests) {
            System.exit(1);
        }
    }

    private static void runTest(String name, Runnable testBlock) {
        totalTests++;
        try {
            testBlock.run();
            System.out.printf("[PASS] %s\n", name);
            passedTests++;
        } catch (AssertionError | Exception e) {
            System.out.printf("[FAIL] %s\n", name);
            System.out.println("  -> Detail: " + e.getMessage());
        }
    }

    private static void assertTrue(boolean condition, String msg) {
        if (!condition) {
            throw new AssertionError("Expected true, got false: " + msg);
        }
    }

    private static void assertEquals(Object expected, Object actual, String msg) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError(String.format("Expected '%s', but got '%s': %s", expected, actual, msg));
        }
    }

    // --- Deque Tests ---

    private static void testDequeEmpty() {
        Deque<Integer> d = new Deque<>();
        assertTrue(d.isEmpty(), "New Deque should be empty");
        assertEquals(0, d.size(), "New Deque size should be 0");
    }

    private static void testDequeInsert() {
        Deque<String> d = new Deque<>();
        d.insertFront("A");
        d.insertRear("B");
        d.insertFront("C");

        // Elements should be: C -> A -> B
        assertEquals(3, d.size(), "Size should be 3 after 3 inserts");
        assertEquals("C", d.peekFront(), "Front element should be C");
        assertEquals("B", d.peekRear(), "Rear element should be B");
    }

    private static void testDequeRemove() {
        Deque<Integer> d = new Deque<>();
        d.insertRear(10);
        d.insertRear(20);
        d.insertFront(5);

        // Deque: 5 -> 10 -> 20
        assertEquals(5, d.removeFront(), "Removed front should be 5");
        assertEquals(20, d.removeRear(), "Removed rear should be 20");
        assertEquals(10, d.peekFront(), "Front should now be 10");
        assertEquals(1, d.size(), "Remaining size should be 1");
    }

    private static void testDequeExceptions() {
        Deque<Double> d = new Deque<>();
        boolean exceptionThrown = false;
        try {
            d.removeFront();
        } catch (NoSuchElementException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "Removing front from empty Deque should throw exception");

        exceptionThrown = false;
        try {
            d.peekRear();
        } catch (NoSuchElementException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "Peeking rear of empty Deque should throw exception");
    }

    private static void testDequeClear() {
        Deque<Integer> d = new Deque<>();
        d.insertFront(1);
        d.insertRear(2);
        d.clear();
        assertTrue(d.isEmpty(), "Deque should be empty after clear");
        assertEquals(0, d.size(), "Size should be 0 after clear");
        
        // Ensure we can reuse it
        d.insertFront(5);
        assertEquals(5, d.peekFront(), "Should be able to insert after clear");
    }

    // --- Quicksort Tests ---

    private static void testSortEdgeCases() {
        Integer[] empty = new Integer[0];
        QuickSort.sort(empty); // Should not crash

        Integer[] single = new Integer[]{42};
        QuickSort.sort(single);
        assertEquals(42, single[0], "Single element array should remain unchanged");
    }

    private static void testSortRandom() {
        Integer[] arr = {15, 3, 9, 21, 5, 8, 12, 1, 6};
        QuickSort.sort(arr);
        for (int i = 0; i < arr.length - 1; i++) {
            assertTrue(arr[i] <= arr[i+1], "Array not sorted at index " + i);
        }
    }

    private static void testSortSortedReversed() {
        // Pre-sorted array
        Integer[] sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        QuickSort.sort(sorted);
        for (int i = 0; i < sorted.length - 1; i++) {
            assertTrue(sorted[i] <= sorted[i+1], "Pre-sorted check failed");
        }

        // Reversed array
        Integer[] reversed = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        QuickSort.sort(reversed);
        for (int i = 0; i < reversed.length - 1; i++) {
            assertTrue(reversed[i] <= reversed[i+1], "Reversed check failed");
        }
    }

    private static void testSortServiceRequests() {
        Location hostel = new Location("Limann Hall", Location.Type.HOSTEL);
        Location dept = new Location("JQB", Location.Type.LECTURE_HALL);

        // Sorting service requests by urgency descending, then by time ascending
        ServiceRequest r1 = new ServiceRequest(1, ServiceRequest.Type.SHUTTLE, hostel, dept, 5, 540, "Low priority morning");
        ServiceRequest r2 = new ServiceRequest(2, ServiceRequest.Type.SHUTTLE, hostel, dept, 10, 500, "High priority early");
        ServiceRequest r3 = new ServiceRequest(3, ServiceRequest.Type.MAINTENANCE, dept, null, 10, 480, "High priority earlier");
        ServiceRequest r4 = new ServiceRequest(4, ServiceRequest.Type.MAINTENANCE, hostel, null, 2, 600, "Lowest priority");

        ServiceRequest[] requests = {r1, r2, r3, r4};
        QuickSort.sort(requests);

        // Sorted order should be:
        // 1. r3 (Urgency 10, time 480)
        // 2. r2 (Urgency 10, time 500)
        // 3. r1 (Urgency 5, time 540)
        // 4. r4 (Urgency 2, time 600)
        assertEquals(3, requests[0].getId(), "First request should be request #3");
        assertEquals(2, requests[1].getId(), "Second request should be request #2");
        assertEquals(1, requests[2].getId(), "Third request should be request #1");
        assertEquals(4, requests[3].getId(), "Fourth request should be request #4");
    }
}
