package com.pragma.foodcourtservice.domain.model;

import lombok.Getter;

import java.util.List;

@Getter
public enum OrderStatus {
    PENDING("PENDING"),
    IN_PREPARATION("IN_PREPARATION"),
    READY("READY"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED");

    private final String code;

    OrderStatus(String code) {
        this.code = code;
    }

    public static List<OrderStatus> activeStatuses() {
        return List.of(PENDING, IN_PREPARATION, READY);
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isInPreparation() {
        return this == IN_PREPARATION;
    }

    public boolean isReady() {
        return this == READY;
    }

    public boolean isActive() {
        return activeStatuses().contains(this);
    }
}

