package com.sparta.ojinger.entity;


public enum TradeStatus {
    ON_SALE(Status.ON_SALE),
    SOLD_OUT(Status.SOLD_OUT),
    SUSPENSION(Status.SUSPENSION);

    private final String status;

    TradeStatus(String status) {
        this.status = status;
    }

    public static class Status {
        public static final String ON_SALE = "ON SALE";
        public static final String SOLD_OUT = "SOLD OUT";
        public static final String SUSPENSION = "SUSPENSION";
    }
}
