package com.campus.optimizer.data;

public class Road {
    private final int fromLocationId;
    private final int toLocationId;
    private final double distance;

    public Road(int fromLocationId, int toLocationId, double distance) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public double getDistance() {
        return distance;
    }
}