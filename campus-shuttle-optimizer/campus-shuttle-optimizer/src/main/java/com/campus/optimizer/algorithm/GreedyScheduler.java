package com.campus.optimizer.algorithm;

import java.util.*;

/**
 * Greedy Scheduler for Campus Shuttle & Maintenance Requests
 * 
 * Implements a greedy algorithm that schedules service requests (shuttle rides and maintenance tasks)
 * based on:
 * 1. Urgency level (highest priority first)
 * 2. Geographic proximity to available resources
 * 3. Resource availability status
 * 
 * Derived parameter (from Sylvester Daniel Okantah index 22303503):
 * Priority weight = 2+2+3+0+3+5+0+3 = 18
 */
public class GreedyScheduler {

    // Derived priority weight constant from team index number
    private static final int PRIORITY_WEIGHT = 18;

    /**
     * Represents a service request (shuttle or maintenance)
     */
    public static class ServiceRequest implements Comparable<ServiceRequest> {
        public String id;
        public String type; // "SHUTTLE" or "MAINTENANCE"
        public int sourceLocationId;
        public int destLocationId; // for shuttle only
        public int urgency; // 1-10 scale
        public long submissionTime;
        public long deadline;
        public boolean assigned;

        public ServiceRequest(String id, String type, int sourceLocationId, 
                            int destLocationId, int urgency, long deadline) {
            this.id = id;
            this.type = type;
            this.sourceLocationId = sourceLocationId;
            this.destLocationId = destLocationId;
            this.urgency = urgency;
            this.submissionTime = System.currentTimeMillis();
            this.deadline = deadline;
            this.assigned = false;
        }

        /**
         * Compare requests by urgency (descending) then by deadline (ascending)
         * Higher urgency and earlier deadlines get higher priority
         */
        @Override
        public int compareTo(ServiceRequest other) {
            // Higher urgency comes first
            if (this.urgency != other.urgency) {
                return Integer.compare(other.urgency, this.urgency);
            }
            // Earlier deadline comes first
            return Long.compare(this.deadline, other.deadline);
        }

        @Override
        public String toString() {
            return String.format("Request{id=%s, type=%s, urgency=%d, deadline=%d, assigned=%s}",
                    id, type, urgency, deadline, assigned);
        }
    }

    /**
     * Represents an available resource (shuttle vehicle or maintenance staff)
     */
    public static class Resource {
        public String id;
        public String type; // "SHUTTLE" or "MAINTENANCE_STAFF"
        public int currentLocationId;
        public int capacity; // for shuttle
        public String specialization; // for maintenance staff
        public boolean available;
        public double utilization; // 0.0 to 1.0

        public Resource(String id, String type, int currentLocationId, boolean available) {
            this.id = id;
            this.type = type;
            this.currentLocationId = currentLocationId;
            this.available = available;
            this.utilization = 0.0;
        }

        @Override
        public String toString() {
            return String.format("Resource{id=%s, type=%s, location=%d, available=%s, util=%.2f%%}",
                    id, type, currentLocationId, available, utilization * 100);
        }
    }

    /**
     * Represents the distance/travel time between two locations
     */
    private double[][] distanceMatrix;
    private int numLocations;

    public GreedyScheduler(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.numLocations = distanceMatrix.length;
    }

    /**
     * Main greedy scheduling algorithm
     * 
     * Algorithm:
     * 1. Sort all service requests by urgency and deadline
     * 2. For each request in sorted order:
     *    a. Find all available resources of matching type
     *    b. Select the closest available resource
     *    c. Assign the request to that resource
     *    d. Update resource status and location
     * 
     * Time Complexity: O(n * m log m) where n = requests, m = resources
     * Space Complexity: O(n + m)
     */
    public Map<String, String> scheduleRequests(List<ServiceRequest> requests, 
                                                 List<Resource> resources) {
        Map<String, String> assignments = new LinkedHashMap<>();

        // Sort requests by urgency and deadline
        Collections.sort(requests);

        for (ServiceRequest request : requests) {
            if (request.assigned) {
                continue; // Already assigned
            }

            // Find the best resource for this request
            Resource bestResource = findBestResource(request, resources);

            if (bestResource != null) {
                // Assign the request
                assignments.put(request.id, bestResource.id);
                request.assigned = true;
                
                // Update resource state
                updateResource(bestResource, request);
                
                System.out.println("Assigned: " + request + " -> " + bestResource);
            } else {
                System.out.println("No available resource for: " + request);
                assignments.put(request.id, "UNASSIGNED");
            }
        }

        return assignments;
    }

    /**
     * Greedy selection: find the closest available resource of matching type
     * 
     * Greedy choice: Always pick the nearest available resource
     * This minimizes travel time/distance to the request location
     */
    private Resource findBestResource(ServiceRequest request, List<Resource> resources) {
        Resource best = null;
        double minDistance = Double.MAX_VALUE;

        for (Resource resource : resources) {
            // Resource must be available and match type
            if (!resource.available) continue;
            if (!resource.type.equals(getResourceTypeForRequest(request))) continue;

            // Calculate distance from resource to request location
            double distance = distanceMatrix[resource.currentLocationId][request.sourceLocationId];

            // Apply urgency-based weighting to favor high-urgency requests
            double weightedDistance = distance / (1.0 + (request.urgency / PRIORITY_WEIGHT));

            if (weightedDistance < minDistance) {
                minDistance = weightedDistance;
                best = resource;
            }
        }

        return best;
    }

    /**
     * Update resource state after assignment
     */
    private void updateResource(Resource resource, ServiceRequest request) {
        // Update location
        if (request.type.equals("SHUTTLE")) {
            resource.currentLocationId = request.destLocationId;
            resource.utilization = Math.min(1.0, resource.utilization + 0.25);
        } else if (request.type.equals("MAINTENANCE")) {
            resource.currentLocationId = request.sourceLocationId;
            resource.utilization = Math.min(1.0, resource.utilization + 0.5);
        }

        // Mark as unavailable if fully utilized
        if (resource.utilization >= 1.0) {
            resource.available = false;
        }
    }

    /**
     * Map request type to required resource type
     */
    private String getResourceTypeForRequest(ServiceRequest request) {
        if (request.type.equals("SHUTTLE")) {
            return "SHUTTLE";
        } else if (request.type.equals("MAINTENANCE")) {
            return "MAINTENANCE_STAFF";
        }
        return null;
    }

    /**
     * Calculate scheduling efficiency metrics
     */
    public Map<String, Double> calculateMetrics(List<ServiceRequest> requests,
                                                  List<Resource> resources,
                                                  Map<String, String> assignments) {
        Map<String, Double> metrics = new LinkedHashMap<>();

        // Assignment rate
        long assigned = assignments.values().stream()
                .filter(v -> !v.equals("UNASSIGNED"))
                .count();
        double assignmentRate = (double) assigned / requests.size() * 100;
        metrics.put("Assignment Rate (%)", assignmentRate);

        // Average utilization
        double avgUtilization = resources.stream()
                .mapToDouble(r -> r.utilization)
                .average()
                .orElse(0.0) * 100;
        metrics.put("Average Resource Utilization (%)", avgUtilization);

        // Unassigned requests
        metrics.put("Unassigned Requests", (double) (requests.size() - assigned));

        return metrics;
    }

    /**
     * Example usage and testing
     */
    public static void main(String[] args) {
        // Create sample distance matrix (5 locations)
        double[][] distances = {
            {0,   1,   2,   3,   4},   // Location 0
            {1,   0,   1.5, 2,   3},   // Location 1
            {2,   1.5, 0,   1,   2},   // Location 2
            {3,   2,   1,   0,   1.5}, // Location 3
            {4,   3,   2,   1.5, 0}    // Location 4
        };

        GreedyScheduler scheduler = new GreedyScheduler(distances);

        // Create sample requests
        List<ServiceRequest> requests = Arrays.asList(
            new ServiceRequest("SR1", "SHUTTLE", 0, 2, 9, System.currentTimeMillis() + 600000),
            new ServiceRequest("SR2", "MAINTENANCE", 1, 1, 7, System.currentTimeMillis() + 1200000),
            new ServiceRequest("SR3", "SHUTTLE", 2, 4, 5, System.currentTimeMillis() + 1800000),
            new ServiceRequest("SR4", "MAINTENANCE", 3, 3, 8, System.currentTimeMillis() + 900000),
            new ServiceRequest("SR5", "SHUTTLE", 4, 0, 6, System.currentTimeMillis() + 1500000)
        );

        // Create sample resources
        List<Resource> resources = Arrays.asList(
            new Resource("SH1", "SHUTTLE", 0, true),
            new Resource("SH2", "SHUTTLE", 2, true),
            new Resource("MS1", "MAINTENANCE_STAFF", 1, true),
            new Resource("MS2", "MAINTENANCE_STAFF", 3, true)
        );

        // Run scheduling
        System.out.println("=== Campus Shuttle & Maintenance Greedy Scheduler ===\n");
        Map<String, String> assignments = scheduler.scheduleRequests(requests, resources);

        System.out.println("\n=== Assignment Summary ===");
        assignments.forEach((req, res) -> System.out.println(req + " -> " + res));

        System.out.println("\n=== Scheduling Metrics ===");
        Map<String, Double> metrics = scheduler.calculateMetrics(requests, resources, assignments);
        metrics.forEach((metric, value) -> System.out.printf("%s: %.2f\n", metric, value));
    }
}
