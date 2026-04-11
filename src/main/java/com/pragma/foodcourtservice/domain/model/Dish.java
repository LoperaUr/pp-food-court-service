package com.pragma.foodcourtservice.domain.model;


import lombok.Data;

@Data
public class Dish {
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Long restaurantId;
    private String urlImage;
    private boolean active;
    private Long price;
}
