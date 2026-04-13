package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Category;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.ICategoryPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DishService implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IAuthenticationServicePort authenticationContextPort;
    private final ICategoryPersistencePort categoryPersistencePort;

    @Override
    public void createDish(Dish dish) {
        validateDish(dish);
        validateCategory(dish);

        dish.setActive(true);

        dishPersistencePort.createDish(dish);
    }

    private void validateCategory(Dish dish) {
        Category category = categoryPersistencePort.getCategoryById(dish.getCategoryId());
        if (category == null)
            throw new DomainException(DomainConstants.MSG_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    private void validateDish(Dish dish) {
        Long authId = authenticationContextPort.getAuthenticatedUser().getId();
        Restaurant restaurant = restaurantServicePort.getRestaurantById(dish.getRestaurantId());
        if (!restaurant.getOwnerId().equals(authId))
            throw new DomainException(DomainConstants.MSG_ONLY_OWNER_CAN_CREATE_DISH, HttpStatus.UNAUTHORIZED);
    }
}
