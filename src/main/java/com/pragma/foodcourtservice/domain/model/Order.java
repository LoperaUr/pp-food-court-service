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
}
