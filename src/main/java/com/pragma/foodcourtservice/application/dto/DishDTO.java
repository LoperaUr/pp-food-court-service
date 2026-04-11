package com.pragma.foodcourtservice.application.dto;

import com.pragma.foodcourtservice.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishDTO {
    private Long id;

    @NotBlank(message = ApplicationConstants.DISH_NAME_CANNOT_BE_BLANK)
    private String name;

    @NotNull(message = ApplicationConstants.DISH_CATEGORY_ID_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.DISH_CATEGORY_ID_MUST_BE_POSITIVE)
    private Long categoryId;

    @NotBlank(message = ApplicationConstants.DISH_DESCRIPTION_CANNOT_BE_BLANK)
    private String description;

    @NotNull(message = ApplicationConstants.DISH_RESTAURANT_ID_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.DISH_RESTAURANT_ID_MUST_BE_POSITIVE)
    private Long restaurantId;

    @NotBlank(message = ApplicationConstants.DISH_URL_IMAGE_CANNOT_BE_BLANK)
    private String urlImage;

    private boolean active;

    @NotNull(message = ApplicationConstants.DISH_PRICE_CANNOT_BE_NULL)
    @Positive(message = ApplicationConstants.DISH_PRICE_MUST_BE_POSITIVE)
    private Long price;
}
