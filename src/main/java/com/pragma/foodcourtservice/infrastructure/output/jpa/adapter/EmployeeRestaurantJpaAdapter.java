package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IEmployeeRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final IEmployeeRestaurantRepository employeeRestaurantRepository;

    @Override
    public boolean isEmployeeFromRestaurant(Long employeeId, Long restaurantId) {
        return employeeRestaurantRepository.existsByEmployeeIdAndRestaurantId(employeeId, restaurantId);
    }
}

