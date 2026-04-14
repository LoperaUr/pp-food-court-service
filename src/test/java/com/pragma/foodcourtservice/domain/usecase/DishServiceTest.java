package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.api.IRestaurantServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Category;
import com.pragma.foodcourtservice.domain.model.Dish;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.ICategoryPersistencePort;
import com.pragma.foodcourtservice.domain.spi.IDishPersistencePort;
import com.pragma.foodcourtservice.testdata.builders.CategoryBuilder;
import com.pragma.foodcourtservice.testdata.builders.DishBuilder;
import com.pragma.foodcourtservice.testdata.builders.RestaurantBuilder;
import com.pragma.foodcourtservice.testdata.builders.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @InjectMocks
    private DishService dishService;

    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IRestaurantServicePort restaurantServicePort;
    @Mock
    private IAuthenticationServicePort authenticationContextPort;
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    private User authUser;
    private Dish dish;
    private Restaurant restaurant;
    private Category category;

    @BeforeEach
    void setUp() {
        authUser = UserBuilder.anOwner()
                .withId(1L)
                .build();

        dish = DishBuilder.aDish()
                .withRestaurantId(1L)
                .withCategoryId(1L)
                .withActive(false)
                .build();

        restaurant = RestaurantBuilder.aRestaurant()
                .withId(1L)
                .withOwnerId(1L)
                .build();

        category = CategoryBuilder.aCategory()
                .withId(1L)
                .build();
    }

    @Test
    void createDishIsSuccess() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(restaurantServicePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(1L)).thenReturn(category);

        dishService.createDish(dish);

        assertTrue(dish.isActive());
        verify(dishPersistencePort).saveDish(any(Dish.class));
    }

    @Test
    void createDishThrowsWhenAuthenticatedUserIsNotRestaurantOwner() {
        User otherUser = UserBuilder.anOwner().withId(99L).build();

        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(otherUser);
        when(restaurantServicePort.getRestaurantById(1L)).thenReturn(restaurant);

        DomainException exception = assertThrows(DomainException.class,
                () -> dishService.createDish(dish));

        assertEquals(DomainConstants.MSG_ONLY_OWNER_CAN_CREATE_DISH, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        verify(categoryPersistencePort, never()).getCategoryById(any(Long.class));
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void createDishThrowsWhenCategoryNotFound() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(restaurantServicePort.getRestaurantById(1L)).thenReturn(restaurant);
        when(categoryPersistencePort.getCategoryById(1L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> dishService.createDish(dish));

        assertEquals(DomainConstants.MSG_CATEGORY_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void updateDishIsSuccessAndOnlyUpdatesPriceAndDescription() {
        Dish updatedDish = DishBuilder.aDish()
                .withId(1L)
                .withName("Nombre cambiado")
                .withCategoryId(99L)
                .withDescription("Descripcion actualizada")
                .withRestaurantId(99L)
                .withUrlImage("https://image.test/changed.png")
                .withActive(false)
                .withPrice(45000L)
                .build();

        Dish persistedDish = DishBuilder.aDish()
                .withId(1L)
                .withName("Nombre original")
                .withCategoryId(1L)
                .withDescription("Descripcion original")
                .withRestaurantId(1L)
                .withUrlImage("https://image.test/original.png")
                .withActive(true)
                .withPrice(20000L)
                .build();

        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(restaurantServicePort.getRestaurantByOwnerId(1L)).thenReturn(restaurant);
        when(dishPersistencePort.getDishById(1L)).thenReturn(persistedDish);

        dishService.updateDish(updatedDish);

        ArgumentCaptor<Dish> dishCaptor = ArgumentCaptor.forClass(Dish.class);
        verify(dishPersistencePort).saveDish(dishCaptor.capture());

        Dish savedDish = dishCaptor.getValue();
        assertAll(
                () -> assertEquals(persistedDish.getId(), savedDish.getId()),
                () -> assertEquals(persistedDish.getName(), savedDish.getName()),
                () -> assertEquals(persistedDish.getCategoryId(), savedDish.getCategoryId()),
                () -> assertEquals(updatedDish.getDescription(), savedDish.getDescription()),
                () -> assertEquals(persistedDish.getRestaurantId(), savedDish.getRestaurantId()),
                () -> assertEquals(persistedDish.getUrlImage(), savedDish.getUrlImage()),
                () -> assertEquals(persistedDish.isActive(), savedDish.isActive()),
                () -> assertEquals(updatedDish.getPrice(), savedDish.getPrice())
        );
    }

    @Test
    void updateDishThrowsWhenPriceIsNotValid() {
        Dish invalidDish = DishBuilder.aDish()
                .withId(1L)
                .withPrice(0L)
                .build();

        DomainException exception = assertThrows(DomainException.class,
                () -> dishService.updateDish(invalidDish));

        assertEquals(DomainConstants.MSG_PRICE_MUST_BE_GREATER_THAN_ZERO, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verifyNoInteractions(authenticationContextPort, restaurantServicePort, dishPersistencePort);
    }

    @Test
    void updateDishThrowsWhenDishNotFound() {
        Dish updatedDish = DishBuilder.aDish()
                .withId(1L)
                .withPrice(25000L)
                .build();

        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(restaurantServicePort.getRestaurantByOwnerId(1L)).thenReturn(restaurant);
        when(dishPersistencePort.getDishById(1L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> dishService.updateDish(updatedDish));

        assertEquals(DomainConstants.MSG_DISH_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }

    @Test
    void updateDishThrowsWhenAuthenticatedUserDoesNotOwnTheRestaurant() {
        Dish updatedDish = DishBuilder.aDish()
                .withId(1L)
                .withPrice(25000L)
                .build();

        Restaurant otherRestaurant = RestaurantBuilder.aRestaurant()
                .withId(2L)
                .withOwnerId(1L)
                .build();

        Dish persistedDish = DishBuilder.aDish()
                .withId(1L)
                .withRestaurantId(1L)
                .build();

        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(restaurantServicePort.getRestaurantByOwnerId(1L)).thenReturn(otherRestaurant);
        when(dishPersistencePort.getDishById(1L)).thenReturn(persistedDish);

        DomainException exception = assertThrows(DomainException.class,
                () -> dishService.updateDish(updatedDish));

        assertEquals(DomainConstants.MSG_ONLY_OWNER_CAN_UPDATE_DISH, exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        verify(dishPersistencePort, never()).saveDish(any(Dish.class));
    }
}

