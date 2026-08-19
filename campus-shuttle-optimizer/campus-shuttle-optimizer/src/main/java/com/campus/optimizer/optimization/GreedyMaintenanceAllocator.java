package com.campus.optimizer.optimization;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyMaintenanceAllocator {

    public OptimizationResult allocate(List<ServiceRequest> requests, int capacity) {
        if (requests == null || requests.isEmpty() || capacity <= 0) {
            return OptimizationResult.empty();
        }

        List<ServiceRequest> maintenanceRequests = new ArrayList<>();
        for (ServiceRequest request : requests) {
            if (request != null && "MAINTENANCE".equalsIgnoreCase(request.getType())
                    && request.getDuration() > 0) {
                maintenanceRequests.add(request);
            }
        }

        maintenanceRequests.sort(Comparator
                .comparingInt(ServiceRequest::getPriority).reversed()
                .thenComparing(ServiceRequest::getId));

        List<String> selectedIds = new ArrayList<>();
        int remainingCapacity = capacity;
        int totalDuration = 0;
        int totalBenefit = 0;

        for (ServiceRequest request : maintenanceRequests) {
            if (request.getDuration() <= remainingCapacity) {
                selectedIds.add(request.getId());
                remainingCapacity -= request.getDuration();
                totalDuration += request.getDuration();
                totalBenefit += request.getBenefit();
            }
        }

        return new OptimizationResult(selectedIds, totalBenefit, totalDuration);
    }
}