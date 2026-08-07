package com.campus.optimizer.model;

public class ServiceRequest {
    private int requestId;
    private String source;
    private String destination;
    private String category;
    private int urgency;
    private String timeSubmitted;
    private String deadline;
    private String status;

    public ServiceRequest() {
    }

    public ServiceRequest(int requestId, String source, String destination, String category, int urgency, String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public void setTimeSubmitted(String timeSubmitted) {
        this.timeSubmitted = timeSubmitted;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId=" + requestId +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", category='" + category + '\'' +
                ", urgency=" + urgency +
                ", timeSubmitted='" + timeSubmitted + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
