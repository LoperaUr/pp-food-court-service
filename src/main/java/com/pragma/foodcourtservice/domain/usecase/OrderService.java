package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.api.INotificationServicePort;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.*;
import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class OrderService implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IDishServicePort dishServicePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IUserServicePort userServicePort;
    private final INotificationServicePort notificationServicePort;

    @Override
    public void createOrder(Order order, Long clientId) {
        validateOrderHasDishes(order);
        validateRestaurantId(order);

        Restaurant restaurant = restaurantServicePort.getRestaurantById(order.getRestaurantId());

        validateClientHasNoActiveOrder(clientId);
        validateDishesBelongToRestaurant(order, restaurant);

        order.setClientId(clientId);
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

    @Override
    public void markOrderAsReady(Long orderId, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId);
        validateOrderExists(order);
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderHasAssignedEmployee(order);
        validateEmployeeIsAssignedToOrder(order, employeeId);
        validateOrderIsInPreparation(order);

        String securityPin = generateSecurityPin();
        order.setSecurityPin(securityPin);
        order.setStatus(OrderStatus.READY);
        orderPersistencePort.saveOrder(order);

        User client = userServicePort.getUserById(order.getClientId());
        String message = "Tu pedido está listo para recoger. PIN de seguridad: " + securityPin;
        notificationServicePort.notifyOrderReady(
                client.getEmail(),
                message
        );
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String securityCode, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId);
        validateOrderExists(order);
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderHasAssignedEmployee(order);
        validateEmployeeIsAssignedToOrder(order, employeeId);
        validateOrderIsReady(order);
        validateSecurityCode(order, securityCode);

        order.setStatus(OrderStatus.DELIVERED);
        orderPersistencePort.saveOrder(order);
    }

    private void validateSecurityCode(Order order, String securityCode) {
        if (!order.getSecurityPin().equals(securityCode)) {
            throw new DomainException(DomainConstants.MSG_INVALID_SECURITY_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateOrderIsReady(Order order) {
        if (order.getStatus() != OrderStatus.READY) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_READY, HttpStatus.BAD_REQUEST);
        }

    }

    private void validateOrderIsAssignable(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_ASSIGNABLE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateOrderIsInPreparation(Order order) {
        if (order.getStatus() != OrderStatus.IN_PREPARATION) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_READY, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateOrderHasAssignedEmployee(Order order) {
        if (order.getChefId() == null) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_READY, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmployeeIsAssignedToOrder(Order order, Long employeeId) {
        if (!order.getChefId().equals(employeeId)) {
            throw new DomainException(DomainConstants.MSG_ORDER_NOT_ASSIGNED_TO_AUTHENTICATED_EMPLOYEE, HttpStatus.FORBIDDEN);
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

    private void validateClientHasNoActiveOrder(Long clientId) {
        if (orderPersistencePort.hasActiveOrderForClient(clientId, OrderStatus.activeStatuses())) {
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

    private String generateSecurityPin() {
        int pin = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(pin);
    }
}
