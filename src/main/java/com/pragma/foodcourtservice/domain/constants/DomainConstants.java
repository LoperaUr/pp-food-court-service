package com.pragma.foodcourtservice.domain.constants;

public final class DomainConstants {

    // Prevent instantiation
    private DomainConstants() {}

    // System Messages
    public static final String MSG_RESTAURANT_NOT_FOUND = "Restaurant not found";
    public static final String MSG_OWNER_NOT_FOUND = "Owner user not found";
    public static final String MSG_OWNER_INVALID_ROLE = "Owner user must have OWNER role";
    public static final String MSG_NOT_ELEVATED_ROLE = "User does not have an elevated role";
    public static final String MSG_ROLE_NOT_AUTHORIZED = "User role not authorized to perform this action";
    public static final String MSG_ONLY_OWNER_CAN_CREATE_DISH = "Only the restaurant owner can create dishes for this restaurant";
    public static final String MSG_USER_SERVICE_UNAUTHORIZED = "Not authorized to query user service";
    public static final String MSG_USER_SERVICE_UNAVAILABLE = "User service unavailable";
    public static final String MSG_AUTHENTICATION_STATE_IMMUTABLE = "Authentication state cannot be changed";
    public static final String MSG_CATEGORY_NOT_FOUND = "Category not found";
    public static final String MSG_DISH_NOT_FOUND = "Dish not found";
    public static final String MSG_ONLY_OWNER_CAN_UPDATE_DISH = "Only the restaurant owner can update dishes for this restaurant";
    public static final String MSG_PRICE_MUST_BE_GREATER_THAN_ZERO = "Dish price must be greater than zero";
    public static final String MSG_OWNER_NOT_HAVE_RESTAURANT = "Owner does not have a restaurant";
    public static final String MSG_ORDER_MUST_CONTAIN_AT_LEAST_ONE_DISH = "Order must contain at least one dish";
    public static final String MSG_ORDER_MUST_HAVE_RESTAURANT_ID = "Order must have a restaurant ID";
    public static final String MSG_CLIENT_ALREADY_HAS_ACTIVE_ORDER = "Client already has an active order";
    public static final String MSG_DISH_DOES_NOT_BELONG_TO_RESTAURANT = "Dish does not belong to the selected restaurant";
    public static final String MSG_OWNER_TOKEN_MISMATCH = "Authenticated owner does not match request owner";
    public static final String MSG_INVALID_EMPLOYEE_ID = "Employee ID is required";


    // Key Names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_ROLE_NAME = "role_name";

    // Token
    public static final String TOKEN_PREFIX = "Bearer ";
}



