package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.domain.model.OrderStatus;

public interface IOrderHandler {

    void createOrder(OrderDTO orderDTO);

    PageResponseDTO<OrderDTO> getOrders(int page, int size, OrderStatus status);
}
