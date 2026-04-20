package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Category;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.api.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.ICategoryPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import lombok.NonNull;
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
        validateCategoryExist(dish.getCategoryId());

        dish.setActive(true);

        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDish(Dish dish) {
        validateDishPrice(dish);
        Dish dishResult = getDishByIdAndValidateOwn(dish.getId());

        dishResult.setPrice(dish.getPrice());
        dishResult.setDescription(dish.getDescription());

        dishPersistencePort.saveDish(dishResult);
    }

    @Override
    public void updateDishStatus(Long dishId, boolean active) {
        Dish dishResult = getDishByIdAndValidateOwn(dishId);

        dishResult.setActive(active);

        dishPersistencePort.saveDish(dishResult);
    }

    @Override
    public PageModel<Dish> getDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(restaurantId);
        if (restaurant == null)
            throw new DomainException(DomainConstants.MSG_RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND);

        if (categoryId != 0) {
            validateCategoryExist(categoryId);
            return dishPersistencePort.getDishesByRestaurantAndCategoryId(restaurantId, categoryId, page, size);
        }

        return dishPersistencePort.getDishesByRestaurant(restaurantId, page, size);
    }

    @Override
    public Dish getDishById(Long id) {
        Dish dish = dishPersistencePort.getDishById(id);
        if (dish == null)
            throw new DomainException(DomainConstants.MSG_DISH_NOT_FOUND, HttpStatus.NOT_FOUND);
        return dish;
    }

    private @NonNull Dish getDishByIdAndValidateOwn(Long dishId) {
        Long authId = authenticationContextPort.getAuthenticatedUser().getId();
        Restaurant restaurantResult = restaurantServicePort.getRestaurantByOwnerId(authId);
        Dish dishResult = getDishById(dishId);

        validateRestaurantOwnership(restaurantResult, dishResult);
        return dishResult;
    }

    private void validateRestaurantOwnership(Restaurant restaurant, Dish dish) {
        if (!dish.getRestaurantId().equals(restaurant.getId()))
            throw new DomainException(DomainConstants.MSG_ONLY_OWNER_CAN_UPDATE_DISH, HttpStatus.UNAUTHORIZED);
    }

    private void validateDishPrice(Dish dish) {
        if (dish.getPrice() <= 0)
            throw new DomainException(DomainConstants.MSG_PRICE_MUST_BE_GREATER_THAN_ZERO, HttpStatus.BAD_REQUEST);
    }

    private void validateCategoryExist(Long categoryId) {
        Category category = categoryPersistencePort.getCategoryById(categoryId);
        if (category == null)
            throw new DomainException(DomainConstants.MSG_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    private void validateDish(Dish dish) {
        validateDishPrice(dish);
        Long authId = authenticationContextPort.getAuthenticatedUser().getId();
        Restaurant restaurant = restaurantServicePort.getRestaurantById(dish.getRestaurantId());
        if (!restaurant.getOwnerId().equals(authId))
            throw new DomainException(DomainConstants.MSG_ONLY_OWNER_CAN_CREATE_DISH, HttpStatus.UNAUTHORIZED);
    }

}
