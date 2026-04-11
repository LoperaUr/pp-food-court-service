package com.pragma.foodcourtservice.application.constants;

public final class ApplicationConstants {

    // Prevent instantiation
    private ApplicationConstants() {}

    // Messages
    public static final String RESTAURANT_NAME_CANNOT_BE_BLANK = "Restaurant name cannot be blank";
    public static final String RESTAURANT_NIT_CANNOT_BE_BLANK = "NIT cannot be blank";
    public static final String RESTAURANT_NIT_MUST_BE_NUMERIC = "NIT must be numeric";
    public static final String RESTAURANT_ADDRESS_CANNOT_BE_BLANK = "Address cannot be blank";
    public static final String RESTAURANT_PHONE_CANNOT_BE_BLANK = "Phone cannot be blank";
    public static final String RESTAURANT_PHONE_INVALID = "Phone must be numeric, maximum 13 characters, can contain +";
    public static final String RESTAURANT_LOGO_CANNOT_BE_BLANK = "URL Logo cannot be blank";
    public static final String RESTAURANT_OWNER_ID_CANNOT_BE_BLANK = "Owner ID cannot be blank";
    public static final String RESTAURANT_OWNER_ID_MUST_BE_NUMERIC = "Owner ID must be numeric";
    public static final String RESTAURANT_NAME_ONLY_NUMBERS = "Restaurant name cannot contain only numbers";

    public static final String DISH_NAME_CANNOT_BE_BLANK = "Dish name cannot be blank";
    public static final String DISH_CATEGORY_ID_CANNOT_BE_NULL = "Category is required";
    public static final String DISH_CATEGORY_ID_MUST_BE_POSITIVE = "Category must be a positive number";
    public static final String DISH_DESCRIPTION_CANNOT_BE_BLANK = "Description cannot be blank";
    public static final String DISH_RESTAURANT_ID_CANNOT_BE_NULL = "Restaurant ID is required";
    public static final String DISH_RESTAURANT_ID_MUST_BE_POSITIVE = "Restaurant ID must be a positive number";
    public static final String DISH_URL_IMAGE_CANNOT_BE_BLANK = "Image URL cannot be blank";
    public static final String DISH_PRICE_CANNOT_BE_NULL = "Price is required";
    public static final String DISH_PRICE_MUST_BE_POSITIVE = "Price must be a positive integer greater than 0";

}
