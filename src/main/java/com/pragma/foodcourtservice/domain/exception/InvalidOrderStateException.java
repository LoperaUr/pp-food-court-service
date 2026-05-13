package com.pragma.foodcourtservice.domain.exception;

public class InvalidOrderStateException extends OrderDomainException {

    public InvalidOrderStateException(String message) {
        super(message);
    }
}

