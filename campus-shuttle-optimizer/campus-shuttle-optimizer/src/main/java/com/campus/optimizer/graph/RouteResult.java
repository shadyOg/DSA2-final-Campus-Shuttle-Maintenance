package com.campus.optimizer.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RouteResult {
    private final String source;
    private final String destination;
    private final List<String> path;
    private final double distance;

    public RouteResult(String source, String destination, List<String> path, double distance) {
        this.source = source;
        this.destination = destination;
        this.path = Collections.unmodifiableList(new ArrayList<>(path));
        this.distance = distance;
    }

    public static RouteResult unreachable(String source, String destination) {
        return new RouteResult(source, destination, Collections.emptyList(), Double.POSITIVE_INFINITY);
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isReachable() {
        return !path.isEmpty();
    }
}