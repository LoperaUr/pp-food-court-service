package com.pragma.foodcourtservice.domain.exception;

public class InvalidSecurityCodeException extends OrderDomainException {

    public InvalidSecurityCodeException(String message) {
        super(message);
    }
}

