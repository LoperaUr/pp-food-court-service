package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;

import java.util.Collection;
import java.util.List;

public interface IDishServicePort {
    void createDish(Dish dish);

    void updateDish(Dish dish);

    void updateDishStatus(Long dishId, boolean active);

    PageModel<Dish> getDishesByRestaurant(Long id, Long categoryId, int page, int size);

    Dish getDishById(Long id);

    List<Dish> getDishesByIds(Collection<Long> ids);
}
