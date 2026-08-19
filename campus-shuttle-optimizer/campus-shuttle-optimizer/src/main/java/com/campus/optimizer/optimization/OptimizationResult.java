package com.campus.optimizer.optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizationResult {
    private final List<String> selectedRequestIds;
    private final int totalBenefit;
    private final int totalDuration;

    public OptimizationResult(List<String> selectedRequestIds, int totalBenefit, int totalDuration) {
        this.selectedRequestIds = new ArrayList<>(
                selectedRequestIds == null ? Collections.emptyList() : selectedRequestIds);
        this.totalBenefit = totalBenefit;
        this.totalDuration = totalDuration;
    }

    public List<String> getSelectedRequestIds() {
        return Collections.unmodifiableList(selectedRequestIds);
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
