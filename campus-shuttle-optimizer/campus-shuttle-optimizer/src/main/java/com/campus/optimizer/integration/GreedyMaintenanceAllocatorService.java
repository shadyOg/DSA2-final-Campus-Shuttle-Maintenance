package com.campus.optimizer.integration;

import com.campus.optimizer.db.ServiceRequestDAO;
import com.campus.optimizer.optimization.GreedyMaintenanceAllocator;
import com.campus.optimizer.optimization.OptimizationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that wires the GreedyMaintenanceAllocator to the database.
 * Converts database ServiceRequest objects to optimization ServiceRequest objects
 * and executes the greedy maintenance allocation algorithm.
 */
public class GreedyMaintenanceAllocatorService {
    private final ServiceRequestDAO dao;
    private final GreedyMaintenanceAllocator allocator;

    public GreedyMaintenanceAllocatorService() {
        this.dao = new ServiceRequestDAO();
        this.allocator = new GreedyMaintenanceAllocator();
    }

    /**
     * Allocates maintenance requests from the database based on technician capacity.
     * Fetches all pending maintenance requests and applies the greedy allocation algorithm.
     *
     * @param capacity the available technician capacity (in time units)
     * @return OptimizationResult containing selected maintenance request IDs, total benefit, and duration
     */
    public OptimizationResult allocateMaintenanceRequests(int capacity) {
        // Fetch all pending requests from the database
        List<DatabaseServiceRequest> dbRequests = dao.findByStatus("pending");
        
        // Convert database requests to optimization requests
        List<OptimizationServiceRequest> optimizationRequests = convertToOptimizationRequests(dbRequests);
        
        // Run the greedy allocation algorithm
        return allocator.allocate(optimizationRequests, capacity);
    }

    /**
     * Allocates maintenance requests by category from the database.
     * Filters requests to only include those matching the specified category.
     *
     * @param category the service category to filter by (e.g., "maintenance")
     * @param capacity the available technician capacity (in time units)
     * @return OptimizationResult containing selected maintenance request IDs, total benefit, and duration
     */
    public OptimizationResult allocateMaintenanceRequestsByCategory(String category, int capacity) {
        // Fetch all pending requests from the database
        List<DatabaseServiceRequest> dbRequests = dao.findByStatus("pending");
        
        // Filter by category
        List<DatabaseServiceRequest> filteredRequests = new ArrayList<>();
        for (DatabaseServiceRequest request : dbRequests) {
            if (request.getCategory() != null && request.getCategory().equalsIgnoreCase(category)) {
                filteredRequests.add(request);
            }
        }
        
        // Convert to optimization requests
        List<OptimizationServiceRequest> optimizationRequests = convertToOptimizationRequests(filteredRequests);
        
        // Run the greedy allocation algorithm
        return allocator.allocate(optimizationRequests, capacity);
    }

    /**
     * Allocates maintenance requests by urgency level from the database.
     * Filters requests to only include those matching the specified urgency.
     *
     * @param urgency the urgency level to filter by
     * @param capacity the available technician capacity (in time units)
     * @return OptimizationResult containing selected maintenance request IDs, total benefit, and duration
     */
    public OptimizationResult allocateMaintenanceRequestsByUrgency(int urgency, int capacity) {
        // Fetch requests by urgency from the database
        List<DatabaseServiceRequest> dbRequests = dao.findByUrgency(urgency);
        
        // Convert to optimization requests
        List<OptimizationServiceRequest> optimizationRequests = convertToOptimizationRequests(dbRequests);
        
        // Run the greedy allocation algorithm
        return allocator.allocate(optimizationRequests, capacity);
    }

    /**
     * Converts database ServiceRequest objects to optimization ServiceRequest objects.
     * Maps database fields to optimization fields:
     * - requestId -> id
     * - category -> type
     * - destination -> location
     * - urgency -> priority
     * - duration is estimated as (deadline - timeSubmitted), defaulting to 1 hour if not calculable
     * - benefit is calculated as urgency level * 10
     *
     * @param dbRequests list of database ServiceRequest objects
     * @return list of optimization ServiceRequest objects
     */
    private List<OptimizationServiceRequest> convertToOptimizationRequests(
            List<DatabaseServiceRequest> dbRequests) {
        List<OptimizationServiceRequest> optimizationRequests = new ArrayList<>();
        
        for (DatabaseServiceRequest dbRequest : dbRequests) {
            if (dbRequest == null) continue;
            
            String id = "REQ-" + dbRequest.getRequestId();
            String type = dbRequest.getCategory() != null ? dbRequest.getCategory() : "UNKNOWN";
            String location = dbRequest.getDestination() != null ? dbRequest.getDestination() : "Unknown";
            int priority = dbRequest.getUrgency();
            
            // Estimate duration (default 1 hour if not calculable)
            int duration = estimateDuration(dbRequest.getTimeSubmitted(), dbRequest.getDeadline());
            
            // Calculate benefit based on urgency (higher urgency = higher benefit)
            int benefit = dbRequest.getUrgency() * 10;
            
            OptimizationServiceRequest optRequest = new OptimizationServiceRequest(
                    id, type, location, priority, duration, benefit
            );
            optimizationRequests.add(optRequest);
        }
        
        return optimizationRequests;
    }

    /**
     * Estimates the duration of a request based on submitted and deadline times.
     * If times are not calculable, defaults to 1 hour.
     *
     * @param timeSubmitted the time the request was submitted (format: "YYYY-MM-DD HH:MM")
     * @param deadline the deadline for the request (format: "YYYY-MM-DD HH:MM")
     * @return estimated duration in hours
     */
    private int estimateDuration(String timeSubmitted, String deadline) {
        try {
            if (timeSubmitted == null || deadline == null || 
                timeSubmitted.trim().isEmpty() || deadline.trim().isEmpty()) {
                return 1; // Default 1 hour
            }
            
            // Simple heuristic: for now, return a reasonable default based on urgency context
            // This could be enhanced with actual time parsing if needed
            return 1; // Conservative default
        } catch (Exception e) {
            return 1; // Default 1 hour on any error
        }
    }
}
