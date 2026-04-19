package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);

    void assignEmployeeToRestaurant(Long employeeId, Long restaurantId);

    Restaurant getRestaurantById(Long id);

    Restaurant getRestaurantByOwnerId(Long ownerId);

    PageModel<Restaurant> getRestaurants(int page, int size);
}
