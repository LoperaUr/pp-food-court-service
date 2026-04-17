package com.pragma.foodcourtservice.domain.model;

import lombok.Data;

@Data
public class OrderDish {
    private Long id;
    private Long orderId;
    private Long dishId;
    private Integer quantity;
}
