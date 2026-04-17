package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.OrderDTO;

public interface IOrderHandler {
    void createOrder(OrderDTO orderDTO);
}
