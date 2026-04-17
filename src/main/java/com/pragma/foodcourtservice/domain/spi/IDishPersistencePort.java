package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;

public interface IDishPersistencePort {
    void saveDish(Dish dish);

    Dish getDishById(Long id);

    PageModel<Dish> getDishesByRestaurant(Long id, int page, int size);

    PageModel<Dish> getDishesByRestaurantAndCategoryId(Long id, Long categoryId, int page, int size);
}
