package com.pragma.foodcourtservice.application.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long restaurantId;
    private LocalDateTime date;
    private String status;
    private List<OrderDishDTO> dishes;
}
