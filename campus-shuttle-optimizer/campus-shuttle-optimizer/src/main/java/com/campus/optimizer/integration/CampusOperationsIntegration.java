package com.campus.optimizer.integration;

import com.campus.optimizer.optimization.CampusServiceOptimizer;
import com.campus.optimizer.optimization.GreedyMaintenanceAllocator;
import com.campus.optimizer.optimization.OptimizationResult;
import com.campus.optimizer.optimization.ServiceRequest;

import java.util.Arrays;
import java.util.List;

public class CampusOperationsIntegration {
    public static void main(String[] args) {
        List<ServiceRequest> requests = Arrays.asList(
                new ServiceRequest("SH-101", "SHUTTLE", "Hostel A", 9, 3, 18),
                new ServiceRequest("MN-204", "MAINTENANCE", "Electrical Lab", 8, 4, 24),
                new ServiceRequest("SH-220", "SHUTTLE", "Lecture Hall 5", 7, 2, 12),
                new ServiceRequest("MN-315", "MAINTENANCE", "Water Plant", 10, 5, 30));

        CampusServiceOptimizer optimizer = new CampusServiceOptimizer();
        OptimizationResult result = optimizer.optimize(requests, 7);

        System.out.println("Campus optimization summary");
        System.out.println("Selected IDs: " + result.getSelectedRequestIds());
        System.out.println("Total benefit: " + result.getTotalBenefit());
        System.out.println("Total duration: " + result.getTotalDuration());

        GreedyMaintenanceAllocator allocator = new GreedyMaintenanceAllocator();
        OptimizationResult maintenanceResult = allocator.allocate(requests, 7);

        System.out.println("Greedy maintenance allocation");
        System.out.println("Selected IDs: " + maintenanceResult.getSelectedRequestIds());
        System.out.println("Total benefit: " + maintenanceResult.getTotalBenefit());
        System.out.println("Total duration: " + maintenanceResult.getTotalDuration());
    }
}
