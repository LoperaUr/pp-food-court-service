package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.RestaurantDTO;
import com.pragma.foodcourtservice.application.mapper.IRestaurantMapper;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantMapper restaurantMapper;

    @Override
    public void createRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurantServicePort.createRestaurant(restaurant);
    }

    @Override
    public PageResponseDTO<RestaurantDTO> getRestaurants(int page, int size) {
        PageModel<Restaurant> restaurantPage = restaurantServicePort.getRestaurants(page, size);
        return restaurantMapper.toPageResponseDTO(restaurantPage);
    }
}

