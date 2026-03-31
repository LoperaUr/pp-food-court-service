package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.RestaurantDTO;
import com.pragma.foodcourtservice.application.mapper.IRestaurantMapper;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantMapper restaurantDTOMapper;

    @Override
    public void createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantDTOMapper.toEntity(restaurantDTO);
        restaurantServicePort.createRestaurant(restaurant);
    }
}
