package com.pragma.foodcourtservice.domain.exception;

public class DishNotFoundException extends OrderDomainException {

    public DishNotFoundException(String message) {
        super(message);
    }
}

