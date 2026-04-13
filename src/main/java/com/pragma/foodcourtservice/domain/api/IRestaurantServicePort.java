package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void createRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(Long id);
    Restaurant getRestaurantByOwnerId(Long ownerId);
}
