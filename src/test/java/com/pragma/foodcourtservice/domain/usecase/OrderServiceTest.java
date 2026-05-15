package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.ClientAlreadyHasActiveOrderException;
import com.pragma.foodcourtservice.domain.exception.DishDoesNotBelongToRestaurantException;
import com.pragma.foodcourtservice.domain.exception.InvalidOrderDataException;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderDish;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.domain.api.INotificationServicePort;
import com.pragma.foodcourtservice.domain.api.ITraceabilityServicePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import com.pragma.foodcourtservice.testdata.builders.RestaurantBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IRestaurantServicePort restaurantServicePort;
    @Mock
    private IDishServicePort dishServicePort;
    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private INotificationServicePort notificationServicePort;
    @Mock
    private ITraceabilityServicePort traceabilityServicePort;

    private Restaurant restaurant;
    private Order order;
    private Dish dish;
    private User clientUser;

    @BeforeEach
    void setUp() {
        restaurant = RestaurantBuilder.aRestaurant()
                .withId(10L)
                .withOwnerId(2L)
                .build();

        OrderDish orderDish = new OrderDish();
        orderDish.setDishId(100L);
        orderDish.setQuantity(2);

        dish = new Dish();
        dish.setId(100L);
        dish.setRestaurantId(10L);

        order = new Order();
        order.setRestaurantId(10L);
        order.setDishes(new ArrayList<>(List.of(orderDish)));

        clientUser = new User();
        clientUser.setId(1L);
        clientUser.setEmail("client@example.com");
    }

    @Test
    void createOrderIsSuccess() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishesByIds(anyList())).thenReturn(List.of(dish));
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        orderService.createOrder(order, 1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertAll(
                () -> assertEquals(1L, savedOrder.getClientId()),
                () -> assertEquals(OrderStatus.PENDING, savedOrder.getStatus()),
                () -> assertNotNull(savedOrder.getDate()),
                () -> assertEquals(10L, savedOrder.getRestaurantId()),
                () -> assertEquals(1, savedOrder.getDishes().size())
        );
        verify(orderPersistencePort).hasActiveOrderForClient(1L, OrderStatus.activeStatuses());
        verify(restaurantServicePort).getRestaurantById(10L);
        verify(dishServicePort).getDishesByIds(List.of(100L));
        verify(traceabilityServicePort).registerOrderStatusChange(
                eq(savedOrder.getId()),
                eq(1L),
                eq("client@example.com"),
                any(LocalDateTime.class),
                org.mockito.ArgumentMatchers.isNull(),
                eq(OrderStatus.PENDING.getCode()),
                org.mockito.ArgumentMatchers.isNull()
        );
    }

    @Test
    void assignEmployeeToOrderIsSuccess() {
        order.setStatus(OrderStatus.PENDING);
        order.setClientId(1L);

        when(orderPersistencePort.getOrderById(1L)).thenReturn(java.util.Optional.of(order));
        when(employeeRestaurantPersistencePort.isEmployeeFromRestaurant(5L, 10L)).thenReturn(true);
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        orderService.assignEmployeeToOrder(1L, 5L);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(captor.capture());
        Order saved = captor.getValue();
        assertEquals(5L, saved.getChefId());
        assertEquals(OrderStatus.IN_PREPARATION, saved.getStatus());
        verify(traceabilityServicePort).registerOrderStatusChange(
                eq(saved.getId()),
                eq(1L),
                eq("client@example.com"),
                any(LocalDateTime.class),
                eq(OrderStatus.PENDING.getCode()),
                eq(OrderStatus.IN_PREPARATION.getCode()),
                eq(5L)
        );
    }

    @Test
    void markOrderAsReadySendsNotification() {
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setChefId(7L);
        order.setClientId(1L);

        when(orderPersistencePort.getOrderById(2L)).thenReturn(java.util.Optional.of(order));
        when(employeeRestaurantPersistencePort.isEmployeeFromRestaurant(7L, 10L)).thenReturn(true);
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        orderService.markOrderAsReady(2L, 7L);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(captor.capture());
        Order saved = captor.getValue();
        assertNotNull(saved.getSecurityPin());
        verify(notificationServicePort).notifyOrderReady(eq("client@example.com"), org.mockito.ArgumentMatchers.contains(saved.getSecurityPin()));
    }

    @Test
    void markOrderAsDeliveredWithValidPin() {
        order.setStatus(OrderStatus.READY);
        order.setChefId(9L);
        order.setClientId(1L);
        order.setSecurityPin("123456");

        when(orderPersistencePort.getOrderById(3L)).thenReturn(java.util.Optional.of(order));
        when(employeeRestaurantPersistencePort.isEmployeeFromRestaurant(9L, 10L)).thenReturn(true);
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        orderService.markOrderAsDelivered(3L, "123456", 9L);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(captor.capture());
        Order saved = captor.getValue();
        assertEquals(OrderStatus.DELIVERED, saved.getStatus());
    }

    @Test
    void markOrderAsDeliveredWithInvalidPinThrows() {
        order.setStatus(OrderStatus.READY);
        order.setChefId(9L);
        order.setSecurityPin("123456");

        when(orderPersistencePort.getOrderById(4L)).thenReturn(java.util.Optional.of(order));
        when(employeeRestaurantPersistencePort.isEmployeeFromRestaurant(9L, 10L)).thenReturn(true);

        assertThrows(com.pragma.foodcourtservice.domain.exception.InvalidSecurityCodeException.class,
                () -> orderService.markOrderAsDelivered(4L, "000000", 9L));
    }

    @Test
    void cancelOrderByClientSuccess() {
        order.setStatus(OrderStatus.PENDING);
        order.setClientId(1L);

        when(orderPersistencePort.getOrderById(5L)).thenReturn(java.util.Optional.of(order));
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        orderService.cancelOrder(5L, 1L);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(captor.capture());
        Order saved = captor.getValue();
        assertEquals(OrderStatus.CANCELLED, saved.getStatus());
    }

    @Test
    void cancelOrderNotPendingThrows() {
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setClientId(1L);

        when(orderPersistencePort.getOrderById(6L)).thenReturn(java.util.Optional.of(order));

        assertThrows(com.pragma.foodcourtservice.domain.exception.InvalidOrderStateException.class,
                () -> orderService.cancelOrder(6L, 1L));
    }

    @Test
    void cancelOrderByDifferentClientThrows() {
        order.setStatus(OrderStatus.PENDING);
        order.setClientId(99L);

        when(orderPersistencePort.getOrderById(7L)).thenReturn(java.util.Optional.of(order));

        assertThrows(com.pragma.foodcourtservice.domain.exception.OrderDoesNotBelongToAuthenticatedClientException.class,
                () -> orderService.cancelOrder(7L, 1L));
    }

    @Test
    void createOrderThrowsWhenDishesAreEmpty() {
        order.setDishes(new ArrayList<>());

        InvalidOrderDataException exception = assertThrows(InvalidOrderDataException.class,
                () -> orderService.createOrder(order, 1L));

        assertEquals(DomainConstants.MSG_ORDER_MUST_CONTAIN_AT_LEAST_ONE_DISH, exception.getMessage());
        verifyNoInteractions(restaurantServicePort, orderPersistencePort, dishServicePort);
    }

    @Test
    void createOrderThrowsWhenRestaurantIdIsNull() {
        order.setRestaurantId(null);

        InvalidOrderDataException exception = assertThrows(InvalidOrderDataException.class,
                () -> orderService.createOrder(order, 1L));

        assertEquals(DomainConstants.MSG_ORDER_MUST_HAVE_RESTAURANT_ID, exception.getMessage());
        verifyNoInteractions(restaurantServicePort, orderPersistencePort, dishServicePort);
    }

    @Test
    void createOrderThrowsWhenClientAlreadyHasActiveOrder() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(true);

        ClientAlreadyHasActiveOrderException exception = assertThrows(ClientAlreadyHasActiveOrderException.class,
                () -> orderService.createOrder(order, 1L));

        assertEquals(DomainConstants.MSG_CLIENT_ALREADY_HAS_ACTIVE_ORDER, exception.getMessage());
        verify(restaurantServicePort).getRestaurantById(10L);
        verify(orderPersistencePort).hasActiveOrderForClient(1L, OrderStatus.activeStatuses());
        verify(dishServicePort, never()).getDishesByIds(anyList());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    void createOrderThrowsWhenDishDoesNotBelongToRestaurant() {
        Dish otherRestaurantDish = new Dish();
        otherRestaurantDish.setId(100L);
        otherRestaurantDish.setRestaurantId(99L);

        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishesByIds(anyList())).thenReturn(List.of(otherRestaurantDish));

        DishDoesNotBelongToRestaurantException exception = assertThrows(DishDoesNotBelongToRestaurantException.class,
                () -> orderService.createOrder(order, 1L));

        assertEquals(DomainConstants.MSG_DISH_DOES_NOT_BELONG_TO_RESTAURANT, exception.getMessage());
        verify(orderPersistencePort).hasActiveOrderForClient(1L, OrderStatus.activeStatuses());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    void createOrderSetsDateWhenSaving() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishesByIds(anyList())).thenReturn(List.of(dish));
        when(userServicePort.getUserById(1L)).thenReturn(clientUser);

        order.setDate(null);
        orderService.createOrder(order, 1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder.getDate());
        assertTrue(savedOrder.getDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}



