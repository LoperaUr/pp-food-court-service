package com.pragma.foodcourtservice.infrastructure.output.jpa.repository;

import com.pragma.foodcourtservice.infrastructure.output.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pragma.foodcourtservice.domain.model.OrderStatus;
import java.util.Collection;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByClientIdAndStatusIn(Long clientId, Collection<OrderStatus> statuses);

    Page<OrderEntity> getOrderEntitiesByStatus(OrderStatus status, Pageable pageable);
}
