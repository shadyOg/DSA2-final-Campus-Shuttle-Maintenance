package com.campus.optimizer.optimization;

import com.campus.optimizer.structures.DynamicArray;

import java.util.Comparator;
import java.util.List;

public class GreedyMaintenanceAllocator {

    public OptimizationResult allocate(List<ServiceRequest> requests, int capacity) {
        if (requests == null || requests.isEmpty() || capacity <= 0) {
            return OptimizationResult.empty();
        }

        DynamicArray<ServiceRequest> maintenanceRequests = new DynamicArray<>();
        for (ServiceRequest request : requests) {
            if (request != null && "MAINTENANCE".equalsIgnoreCase(request.getType())
                    && request.getDuration() > 0) {
                maintenanceRequests.add(request);
            }
        }

        Comparator<ServiceRequest> priorityOrder = Comparator
            .comparingInt(ServiceRequest::getPriority).reversed()
            .thenComparing(ServiceRequest::getId);
        sort(maintenanceRequests, priorityOrder);

        DynamicArray<String> selectedIds = new DynamicArray<>();
        int remainingCapacity = capacity;
        int totalDuration = 0;
        int totalBenefit = 0;

        for (int index = 0; index < maintenanceRequests.size(); index++) {
            ServiceRequest request = maintenanceRequests.get(index);
            if (request.getDuration() <= remainingCapacity) {
                selectedIds.add(request.getId());
                remainingCapacity -= request.getDuration();
                totalDuration += request.getDuration();
                totalBenefit += request.getBenefit();
            }
        }

        return new OptimizationResult(selectedIds, totalBenefit, totalDuration);
    }

    private void sort(DynamicArray<ServiceRequest> values, Comparator<ServiceRequest> comparator) {
        for (int index = 1; index < values.size(); index++) {
            ServiceRequest current = values.get(index);
            int position = index - 1;
            while (position >= 0 && comparator.compare(values.get(position), current) > 0) {
                values.set(position + 1, values.get(position));
                position--;
            }
            values.set(position + 1, current);
        }
    }
}