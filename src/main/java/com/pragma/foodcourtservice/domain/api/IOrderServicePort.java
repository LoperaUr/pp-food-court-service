package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;

public interface IOrderServicePort {
    void createOrder(Order order);

    PageModel<Order> getOrders(int page, int size, OrderStatus status);
}
