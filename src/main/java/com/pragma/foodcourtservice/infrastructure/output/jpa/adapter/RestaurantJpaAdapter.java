package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.RestaurantEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id).orElse(null);
        return restaurantEntityMapper.toRestaurant(restaurant);
    }
}