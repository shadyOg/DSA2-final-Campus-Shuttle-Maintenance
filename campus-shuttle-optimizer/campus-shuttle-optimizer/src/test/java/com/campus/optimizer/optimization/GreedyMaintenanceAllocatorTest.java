package com.campus.optimizer.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GreedyMaintenanceAllocatorTest {

    @Test
    public void allocatePrioritizesMaintenanceRequestsWithinCapacity() {
        List<ServiceRequest> requests = Arrays.asList(
                new ServiceRequest("M1", "MAINTENANCE", "Boiler Room", 8, 3, 20),
                new ServiceRequest("S1", "SHUTTLE", "Hostel A", 10, 1, 50),
                new ServiceRequest("M2", "MAINTENANCE", "Electrical Lab", 10, 2, 15),
                new ServiceRequest("M3", "MAINTENANCE", "Water Plant", 6, 2, 30));

        OptimizationResult result = new GreedyMaintenanceAllocator().allocate(requests, 4);

        assertEquals(Arrays.asList("M2", "M3"), result.getSelectedRequestIds());
        assertEquals(45, result.getTotalBenefit());
        assertEquals(4, result.getTotalDuration());
    }

    @Test
    public void allocateReturnsEmptySelectionWhenThereIsNoTechnicianCapacity() {
        List<ServiceRequest> requests = Arrays.asList(
                new ServiceRequest("M1", "MAINTENANCE", "Boiler Room", 10, 1, 12));

        OptimizationResult result = new GreedyMaintenanceAllocator().allocate(requests, 0);

        assertTrue(result.getSelectedRequestIds().isEmpty());
        assertEquals(0, result.getTotalBenefit());
        assertEquals(0, result.getTotalDuration());
    }
}