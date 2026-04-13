package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.Role;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class RestaurantService implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserServicePort userServicePort;
    private final IAuthenticationServicePort authenticationContextPort;

    @Override
    public void createRestaurant(Restaurant restaurant) {
        validateAdmin();
        validateOwner(restaurant);
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(id);
        if (restaurant == null) {
            throw new DomainException(DomainConstants.MSG_RESTAURANT_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
        return restaurant;
    }

    @Override
    public Restaurant getRestaurantByOwnerId(Long ownerId) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantByOwnerId(ownerId);
        if (restaurant == null) {
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_HAVE_RESTAURANT, HttpStatus.FORBIDDEN);
        }
        return restaurant;
    }

    private void validateAdmin() {
        User currentUser = authenticationContextPort.getAuthenticatedUser();

        if (currentUser == null || currentUser.hasNoRole()) {
            throw new DomainException(DomainConstants.MSG_NOT_ELEVATED_ROLE, HttpStatus.FORBIDDEN);
        }

        if (currentUser.notSameRole(Role.ADMIN)) {
            throw new DomainException(DomainConstants.MSG_ROLE_NOT_AUTHORIZED, HttpStatus.FORBIDDEN);
        }
    }

    private void validateOwner(Restaurant restaurant) {
        User userOwner = userServicePort.getUserById(restaurant.getOwnerId());
        if (userOwner == null) {
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        if (userOwner.hasNoRole()) {
            throw new DomainException(DomainConstants.MSG_NOT_ELEVATED_ROLE, HttpStatus.BAD_REQUEST);
        }

        if (userOwner.notSameRole(Role.OWNER)) {
            throw new DomainException(DomainConstants.MSG_OWNER_INVALID_ROLE, HttpStatus.BAD_REQUEST);
        }
    }
}
