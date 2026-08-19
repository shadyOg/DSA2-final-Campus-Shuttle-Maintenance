package com.campus.optimizer.model;

public class Resource {
    private int resourceId;
    private String type;
    private String homeLocation;
    private int capacity;
    private String availabilityStatus;

    public Resource() {
    }

    public Resource(int resourceId, String type, String homeLocation, int capacity, String availabilityStatus) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocation = homeLocation;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(String homeLocation) {
        this.homeLocation = homeLocation;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", type='" + type + '\'' +
                ", homeLocation='" + homeLocation + '\'' +
                ", capacity=" + capacity +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                '}';
    }
}
