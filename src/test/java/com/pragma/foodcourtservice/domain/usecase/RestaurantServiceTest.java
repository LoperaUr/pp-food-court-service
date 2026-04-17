package com.pragma.foodcourtservice.domain.usecase;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.api.IUserServicePort;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.model.Restaurant;
import com.pragma.foodcourtservice.domain.model.Role;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.spi.IRestaurantPersistencePort;
import com.pragma.foodcourtservice.testdata.builders.UserBuilder;
import com.pragma.foodcourtservice.testdata.builders.RestaurantBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private IAuthenticationServicePort authenticationContextPort;

    private User authUser;
    private User nonAdminUser;
    private User authUserWithoutRole;
    private User ownerUser;
    private User ownerWithoutRole;
    private User nonOwnerUser;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        authUser = UserBuilder.anAdmin()
                .withId(1L)
                .build();

        nonAdminUser = UserBuilder.aUser()
                .withId(1L)
                .withRole(Role.EMPLOYEE.name())
                .build();

        authUserWithoutRole = UserBuilder.aUser()
                .withId(1L)
                .withRole(null)
                .build();

        ownerUser = UserBuilder.anOwner()
                .withId(2L)
                .build();

        ownerWithoutRole = UserBuilder.aUser()
                .withId(2L)
                .withRole(null)
                .build();

        nonOwnerUser = UserBuilder.aUser()
                .withId(2L)
                .withRole(Role.CLIENT.name())
                .build();

        restaurant = RestaurantBuilder.aRestaurant()
                .withOwnerId(2L)
                .build();
    }


    @Test
    void createRestaurantIsSuccess() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(userServicePort.getUserById(2L)).thenReturn(ownerUser);

        restaurantService.createRestaurant(restaurant);

        verify(restaurantPersistencePort).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenAuthUserIsNull() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_NOT_ELEVATED_ROLE, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenAuthUserHasNoRole() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUserWithoutRole);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_NOT_ELEVATED_ROLE, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenAuthUserIsNotAdmin() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(nonAdminUser);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_ROLE_NOT_AUTHORIZED, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenOwnerNotFound() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(userServicePort.getUserById(2L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_OWNER_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenOwnerHasNoRole() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(userServicePort.getUserById(2L)).thenReturn(ownerWithoutRole);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_NOT_ELEVATED_ROLE, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void createRestaurantThrowsWhenOwnerRoleIsNotOwner() {
        when(authenticationContextPort.getAuthenticatedUser()).thenReturn(authUser);
        when(userServicePort.getUserById(2L)).thenReturn(nonOwnerUser);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.createRestaurant(restaurant));

        assertEquals(DomainConstants.MSG_OWNER_INVALID_ROLE, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(restaurantPersistencePort, never()).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void getRestaurantByIdReturnsRestaurant() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(restaurant);

        Restaurant result = restaurantService.getRestaurantById(1L);

        assertEquals(restaurant.getOwnerId(), result.getOwnerId());
        assertEquals(restaurant.getName(), result.getName());
    }

    @Test
    void getRestaurantByIdThrowsWhenRestaurantNotFound() {
        when(restaurantPersistencePort.getRestaurantById(1L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.getRestaurantById(1L));

        assertEquals(DomainConstants.MSG_RESTAURANT_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void getRestaurantByOwnerIdReturnsRestaurant() {
        when(restaurantPersistencePort.getRestaurantByOwnerId(2L)).thenReturn(restaurant);

        Restaurant result = restaurantService.getRestaurantByOwnerId(2L);

        assertEquals(restaurant.getOwnerId(), result.getOwnerId());
        assertEquals(restaurant.getName(), result.getName());
    }

    @Test
    void getRestaurantByOwnerIdThrowsWhenOwnerHasNoRestaurant() {
        when(restaurantPersistencePort.getRestaurantByOwnerId(2L)).thenReturn(null);

        DomainException exception = assertThrows(DomainException.class,
                () -> restaurantService.getRestaurantByOwnerId(2L));

        assertEquals(DomainConstants.MSG_OWNER_NOT_HAVE_RESTAURANT, exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void getRestaurantsIsSuccessAndMasksSensitiveFields() {
        Restaurant restaurantOne = RestaurantBuilder.aRestaurant()
                .withId(10L)
                .withNit("900123456")
                .withPhone("3009999999")
                .withAddress("Calle 45")
                .withOwnerId(20L)
                .build();

        Restaurant restaurantTwo = RestaurantBuilder.aRestaurant()
                .withId(11L)
                .withNit("800987654")
                .withPhone("3018888888")
                .withAddress("Carrera 12")
                .withOwnerId(21L)
                .build();

        PageModel<Restaurant> pageModel = new PageModel<>();
        pageModel.setContent(java.util.List.of(restaurantOne, restaurantTwo));
        pageModel.setPageNumber(0);
        pageModel.setPageSize(10);
        pageModel.setTotalElements(2L);
        pageModel.setTotalPages(1);

        when(restaurantPersistencePort.getRestaurants(0, 10)).thenReturn(pageModel);

        PageModel<Restaurant> result = restaurantService.getRestaurants(0, 10);

        Restaurant firstRestaurant = result.getContent().iterator().next();
        Restaurant secondRestaurant = result.getContent().get(1);

        assertEquals(pageModel, result);
        assertNull(firstRestaurant.getId());
        assertNull(firstRestaurant.getNit());
        assertNull(firstRestaurant.getPhone());
        assertNull(firstRestaurant.getAddress());
        assertNull(firstRestaurant.getOwnerId());
        assertNull(secondRestaurant.getId());
        assertNull(secondRestaurant.getNit());
        assertNull(secondRestaurant.getPhone());
        assertNull(secondRestaurant.getAddress());
        assertNull(secondRestaurant.getOwnerId());
        verify(restaurantPersistencePort).getRestaurants(0, 10);
    }



}