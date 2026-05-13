package com.pragma.foodcourtservice.domain.exception;

public class ClientAlreadyHasActiveOrderException extends OrderDomainException {

    public ClientAlreadyHasActiveOrderException(String message) {
        super(message);
    }
}

