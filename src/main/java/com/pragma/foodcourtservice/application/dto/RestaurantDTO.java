package com.pragma.foodcourtservice.application.dto;

import com.pragma.foodcourtservice.application.constants.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RestaurantDTO {

    private Long id;

    @NotBlank(message = ApplicationConstants.RESTAURANT_NAME_CANNOT_BE_BLANK)
    @Pattern(regexp = "^(?!\\d+$).+", message = ApplicationConstants.RESTAURANT_NAME_ONLY_NUMBERS)
    private String name;

    @NotBlank(message = ApplicationConstants.RESTAURANT_NIT_CANNOT_BE_BLANK)
    @Pattern(regexp = "^\\d+$", message = ApplicationConstants.RESTAURANT_NIT_MUST_BE_NUMERIC)
    private String nit;

    @NotBlank(message = ApplicationConstants.RESTAURANT_ADDRESS_CANNOT_BE_BLANK)
    private String address;

    @NotBlank(message = ApplicationConstants.RESTAURANT_PHONE_CANNOT_BE_BLANK)
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = ApplicationConstants.RESTAURANT_PHONE_INVALID)
    private String phone;

    @NotBlank(message = ApplicationConstants.RESTAURANT_LOGO_CANNOT_BE_BLANK)
    private String urlLogo;

    @NotBlank(message = ApplicationConstants.RESTAURANT_OWNER_ID_CANNOT_BE_BLANK)
    @Pattern(regexp = "^\\d+$", message = ApplicationConstants.RESTAURANT_OWNER_ID_MUST_BE_NUMERIC)
    private String ownerId;
}
