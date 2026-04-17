package com.pragma.foodcourtservice.application.dto;

import lombok.Data;

@Data
public class OrderDishDTO {
    private Long id;
    private Long orderId;
    private Long dishId;
    private Integer quantity;
}
