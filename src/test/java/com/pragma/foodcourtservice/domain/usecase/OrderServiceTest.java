package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IDishServicePort;
import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderDish;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.api.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import com.pragma.foodcourtservice.testdata.builders.RestaurantBuilder;
import com.pragma.foodcourtservice.testdata.builders.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
    private IAuthenticationServicePort authenticationServicePort;
    @Mock
    private IDishServicePort dishServicePort;

    private User authenticatedUser;
    private Restaurant restaurant;
    private Order order;
    private Dish dish;

    @BeforeEach
    void setUp() {
        authenticatedUser = UserBuilder.aUser()
                .withId(1L)
                .build();

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
    }

    @Test
    void createOrderIsSuccess() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(authenticationServicePort.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishById(100L)).thenReturn(dish);

        orderService.createOrder(order);

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
        verify(authenticationServicePort).getAuthenticatedUser();
        verify(dishServicePort).getDishById(100L);
    }

    @Test
    void createOrderThrowsWhenDishesAreEmpty() {
        order.setDishes(new ArrayList<>());

        DomainException exception = assertThrows(DomainException.class,
                () -> orderService.createOrder(order));

        assertEquals(DomainConstants.MSG_ORDER_MUST_CONTAIN_AT_LEAST_ONE_DISH, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verifyNoInteractions(restaurantServicePort, authenticationServicePort, orderPersistencePort, dishServicePort);
    }

    @Test
    void createOrderThrowsWhenRestaurantIdIsNull() {
        order.setRestaurantId(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> orderService.createOrder(order));

        assertEquals(DomainConstants.MSG_ORDER_MUST_HAVE_RESTAURANT_ID, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verifyNoInteractions(restaurantServicePort, authenticationServicePort, orderPersistencePort, dishServicePort);
    }

    @Test
    void createOrderThrowsWhenClientAlreadyHasActiveOrder() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(authenticationServicePort.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class,
                () -> orderService.createOrder(order));

        assertEquals(DomainConstants.MSG_CLIENT_ALREADY_HAS_ACTIVE_ORDER, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(restaurantServicePort).getRestaurantById(10L);
        verify(authenticationServicePort).getAuthenticatedUser();
        verify(orderPersistencePort).hasActiveOrderForClient(1L, OrderStatus.activeStatuses());
        verify(dishServicePort, never()).getDishById(any(Long.class));
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    void createOrderThrowsWhenDishDoesNotBelongToRestaurant() {
        Dish otherRestaurantDish = new Dish();
        otherRestaurantDish.setId(100L);
        otherRestaurantDish.setRestaurantId(99L);

        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(authenticationServicePort.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishById(100L)).thenReturn(otherRestaurantDish);

        DomainException exception = assertThrows(DomainException.class,
                () -> orderService.createOrder(order));

        assertEquals(DomainConstants.MSG_DISH_DOES_NOT_BELONG_TO_RESTAURANT, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(orderPersistencePort).hasActiveOrderForClient(1L, OrderStatus.activeStatuses());
        verify(orderPersistencePort, never()).saveOrder(any(Order.class));
    }

    @Test
    void createOrderSetsDateWhenSaving() {
        when(restaurantServicePort.getRestaurantById(10L)).thenReturn(restaurant);
        when(authenticationServicePort.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(orderPersistencePort.hasActiveOrderForClient(eq(1L), anyCollection())).thenReturn(false);
        when(dishServicePort.getDishById(100L)).thenReturn(dish);

        order.setDate(null);
        orderService.createOrder(order);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderPersistencePort).saveOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder.getDate());
        assertTrue(savedOrder.getDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}



