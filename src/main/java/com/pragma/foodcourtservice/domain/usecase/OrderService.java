package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class OrderService implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IAuthenticationServicePort authenticationServicePort;
    private final IDishServicePort dishServicePort;

    @Override
    public void createOrder(Order order) {
        validateOrderHasDishes(order);
        validateRestaurantId(order);

        Restaurant restaurant = restaurantServicePort.getRestaurantById(order.getRestaurantId());
        User authenticatedUser = authenticationServicePort.getAuthenticatedUser();

        validateClientHasNoActiveOrder(authenticatedUser);
        validateDishesBelongToRestaurant(order, restaurant);

        order.setClientId(authenticatedUser.getId());
        order.setStatus(OrderStatus.PENDING);
        order.setDate(LocalDateTime.now());
        orderPersistencePort.saveOrder(order);
    }

    private void validateOrderHasDishes(Order order) {
        if (order.getDishes() == null || order.getDishes().isEmpty()) {
            throw new DomainException(DomainConstants.MSG_ORDER_MUST_CONTAIN_AT_LEAST_ONE_DISH, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateRestaurantId(Order order) {
        if (order.getRestaurantId() == null) {
            throw new DomainException(DomainConstants.MSG_ORDER_MUST_HAVE_RESTAURANT_ID, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateClientHasNoActiveOrder(User authenticatedUser) {
        if (orderPersistencePort.hasActiveOrderForClient(authenticatedUser.getId(), OrderStatus.activeStatuses())) {
            throw new DomainException(DomainConstants.MSG_CLIENT_ALREADY_HAS_ACTIVE_ORDER, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateDishesBelongToRestaurant(Order order, Restaurant restaurant) {
        order.getDishes().forEach(dish -> {
            Dish dishById = dishServicePort.getDishById(dish.getDishId());
            if (!dishById.getRestaurantId().equals(restaurant.getId())) {
                throw new DomainException(DomainConstants.MSG_DISH_DOES_NOT_BELONG_TO_RESTAURANT, HttpStatus.BAD_REQUEST);
            }
        });
    }
}
