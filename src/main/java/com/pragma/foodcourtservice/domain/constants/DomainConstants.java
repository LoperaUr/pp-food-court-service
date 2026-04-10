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
    public static final String MSG_USER_SERVICE_UNAUTHORIZED = "Not authorized to query user service";
    public static final String MSG_USER_SERVICE_UNAVAILABLE = "User service unavailable";
    public static final String MSG_AUTHENTICATION_STATE_IMMUTABLE = "Authentication state cannot be changed";

    // Key Names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_ROLE_NAME = "role_name";

    // Token
    public static final String TOKEN_PREFIX = "Bearer ";
}



