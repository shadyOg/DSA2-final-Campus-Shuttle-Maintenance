package com.campus.optimizer.optimization;

import com.campus.optimizer.db.ServiceRequestDAO;
import com.campus.optimizer.structures.DynamicArray;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CampusServiceOptimizer {
    private static final DateTimeFormatter REQUEST_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ServiceRequestDAO serviceRequestDAO;

    public CampusServiceOptimizer() {
        this(new ServiceRequestDAO());
    }

    public CampusServiceOptimizer(ServiceRequestDAO serviceRequestDAO) {
        if (serviceRequestDAO == null) {
            throw new IllegalArgumentException("ServiceRequestDAO cannot be null");
        }
        this.serviceRequestDAO = serviceRequestDAO;
    }

    /**
     * Optimizes all pending requests loaded from the SQLite service_requests table.
     * Capacity is expressed in working minutes.
     */
    public OptimizationResult optimizePendingRequests(int capacityMinutes) {
        return optimizeDatabaseRequests(serviceRequestDAO.findByStatus("pending"), capacityMinutes);
    }

    /**
     * Optimizes database requests after converting them to the DP model.
     * Capacity is expressed in working minutes.
     */
    public OptimizationResult optimizeDatabaseRequests(
            List<com.campus.optimizer.model.ServiceRequest> requests, int capacityMinutes) {
        if (requests == null || requests.isEmpty() || capacityMinutes <= 0) {
            return OptimizationResult.empty();
        }

        List<com.campus.optimizer.optimization.ServiceRequest> optimizationRequests = new ArrayList<>();
        for (com.campus.optimizer.model.ServiceRequest request : requests) {
            if (request == null) {
                continue;
            }
            optimizationRequests.add(toOptimizationRequest(request));
        }
        return optimize(optimizationRequests, capacityMinutes);
    }

    public OptimizationResult optimize(
            List<com.campus.optimizer.optimization.ServiceRequest> requests, int capacity) {
        if (requests == null || requests.isEmpty() || capacity <= 0) {
            return OptimizationResult.empty();
        }

        int requestCount = requests.size();
        int[][] bestValue = new int[requestCount + 1][capacity + 1];
        boolean[][] taken = new boolean[requestCount + 1][capacity + 1];

        for (int index = 1; index <= requestCount; index++) {
            ServiceRequest request = requests.get(index - 1);
            int duration = Math.max(0, request.getDuration());

            for (int remainingCapacity = 0; remainingCapacity <= capacity; remainingCapacity++) {
                bestValue[index][remainingCapacity] = bestValue[index - 1][remainingCapacity];

                if (duration <= remainingCapacity) {
                    int candidateValue = bestValue[index - 1][remainingCapacity - duration]
                            + request.getBenefit();
                    if (candidateValue > bestValue[index][remainingCapacity]) {
                        bestValue[index][remainingCapacity] = candidateValue;
                        taken[index][remainingCapacity] = true;
                    }
                }
            }
        }

        int finalCapacity = capacity;
        DynamicArray<String> selectedIds = new DynamicArray<>();
        int totalDuration = 0;
        int totalBenefit = bestValue[requestCount][finalCapacity];

        for (int index = requestCount; index > 0; index--) {
            if (taken[index][finalCapacity]) {
                ServiceRequest request = requests.get(index - 1);
                prepend(selectedIds, request.getId());
                totalDuration += request.getDuration();
                finalCapacity -= request.getDuration();
            }
        }

        return new OptimizationResult(selectedIds, totalBenefit, totalDuration);
    }

        private com.campus.optimizer.optimization.ServiceRequest toOptimizationRequest(
            com.campus.optimizer.model.ServiceRequest request) {
        return new com.campus.optimizer.optimization.ServiceRequest(
                "REQ-" + request.getRequestId(),
                request.getCategory(),
                request.getDestination(),
                request.getUrgency(),
                estimateDurationMinutes(request.getTimeSubmitted(), request.getDeadline()),
                request.getUrgency() * 10);
    }

    private int estimateDurationMinutes(String timeSubmitted, String deadline) {
        if (timeSubmitted == null || deadline == null) {
            return 1;
        }
        try {
            LocalDateTime submitted = LocalDateTime.parse(timeSubmitted, REQUEST_TIME_FORMAT);
            LocalDateTime due = LocalDateTime.parse(deadline, REQUEST_TIME_FORMAT);
            long minutes = Duration.between(submitted, due).toMinutes();
            return (int) Math.max(1, Math.min(Integer.MAX_VALUE, minutes));
        } catch (DateTimeParseException | ArithmeticException exception) {
            return 1;
        }
    }

    private void prepend(DynamicArray<String> values, String value) {
        values.add(value);
        for (int index = values.size() - 1; index > 0; index--) {
            values.set(index, values.get(index - 1));
        }
        values.set(0, value);
    }
}
