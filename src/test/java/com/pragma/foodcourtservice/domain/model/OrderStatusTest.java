package com.pragma.foodcourtservice.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStatusTest {

    @Test
    void shouldIdentifyPendingStatus() {
        assertTrue(OrderStatus.PENDING.isPending());
        assertFalse(OrderStatus.READY.isPending());
    }

    @Test
    void shouldIdentifyInPreparationStatus() {
        assertTrue(OrderStatus.IN_PREPARATION.isInPreparation());
        assertFalse(OrderStatus.CANCELLED.isInPreparation());
    }

    @Test
    void shouldIdentifyReadyStatus() {
        assertTrue(OrderStatus.READY.isReady());
        assertFalse(OrderStatus.DELIVERED.isReady());
    }

    @Test
    void shouldIdentifyActiveStatuses() {
        assertTrue(OrderStatus.PENDING.isActive());
        assertTrue(OrderStatus.IN_PREPARATION.isActive());
        assertTrue(OrderStatus.READY.isActive());
        assertFalse(OrderStatus.DELIVERED.isActive());
        assertFalse(OrderStatus.CANCELLED.isActive());
    }
}

