package com.campus.optimizer.optimization;

import com.campus.optimizer.structures.DynamicArray;

import java.util.List;

public class CampusServiceOptimizer {

    public OptimizationResult optimize(List<ServiceRequest> requests, int capacity) {
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

    private void prepend(DynamicArray<String> values, String value) {
        values.add(value);
        for (int index = values.size() - 1; index > 0; index--) {
            values.set(index, values.get(index - 1));
        }
        values.set(0, value);
    }
}
