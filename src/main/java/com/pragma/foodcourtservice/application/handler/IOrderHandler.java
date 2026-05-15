package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.TraceabilityDTO;
import com.pragma.foodcourtservice.domain.model.OrderStatus;

import java.util.List;

public interface IOrderHandler {

    void createOrder(OrderDTO orderDTO);

    PageResponseDTO<OrderDTO> getOrders(int page, int size, OrderStatus status);

    void assignEmployeeToOrder(Long orderId);

    void markOrderAsReady(Long orderId);

    void markOrderAsDelivered(Long orderId, String securityCode);

    void cancelOrder(Long orderId);

    List<TraceabilityDTO> getOrderHistory(Long orderId);
}
