package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class UserService implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserServicePort userServicePort;

    @Override
    public void createRestaurant(Restaurant restaurant) {
        validateOwner(restaurant);
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    private void validateOwner(Restaurant restaurant) {
        Long ownerId = restaurant.getOwnerId();
        if (ownerId == null) {
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        if (!userServicePort.userExistsWithOwnerRole(ownerId)) {
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
