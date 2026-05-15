package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.Traceability;

import java.time.LocalDateTime;
import java.util.List;

public interface ITraceabilityServicePort {
    void registerOrderStatusChange(Long orderId,
                                   Long clientId,
                                   String clientEmail,
                                   LocalDateTime date,
                                   String previousStatus,
                                   String newStatus,
                                   Long employeeId);

    List<Traceability> getTraceabilityByOrderId(Long orderId);
}

