package com.pragma.foodcourtservice.domain.exception;

public class UnauthorizedRestaurantEmployeeException extends OrderDomainException {

    public UnauthorizedRestaurantEmployeeException(String message) {
        super(message);
    }
}

