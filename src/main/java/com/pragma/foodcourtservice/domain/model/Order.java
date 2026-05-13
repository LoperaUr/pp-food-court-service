package com.pragma.foodcourtservice.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Long id;
    private Long restaurantId;
    private Long clientId;
    private Long chefId;
    private LocalDateTime date;
    private OrderStatus status;
    private List<OrderDish> dishes;
    private String securityPin;

    public void createForClient(Long clientId, LocalDateTime date) {
        this.clientId = clientId;
        this.date = date;
        this.status = OrderStatus.PENDING;
    }

    public void assignChef(Long employeeId) {
        this.chefId = employeeId;
        this.status = OrderStatus.IN_PREPARATION;
    }

    public void markAsReady(String securityPin) {
        this.securityPin = securityPin;
        this.status = OrderStatus.READY;
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
