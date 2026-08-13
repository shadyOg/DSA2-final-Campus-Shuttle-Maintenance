package com.campus.optimizer.optimisation;

public class ServiceRequest {
    private final String id;
    private final String type;
    private final String location;
    private final int priority;
    private final int duration;
    private final int benefit;

    public ServiceRequest(String id, String type, String location, int priority, int duration, int benefit) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.priority = priority;
        this.duration = duration;
        this.benefit = benefit;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getPriority() {
        return priority;
    }

    public int getDuration() {
        return duration;
    }

    public int getBenefit() {
        return benefit;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", priority=" + priority +
                ", duration=" + duration +
                ", benefit=" + benefit +
                '}';
    }
}
