package com.pragma.foodcourtservice.application.handler;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.TraceabilityDTO;
import com.pragma.foodcourtservice.application.mapper.IOrderMapper;
import com.pragma.foodcourtservice.domain.api.IOrderServicePort;
import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.api.IAuthenticationServicePort;
import com.pragma.foodcourtservice.domain.model.Traceability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderMapper orderMapper;
    private final IAuthenticationServicePort authenticationServicePort;

    @Override
    public void createOrder(OrderDTO orderDTO) {
        Long clientId = authenticationServicePort.getAuthenticatedUser().getId();
        Order order = orderMapper.toModel(orderDTO);
        orderServicePort.createOrder(order, clientId);
    }

    @Override
    public PageResponseDTO<OrderDTO> getOrders(int page, int size, OrderStatus status) {
        PageModel<Order> orders = orderServicePort.getOrders(page, size, status);
        return orderMapper.toPageResponseDTO(orders);
    }

    @Override
    public void assignEmployeeToOrder(Long orderId) {
        Long employeeId = authenticationServicePort.getAuthenticatedUser().getId();
        orderServicePort.assignEmployeeToOrder(orderId, employeeId);
    }

    @Override
    public void markOrderAsReady(Long orderId) {
        Long employeeId = authenticationServicePort.getAuthenticatedUser().getId();
        orderServicePort.markOrderAsReady(orderId, employeeId);
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String securityCode) {
        Long employeeId = authenticationServicePort.getAuthenticatedUser().getId();
        orderServicePort.markOrderAsDelivered(orderId, securityCode, employeeId);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Long clientId = authenticationServicePort.getAuthenticatedUser().getId();
        orderServicePort.cancelOrder(orderId, clientId);
    }

    @Override
    public List<TraceabilityDTO> getOrderHistory(Long orderId) {
        Long clientId = authenticationServicePort.getAuthenticatedUser().getId();
        List<Traceability> traceability = orderServicePort.getOrderHistory(orderId, clientId);
        return orderMapper.toTraceabilityDTOList(traceability);
    }
}
