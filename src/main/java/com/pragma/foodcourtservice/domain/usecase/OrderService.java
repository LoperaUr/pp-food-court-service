package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.api.INotificationServicePort;
import com.pragma.foodcourtservice.domain.api.ITraceabilityServicePort;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.ClientAlreadyHasActiveOrderException;
import com.pragma.foodcourtservice.domain.exception.DishDoesNotBelongToRestaurantException;
import com.pragma.foodcourtservice.domain.exception.DishNotFoundException;
import com.pragma.foodcourtservice.domain.exception.InvalidOrderDataException;
import com.pragma.foodcourtservice.domain.exception.InvalidOrderStateException;
import com.pragma.foodcourtservice.domain.exception.InvalidSecurityCodeException;
import com.pragma.foodcourtservice.domain.exception.OrderDoesNotBelongToAuthenticatedClientException;
import com.pragma.foodcourtservice.domain.exception.OrderNotFoundException;
import com.pragma.foodcourtservice.domain.exception.UnauthorizedRestaurantEmployeeException;
import com.pragma.foodcourtservice.domain.model.*;
import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class OrderService implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantServicePort restaurantServicePort;
    private final IDishServicePort dishServicePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IUserServicePort userServicePort;
    private final INotificationServicePort notificationServicePort;
    private final ITraceabilityServicePort traceabilityServicePort;

    @Override
    public void createOrder(Order order, Long clientId) {
        validateOrderHasDishes(order);
        validateRestaurantId(order);

        Restaurant restaurant = restaurantServicePort.getRestaurantById(order.getRestaurantId());

        validateClientHasNoActiveOrder(clientId);

        List<Long> dishIds = order.getDishes().stream()
                .map(OrderDish::getDishId)
                .toList();
        List<Dish> dishes = dishServicePort.getDishesByIds(dishIds);

        validateDishesBelongToRestaurant(order, restaurant, dishes);

        order.createForClient(clientId, LocalDateTime.now());
        orderPersistencePort.saveOrder(order);
        registerTraceability(order, null, null);
    }

    @Override
    public PageModel<Order> getOrders(int page, int size, OrderStatus status) {
        return orderPersistencePort.getOrders(page, size, status);
    }

    @Override
    public void assignEmployeeToOrder(Long orderId, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(DomainConstants.MSG_ORDER_NOT_FOUND));
        validateEmployeeIsNotAlreadyAssigned(order);
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderIsAssignable(order);

        OrderStatus previousStatus = order.getStatus();
        order.assignChef(employeeId);
        orderPersistencePort.saveOrder(order);
        registerTraceability(order, previousStatus, employeeId);
    }

    @Override
    public void markOrderAsReady(Long orderId, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(DomainConstants.MSG_ORDER_NOT_FOUND));
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderHasAssignedEmployee(order);
        validateEmployeeIsAssignedToOrder(order, employeeId);
        validateOrderIsInPreparation(order);

        OrderStatus previousStatus = order.getStatus();
        String securityPin = generateSecurityPin();
        order.markAsReady(securityPin);
        orderPersistencePort.saveOrder(order);
        registerTraceability(order, previousStatus, employeeId);

        User client = userServicePort.getUserById(order.getClientId());
        // Persist first and then notify: the message is external I/O and should only occur once the order state is durably stored.
        String message = "Tu pedido está listo para recoger. PIN de seguridad: " + securityPin;
        notificationServicePort.notifyOrderReady(
                client.getEmail(),
                message
        );
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String securityCode, Long employeeId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(DomainConstants.MSG_ORDER_NOT_FOUND));
        validateEmployeeFromRestaurant(employeeId, order.getRestaurantId());
        validateOrderHasAssignedEmployee(order);
        validateEmployeeIsAssignedToOrder(order, employeeId);
        validateOrderIsReady(order);
        validateSecurityCode(order, securityCode);

        OrderStatus previousStatus = order.getStatus();
        order.markAsDelivered();
        orderPersistencePort.saveOrder(order);
        registerTraceability(order, previousStatus, employeeId);
    }

    @Override
    public void cancelOrder(Long orderId, Long clientId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(DomainConstants.MSG_ORDER_NOT_FOUND));
        validateUserIsClientOfOrder(order, clientId);
        validateOrderIsPending(order);

        OrderStatus previousStatus = order.getStatus();
        order.cancel();
        orderPersistencePort.saveOrder(order);
        registerTraceability(order, previousStatus, null);
    }

    @Override
    public List<Traceability> getOrderHistory(Long orderId, Long clientId) {
        Order order = orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(DomainConstants.MSG_ORDER_NOT_FOUND));
        validateUserIsClientOfOrder(order, clientId);

        return traceabilityServicePort.getTraceabilityByOrderId(orderId);
    }

    private void validateOrderIsPending(Order order) {
        if (order.getStatus() == null || !order.getStatus().isPending()) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ONLY_PENDING_ORDERS_CAN_BE_CANCELLED);
        }
    }

    private void validateUserIsClientOfOrder(Order order, Long clientId) {
        if (!order.getClientId().equals(clientId)) {
            throw new OrderDoesNotBelongToAuthenticatedClientException(DomainConstants.MSG_ORDER_NOT_BELONG_TO_AUTHENTICATED_CLIENT);
        }
    }

    private void validateSecurityCode(Order order, String securityCode) {
        if (!order.getSecurityPin().equals(securityCode)) {
            throw new InvalidSecurityCodeException(DomainConstants.MSG_INVALID_SECURITY_CODE);
        }
    }

    private void validateOrderIsReady(Order order) {
        if (order.getStatus() == null || !order.getStatus().isReady()) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ORDER_NOT_READY);
        }

    }

    private void validateOrderIsAssignable(Order order) {
        if (order.getStatus() == null || !order.getStatus().isPending()) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ORDER_NOT_ASSIGNABLE);
        }
    }

    private void validateOrderIsInPreparation(Order order) {
        if (order.getStatus() == null || !order.getStatus().isInPreparation()) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ORDER_NOT_READY);
        }
    }

    private void validateOrderHasAssignedEmployee(Order order) {
        if (order.getChefId() == null) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ORDER_NOT_READY);
        }
    }

    private void validateEmployeeIsAssignedToOrder(Order order, Long employeeId) {
        if (!order.getChefId().equals(employeeId)) {
            throw new UnauthorizedRestaurantEmployeeException(DomainConstants.MSG_ORDER_NOT_ASSIGNED_TO_AUTHENTICATED_EMPLOYEE);
        }
    }

    private void validateEmployeeIsNotAlreadyAssigned(Order order) {
        if (order.getChefId() != null) {
            throw new InvalidOrderStateException(DomainConstants.MSG_ALREADY_ASSIGNED_EMPLOYEE);
        }
    }

    private void validateEmployeeFromRestaurant(Long employeeId, Long restaurantId) {
        if (!employeeRestaurantPersistencePort.isEmployeeFromRestaurant(employeeId, restaurantId)) {
            throw new UnauthorizedRestaurantEmployeeException(DomainConstants.MSG_EMPLOYEE_NOT_FROM_RESTAURANT);
        }
    }

    private void validateOrderHasDishes(Order order) {
        if (order.getDishes() == null || order.getDishes().isEmpty()) {
            throw new InvalidOrderDataException(DomainConstants.MSG_ORDER_MUST_CONTAIN_AT_LEAST_ONE_DISH);
        }
    }

    private void validateRestaurantId(Order order) {
        if (order.getRestaurantId() == null) {
            throw new InvalidOrderDataException(DomainConstants.MSG_ORDER_MUST_HAVE_RESTAURANT_ID);
        }
    }

    private void validateClientHasNoActiveOrder(Long clientId) {
        if (orderPersistencePort.hasActiveOrderForClient(clientId, OrderStatus.activeStatuses())) {
            throw new ClientAlreadyHasActiveOrderException(DomainConstants.MSG_CLIENT_ALREADY_HAS_ACTIVE_ORDER);
        }
    }

    private void validateDishesBelongToRestaurant(Order order, Restaurant restaurant, List<Dish> dishes) {
        Map<Long, Dish> dishesById = dishes.stream().collect(Collectors.toMap(Dish::getId, dish -> dish));

        order.getDishes().forEach(dish -> {
            Dish dishById = dishesById.get(dish.getDishId());
            if (dishById == null) {
                throw new DishNotFoundException(DomainConstants.MSG_DISH_NOT_FOUND);
            }

            if (!Objects.equals(dishById.getRestaurantId(), restaurant.getId())) {
                throw new DishDoesNotBelongToRestaurantException(DomainConstants.MSG_DISH_DOES_NOT_BELONG_TO_RESTAURANT);
            }
        });
    }

    private String generateSecurityPin() {
        int pin = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(pin);
    }

    private void registerTraceability(Order order, OrderStatus previousStatus, Long employeeId) {
        User client = userServicePort.getUserById(order.getClientId());
        traceabilityServicePort.registerOrderStatusChange(
                order.getId(),
                order.getClientId(),
                client.getEmail(),
                LocalDateTime.now(),
                previousStatus != null ? previousStatus.getCode() : null,
                order.getStatus().getCode(),
                employeeId
        );
    }
}
