package com.campus.optimizer.model;

/**
 * Represents a service request in the Campus Operations Optimizer system.
 * Implements Comparable to enable sorting using QuickSort.
 * 
 * Integration parameters derived from Member Index Number 22046391 (Didemudo Peter-Paul):
 * - Default Priority Weight: Digit sum of 22046391 is 27.
 */
public class ServiceRequest implements Comparable<ServiceRequest> {
    public enum Type {
        SHUTTLE,
        MAINTENANCE
    }

    private final int id;
    private final Type type;
    private final Location location; // Source for shuttle, location for maintenance
    private final Location destination; // Only for shuttle, null for maintenance
    private final int urgency; // Priority score (higher value means higher urgency)
    private final int timeSubmitted; // Minutes from start of operational day (e.g., 480 for 8:00 AM)
    private final String description;

    public ServiceRequest(int id, Type type, Location location, Location destination, int urgency, int timeSubmitted, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.destination = destination;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public Location getDestination() {
        return destination;
    }

    public int getUrgency() {
        return urgency;
    }

    public int getTimeSubmitted() {
        return timeSubmitted;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Compares service requests.
     * Order of sorting:
     * 1. Urgency descending (highest priority first).
     * 2. Time submitted ascending (FIFO if urgency is equal).
     */
    @Override
    public int compareTo(ServiceRequest other) {
        if (this.urgency != other.urgency) {
            return Integer.compare(other.urgency, this.urgency); // Descending order
        }
        return Integer.compare(this.timeSubmitted, other.timeSubmitted); // Ascending order
    }

    @Override
    public String toString() {
        String details = type == Type.SHUTTLE 
            ? "From " + location.getName() + " to " + destination.getName()
            : "At " + location.getName();
        return String.format("Request #%d [%s] - Urgency: %d, Time: %02d:%02d, Details: %s (%s)", 
            id, type, urgency, timeSubmitted / 60, timeSubmitted % 60, description, details);
    }
}
