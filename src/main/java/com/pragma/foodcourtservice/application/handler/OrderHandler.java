package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.mapper.IOrderMapper;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderService;
    private final IOrderMapper orderMapper;
    private final IAuthenticationServicePort authenticationServicePort;

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toModel(orderDTO);
        orderService.createOrder(order);
    }

    @Override
    public PageResponseDTO<OrderDTO> getOrders(int page, int size, OrderStatus status) {
        PageModel<Order> orders = orderService.getOrders(page, size, status);
        return orderMapper.toPageResponseDTO(orders);
    }

    @Override
    public void assignEmployeeToOrder(Long orderId) {
        Long employeeId = authenticationServicePort.getAuthenticatedUser().getId();
        orderService.assignEmployeeToOrder(orderId, employeeId);
    }
}
