package com.campus.optimizer.optimization;

import com.campus.optimizer.structures.DynamicArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizationResult {
    private final DynamicArray<String> selectedRequestIds;
    private final int totalBenefit;
    private final int totalDuration;

    public OptimizationResult(List<String> selectedRequestIds, int totalBenefit, int totalDuration) {
        this.selectedRequestIds = new DynamicArray<>();
        if (selectedRequestIds != null) {
            for (String requestId : selectedRequestIds) {
                this.selectedRequestIds.add(requestId);
            }
        }
        this.totalBenefit = totalBenefit;
        this.totalDuration = totalDuration;
    }

    public OptimizationResult(
            DynamicArray<String> selectedRequestIds, int totalBenefit, int totalDuration) {
        this.selectedRequestIds = selectedRequestIds == null ? new DynamicArray<>() : selectedRequestIds;
        this.totalBenefit = totalBenefit;
        this.totalDuration = totalDuration;
    }

    public List<String> getSelectedRequestIds() {
        List<String> result = new ArrayList<>();
        for (int index = 0; index < selectedRequestIds.size(); index++) {
            result.add(selectedRequestIds.get(index));
        }
        return Collections.unmodifiableList(result);
    }

    public int getTotalBenefit() {
        return totalBenefit;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public static OptimizationResult empty() {
        return new OptimizationResult(Collections.emptyList(), 0, 0);
    }

    @Override
    public String toString() {
        return "OptimizationResult{" +
                "selectedRequestIds=" + selectedRequestIds +
                ", totalBenefit=" + totalBenefit +
                ", totalDuration=" + totalDuration +
                '}';
    }
}
