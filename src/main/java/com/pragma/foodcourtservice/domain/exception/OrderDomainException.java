package com.pragma.foodcourtservice.domain.exception;

public abstract class OrderDomainException extends RuntimeException {

    protected OrderDomainException(String message) {
        super(message);
    }
}

