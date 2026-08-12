package com.campus.optimizer;

import com.campus.optimizer.linear.Deque;
import com.campus.optimizer.model.Location;
import com.campus.optimizer.model.ServiceRequest;
import com.campus.optimizer.sort.QuickSort;

/**
 * Main demonstration driver for Didemudo Peter-Paul's DSA Semester Project assignment.
 * Shows Deque operations, Quicksort priority sorting, and runs performance benchmarks.
 */
public class Main {
    public static void main(String[] args) {
        // Didemudo Peter-Paul's Index Number: 22046391
        // Digit Sum: 2 + 2 + 0 + 4 + 6 + 3 + 9 + 1 = 27 (Default Urgency Base)
        // Last Two Digits: 91 (Traffic Route Penalty)
        int defaultUrgencyBase = 27;
        int routePenalty = 91;

        System.out.println("==========================================================================");
        System.out.println("   CAMPUS SHUTTLE & MAINTENANCE OPERATIONS OPTIMIZER (FOUNDATIONS SKELETON)");
        System.out.println("   Squad: Linear Structures (Deque) & Search/Sort (Quicksort)");
        System.out.println("   Developer: Didemudo Peter-Paul (Index No: 22046391)");
        System.out.printf("   Derived Parameters: Urgency Base = %d, Route Penalty = %d\n", defaultUrgencyBase, routePenalty);
        System.out.println("==========================================================================\n");

        // 1. Setup Mock Campus Locations
        Location limannHostel = new Location("Limann Hostel", Location.Type.HOSTEL);
        Location sarbahHostel = new Location("Sarbah Hostel", Location.Type.HOSTEL);
        Location jqbLectureHall = new Location("JQB Lecture Hall", Location.Type.LECTURE_HALL);
        Location csLab = new Location("CS Laboratory", Location.Type.LABORATORY);
        Location unionStop = new Location("Union Shuttle Stop", Location.Type.SHUTTLE_STOP);
        Location maintenanceDepot = new Location("Maintenance Depot", Location.Type.DEPOT);

        // 2. Demonstrate Deque (Double-Ended Queue)
        System.out.println("--- 1. DEMONSTRATING CUSTOM DEQUE OPERATIONS ---");
        System.out.println("Scenario: Service requests arrive. Urgent requests go to the front; standard requests go to the rear.");
        Deque<ServiceRequest> requestQueue = new Deque<>();

        // Standard Shuttle Request (rear)
        requestQueue.insertRear(new ServiceRequest(101, ServiceRequest.Type.SHUTTLE, limannHostel, jqbLectureHall, 4, 480, "Student heading to class"));
        // Standard Maintenance Request (rear)
        requestQueue.insertRear(new ServiceRequest(102, ServiceRequest.Type.MAINTENANCE, csLab, null, 3, 500, "Leaking pipe in lab"));
        // HIGH URGENCY Shuttle Request (front)
        requestQueue.insertFront(new ServiceRequest(103, ServiceRequest.Type.SHUTTLE, sarbahHostel, csLab, 9, 510, "Student late for exams"));
        // HIGH URGENCY Maintenance Request (front)
        requestQueue.insertFront(new ServiceRequest(104, ServiceRequest.Type.MAINTENANCE, jqbLectureHall, null, 10, 520, "Power outage in JQB exam hall"));

        System.out.printf("Queue Size: %d\n", requestQueue.size());
        System.out.printf("First Request in Queue (Front): %s\n", requestQueue.peekFront());
        System.out.printf("Last Request in Queue (Rear): %s\n\n", requestQueue.peekRear());

        System.out.println("Dispatching requests from the queue in order (Front to Rear):");
        while (!requestQueue.isEmpty()) {
            ServiceRequest request = requestQueue.removeFront();
            System.out.printf("  [DISPATCHED] %s\n", request);
        }
        System.out.println();

        // 3. Demonstrate Quicksort sorting a batch of requests
        System.out.println("--- 2. DEMONSTRATING QUICKSORT PRIORITY ROUTING ---");
        System.out.println("Scenario: A batch of unsorted requests is collected. We sort them by priority (Urgency Descending, then Submission Time Ascending).");

        ServiceRequest[] batch = {
            new ServiceRequest(1, ServiceRequest.Type.SHUTTLE, limannHostel, unionStop, 5, 540, "Standard shuttle ride"),
            new ServiceRequest(2, ServiceRequest.Type.MAINTENANCE, csLab, null, 10, 600, "Server overheating"),
            new ServiceRequest(3, ServiceRequest.Type.MAINTENANCE, maintenanceDepot, null, 10, 570, "Backup generator failure"),
            new ServiceRequest(4, ServiceRequest.Type.SHUTTLE, sarbahHostel, jqbLectureHall, 2, 620, "Student visiting friend"),
            new ServiceRequest(5, ServiceRequest.Type.SHUTTLE, unionStop, jqbLectureHall, 8, 590, "Professor heading to lecture")
        };

        System.out.println("Unsorted Batch:");
        for (ServiceRequest req : batch) {
            System.out.println("  " + req);
        }

        // Run Quicksort
        QuickSort.sort(batch);

        System.out.println("\nSorted Batch (Optimal Dispatch Order):");
        for (ServiceRequest req : batch) {
            System.out.println("  " + req);
        }
        System.out.println();

        // 4. Benchmarking Quicksort Performance
        System.out.println("--- 3. QUICKSORT PERFORMANCE BENCHMARKING ---");
        System.out.println("Sorting random integer datasets of varying sizes:");
        int[] sizes = {100, 1000, 10000, 50000};
        
        System.out.printf("%-12s | %-20s | %-20s\n", "Dataset Size", "Time (Nanoseconds)", "Time (Milliseconds)");
        System.out.println("------------------------------------------------------------------");
        for (int size : sizes) {
            long ns = QuickSort.benchmark(size);
            double ms = ns / 1_000_000.0;
            System.out.printf("%-12d | %-20d | %-20.4f\n", size, ns, ms);
        }
        System.out.println("------------------------------------------------------------------");
        System.out.println("\nExecution completed successfully.");
    }
}
