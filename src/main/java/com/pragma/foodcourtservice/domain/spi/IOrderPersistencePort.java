package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;

import java.util.Collection;

public interface IOrderPersistencePort {
    void saveOrder(Order order);

    boolean hasActiveOrderForClient(Long clientId, Collection<OrderStatus> statuses);

    PageModel<Order> getOrders(int page, int size, OrderStatus status);
}
