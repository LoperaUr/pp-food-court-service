package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Dish;

public interface IDishServicePort {
    void createDish(Dish dish);

    void updateDish(Dish dish);

     void updateDishStatus(Long dishId, boolean active);
}
