package com.campus.optimizer.model;

/**
 * Represents a location node on campus.
 */
public class Location {
    public enum Type {
        HOSTEL,
        LECTURE_HALL,
        LABORATORY,
        SHUTTLE_STOP,
        DEPOT
    }

    private final String name;
    private final Type type;

    public Location(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
