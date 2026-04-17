package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Order;

public interface IOrderServicePort {
    void createOrder(Order order);
}
