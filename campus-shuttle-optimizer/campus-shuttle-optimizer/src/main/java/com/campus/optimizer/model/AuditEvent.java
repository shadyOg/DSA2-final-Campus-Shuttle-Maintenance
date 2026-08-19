package com.campus.optimizer.model;

public class AuditEvent {
    private int eventId;
    private String action;
    private String details;
    private String timestamp;

    public AuditEvent() {
    }

    public AuditEvent(int eventId, String action, String details, String timestamp) {
        this.eventId = eventId;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "eventId=" + eventId +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
