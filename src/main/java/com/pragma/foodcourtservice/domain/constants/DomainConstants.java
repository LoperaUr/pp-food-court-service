package com.pragma.foodcourtservice.domain.constants;

public final class DomainConstants {

    // Prevent instantiation
    private DomainConstants() {}

    // System Messages
    public static final String MSG_RESTAURANT_NOT_FOUND = "Restaurant not found";
    public static final String MSG_OWNER_NOT_FOUND = "Owner user not found";
    public static final String MSG_OWNER_INVALID_ROLE = "Owner user must have OWNER role";

    // Key Names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_ROLE_NAME = "role_name";

    // Token
    public static final String TOKEN_PREFIX = "Bearer ";
}



