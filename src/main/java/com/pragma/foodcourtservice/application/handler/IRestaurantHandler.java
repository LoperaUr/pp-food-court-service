package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.RestaurantDTO;

public interface IRestaurantHandler {

    void createRestaurant(RestaurantDTO restaurantDTO);

    void assignEmployeeToOwnerRestaurant(Long ownerId, Long employeeId);

    PageResponseDTO<RestaurantDTO> getRestaurants(int page, int size);
}
