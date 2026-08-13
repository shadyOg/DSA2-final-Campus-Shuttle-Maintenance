package com.campus.optimizer.optimisation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CampusServiceOptimizerTest {

    @Test
    public void optimizeChoosesMostValuableCampusTaskSetWithinCapacity() {
        List<ServiceRequest> requests = Arrays.asList(
                new ServiceRequest("S1", "SHUTTLE", "Hostel Block A", 10, 2, 15),
                new ServiceRequest("S2", "MAINTENANCE", "Electrical Workshop", 9, 3, 20),
                new ServiceRequest("S3", "SHUTTLE", "Lecture Hall 3", 8, 4, 25),
                new ServiceRequest("S4", "MAINTENANCE", "STEM Lab", 7, 2, 18));

        CampusServiceOptimizer optimizer = new CampusServiceOptimizer();
        OptimizationResult result = optimizer.optimize(requests, 5);

        assertEquals(38, result.getTotalBenefit());
        assertTrue(result.getSelectedRequestIds().contains("S2"));
        assertTrue(result.getSelectedRequestIds().contains("S4"));
        assertEquals(5, result.getTotalDuration());
    }

    @Test
    public void optimizeReturnsEmptySelectionWhenCapacityIsZero() {
        List<ServiceRequest> requests = Arrays.asList(
                new ServiceRequest("M1", "MAINTENANCE", "Boiler Room", 6, 1, 12));

        CampusServiceOptimizer optimizer = new CampusServiceOptimizer();
        OptimizationResult result = optimizer.optimize(requests, 0);

        assertEquals(0, result.getTotalBenefit());
        assertEquals(0, result.getSelectedRequestIds().size());
        assertEquals(0, result.getTotalDuration());
    }
}
