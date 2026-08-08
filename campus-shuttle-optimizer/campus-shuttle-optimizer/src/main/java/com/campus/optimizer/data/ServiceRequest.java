package com.campus.optimizer.data;

public class ServiceRequest {
    private final int id;
    private final int locationId;
    private final String type;
    private final String description;

    public ServiceRequest(int id, int locationId, String type, String description) {
        this.id = id;
        this.locationId = locationId;
        this.type = type;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}