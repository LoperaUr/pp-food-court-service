package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.Dish;

public interface IDishPersistencePort {
    void createDish(Dish dish);
}
