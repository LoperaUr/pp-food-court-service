package com.pragma.foodcourtservice.domain.exception;

public class DishDoesNotBelongToRestaurantException extends OrderDomainException {

    public DishDoesNotBelongToRestaurantException(String message) {
        super(message);
    }
}

