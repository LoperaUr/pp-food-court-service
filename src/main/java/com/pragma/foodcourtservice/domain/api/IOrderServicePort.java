package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;

public interface IOrderServicePort {
    void createOrder(Order order, Long clientId);

    PageModel<Order> getOrders(int page, int size, OrderStatus status);

    void assignEmployeeToOrder(Long orderId, Long employeeId);

    void markOrderAsReady(Long orderId, Long employeeId);

    void markOrderAsDelivered(Long orderId, String securityCode, Long deliveryPersonId);

    void cancelOrder(Long orderId, Long clientId);
}
