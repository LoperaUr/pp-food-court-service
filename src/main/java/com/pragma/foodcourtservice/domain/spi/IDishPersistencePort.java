package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;

import java.util.Collection;
import java.util.List;

public interface IDishPersistencePort {
    void saveDish(Dish dish);

    Dish getDishById(Long id);

    List<Dish> getDishesByIds(Collection<Long> ids);

    PageModel<Dish> getDishesByRestaurant(Long id, int page, int size);

    PageModel<Dish> getDishesByRestaurantAndCategoryId(Long id, Long categoryId, int page, int size);
}
