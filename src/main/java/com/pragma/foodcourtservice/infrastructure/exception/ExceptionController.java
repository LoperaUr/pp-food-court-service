package com.pragma.foodcourtservice.infrastructure.exception;

import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.exception.OrderDomainException;
import com.pragma.foodcourtservice.domain.exception.OrderNotFoundException;
import com.pragma.foodcourtservice.domain.exception.DishNotFoundException;
import com.pragma.foodcourtservice.domain.exception.InvalidOrderDataException;
import com.pragma.foodcourtservice.domain.exception.ClientAlreadyHasActiveOrderException;
import com.pragma.foodcourtservice.domain.exception.DishDoesNotBelongToRestaurantException;
import com.pragma.foodcourtservice.domain.exception.InvalidOrderStateException;
import com.pragma.foodcourtservice.domain.exception.UnauthorizedRestaurantEmployeeException;
import com.pragma.foodcourtservice.domain.exception.OrderDoesNotBelongToAuthenticatedClientException;
import com.pragma.foodcourtservice.domain.exception.InvalidSecurityCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(OrderDomainException.class)
    public ResponseEntity<Map<String, String>> handleOrderDomainException(OrderDomainException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(resolveOrderStatus(ex)).body(error);
    }

    private HttpStatus resolveOrderStatus(OrderDomainException ex) {
        if (ex instanceof OrderNotFoundException || ex instanceof DishNotFoundException) {
            return HttpStatus.NOT_FOUND;
        }

        if (ex instanceof UnauthorizedRestaurantEmployeeException || ex instanceof OrderDoesNotBelongToAuthenticatedClientException) {
            return HttpStatus.FORBIDDEN;
        }

        if (ex instanceof InvalidOrderDataException
                || ex instanceof ClientAlreadyHasActiveOrderException
                || ex instanceof DishDoesNotBelongToRestaurantException
                || ex instanceof InvalidOrderStateException
                || ex instanceof InvalidSecurityCodeException) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.BAD_REQUEST;
    }
}

