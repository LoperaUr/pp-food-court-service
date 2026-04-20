package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.*;
import com.pragma.foodcourtservice.domain.api.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
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
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

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

    @Override
    public PageModel<Order> getOrders(int page, int size, OrderStatus status) {
        return orderPersistencePort.getOrders(page, size, status);
    }

    @Override
    public void assignEmployeeToOrder(Long orderId, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId);
        validateOrderExists(order);
        validateEmployeeIsNotAlreadyAssigned(order);
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderIsAssignable(order);

        order.setChefId(employeeId);
        order.setStatus(OrderStatus.IN_PREPARATION);
        orderPersistencePort.saveOrder(order);
    }

    private void validateOrderIsAssignable(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_ASSIGNABLE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateOrderExists(Order order) {
        if (order == null) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void validateEmployeeIsNotAlreadyAssigned(Order order) {
        if (order.getChefId() != null) {
            throw new DomainException(DomainConstants.MSG_ALREADY_ASSIGNED_EMPLOYEE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmployeeFromRestaurant(Long employeeId, Long restaurantId) {
        if (!employeeRestaurantPersistencePort.isEmployeeFromRestaurant(employeeId, restaurantId)) {
            throw new DomainException(DomainConstants.MSG_EMPLOYEE_NOT_FROM_RESTAURANT, HttpStatus.FORBIDDEN);
        }
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
