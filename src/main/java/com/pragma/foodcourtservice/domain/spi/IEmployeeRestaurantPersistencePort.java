package com.pragma.foodcourtservice.domain.spi;

public interface IEmployeeRestaurantPersistencePort {

    boolean isEmployeeFromRestaurant(Long employeeId, Long restaurantId);
}

