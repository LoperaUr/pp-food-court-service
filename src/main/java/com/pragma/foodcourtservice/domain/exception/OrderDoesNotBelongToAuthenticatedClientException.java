package com.pragma.foodcourtservice.domain.exception;

public class OrderDoesNotBelongToAuthenticatedClientException extends OrderDomainException {

    public OrderDoesNotBelongToAuthenticatedClientException(String message) {
        super(message);
    }
}

