package com.pragma.foodcourtservice.infrastructure.constants;


public class InfrastructureConstants {

    // Prevent instantiation
    private InfrastructureConstants() {}

    // Json Keys
    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PATH = "path";

    // Messages
    public static final String MSG_TOKEN_EXPIRED = "Token has expired";
    public static final String MSG_TOKEN_INVALID = "Invalid token";

    // Log Templates
    public static final String LOG_USER_FETCH_NOT_FOUND = "Error fetching user with ID {}: {}";
    public static final String LOG_USER_FETCH_UNAUTHORIZED = "Unauthorized access when fetching user with ID {}: {}";
    public static final String LOG_USER_FETCH_COMMUNICATION_ERROR = "Error communicating with user service for user ID {}: {}";

    // Utils
    public static final String UTF_8 = "UTF-8";
    public static final String EMPTY_STRING = "";

}

