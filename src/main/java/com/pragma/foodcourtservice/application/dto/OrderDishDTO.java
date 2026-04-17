package com.pragma.foodcourtservice.application.dto;

import com.pragma.foodcourtservice.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderDishDTO {
    private Long id;
    private Long orderId;

    @NotNull(message = ApplicationConstants.ORDER_DISH_ID_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.ORDER_DISH_ID_MUST_BE_POSITIVE)
    private Long dishId;

    @NotNull(message = ApplicationConstants.ORDER_DISH_QUANTITY_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.ORDER_DISH_QUANTITY_MUST_BE_POSITIVE)
    private Integer quantity;
}
