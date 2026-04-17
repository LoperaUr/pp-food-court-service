package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.RestaurantEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
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

    @Override
    public Restaurant getRestaurantByOwnerId(Long ownerId) {
        RestaurantEntity restaurant = restaurantRepository.findByOwnerId(ownerId).orElse(null);
        return restaurantEntityMapper.toRestaurant(restaurant);
    }

    @Override
    public PageModel<Restaurant> getRestaurants(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<RestaurantEntity> pageResult = restaurantRepository.findAll(pageRequest);
        return restaurantEntityMapper.toRestaurantPageModel(pageResult);
    }
}