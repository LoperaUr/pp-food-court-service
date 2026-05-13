package com.pragma.foodcourtservice.infrastructure.output.externalservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationRequestDTO {

    @NotBlank
    @Email
    @Size(max = 120)
    private String recipient;

    @NotBlank
    @Size(max = 4000)
    private String message;
}

