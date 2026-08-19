package com.campus.optimizer.graph;

public final class MstEdge {
    private final String source;
    private final String destination;
    private final double weight;

    public MstEdge(String source, String destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " - " + destination + " (" + weight + ")";
    }
}
