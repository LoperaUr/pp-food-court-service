package com.pragma.foodcourtservice.infrastructure.configuration;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.domain.usecase.DishService;
import com.pragma.foodcourtservice.domain.usecase.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class BeanConfiguration {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserServicePort userServicePort;
    private final IAuthenticationServicePort authenticationContextPort;

    private final IDishPersistencePort dishPersistencePort;

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantService(restaurantPersistencePort, userServicePort, authenticationContextPort);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishService(dishPersistencePort ,restaurantServicePort(),authenticationContextPort);
    }
}
