package com.urbanthreads.orderservice.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    PAID,
    CONFIRMED,
    PACKED,
    SHIPPED,
    DELIVERED,
    RETURNED,
    REFUNDED,
    CANCELLED,
    FAILED,
    HOLD
}