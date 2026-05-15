package com.pragma.foodcourtservice.infrastructure.configuration;

import com.pragma.foodcourtservice.domain.api.*;
import com.pragma.foodcourtservice.domain.spi.ICategoryPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.usecase.DishService;
import com.pragma.foodcourtservice.domain.usecase.OrderService;
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
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final INotificationServicePort notificationServicePort;
    private final ITraceabilityServicePort traceabilityServicePort;

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantService(restaurantPersistencePort, userServicePort, authenticationContextPort);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishService(dishPersistencePort, restaurantServicePort(), authenticationContextPort, categoryPersistencePort);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderService(orderPersistencePort, restaurantServicePort(), dishServicePort(), employeeRestaurantPersistencePort, userServicePort, notificationServicePort, traceabilityServicePort);
    }
}
