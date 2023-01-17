package com.sparta.ojinger.entity;

public enum ElevationStatus {
    APPROVED(Status.APPROVED),
    REJECTED(Status.REJECTED),
    PENDING(Status.PENDING);

    private final String status;

    ElevationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
        public static final String PENDING = "PENDING";
    }
}
