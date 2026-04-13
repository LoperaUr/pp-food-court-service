package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import jakarta.validation.Valid;

public interface IDishHandler {
    void createDish(DishDTO dishDTO);

    void updateDish(Long id, @Valid DishDTO dishDTO);
}
