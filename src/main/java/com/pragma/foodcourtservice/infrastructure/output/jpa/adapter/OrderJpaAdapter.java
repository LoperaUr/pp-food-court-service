package com.pragma.foodcourtservice.infrastructure.output.jpa.adapter;

import com.pragma.foodcourtservice.domain.model.Order;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import com.pragma.foodcourtservice.domain.model.PageModel;
import com.pragma.foodcourtservice.domain.spi.IOrderPersistencePort;
import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderEntity;
import com.pragma.foodcourtservice.infrastructure.output.jpa.mapper.IOrderEntityMapper;
import com.pragma.foodcourtservice.infrastructure.output.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderEntityMapper orderMapper;
    private final IOrderRepository orderRepository;

    @Override
    public void saveOrder(Order order) {
        OrderEntity orderEntity = orderMapper.toEntity(order);
        if (orderEntity.getDishes() != null) {
            orderEntity.getDishes().forEach(dishEntity -> dishEntity.setOrder(orderEntity));
        }
        OrderEntity saved = orderRepository.save(orderEntity);
        order.setId(saved.getId());
    }

    @Override
    public boolean hasActiveOrderForClient(Long clientId, Collection<OrderStatus> statuses) {
        return orderRepository.existsByClientIdAndStatusIn(clientId, statuses);
    }

    @Override
    public PageModel<Order> getOrders(int page, int size, OrderStatus status) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<OrderEntity> pageResult = orderRepository.getOrderEntitiesByStatus(status, pageRequest);
        return orderMapper.toPageModel(pageResult);
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toModel);
    }
}
