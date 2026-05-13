package com.pragma.foodcourtservice.domain.exception;

public class OrderNotFoundException extends OrderDomainException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}

