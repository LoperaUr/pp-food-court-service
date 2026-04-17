package com.pragma.foodcourtservice.application.dto;

import com.pragma.foodcourtservice.application.constants.ApplicationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;

    @NotNull(message = ApplicationConstants.ORDER_RESTAURANT_ID_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.ORDER_RESTAURANT_ID_MUST_BE_POSITIVE)
    private Long restaurantId;

    private LocalDateTime date;
    private String status;

    @NotEmpty(message = ApplicationConstants.ORDER_DISHES_CANNOT_BE_EMPTY)
    @Valid
    private List<OrderDishDTO> dishes;
}
