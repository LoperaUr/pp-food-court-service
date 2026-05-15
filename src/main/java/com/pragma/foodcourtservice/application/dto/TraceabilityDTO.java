package com.pragma.foodcourtservice.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TraceabilityDTO {
    private Long orderId;
    private Long clientId;
    private String clientEmail;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
}

