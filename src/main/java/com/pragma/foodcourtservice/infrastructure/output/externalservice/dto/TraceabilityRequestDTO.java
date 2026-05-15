package com.pragma.foodcourtservice.infrastructure.output.externalservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TraceabilityRequestDTO {
    private Long orderId;
    private Long clientId;
    private String clientEmail;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
}

