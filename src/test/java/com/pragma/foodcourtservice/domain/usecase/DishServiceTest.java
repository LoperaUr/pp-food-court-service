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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
}

