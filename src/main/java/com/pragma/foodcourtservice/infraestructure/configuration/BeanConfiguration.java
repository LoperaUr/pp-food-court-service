package com.pragma.foodcourtservice.infraestructure.configuration;

import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IUserServicePort;
import com.pragma.foodcourtservice.domain.usecase.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class BeanConfiguration {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserServicePort userServicePort;

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new UserService(restaurantPersistencePort, userServicePort);
    }
}
