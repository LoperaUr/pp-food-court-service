package com.pragma.foodcourtservice.infrastructure.constants;

import java.util.List;

public class InfrastructureConstants {

    // Prevent instantiation
    private InfrastructureConstants() {}

    // Json Keys
    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_PATH = "path";

    // Messages
    public static final String MSG_TOKEN_MISSING = "Token is missing or not provided";
    public static final String MSG_TOKEN_EXPIRED = "Token has expired";
    public static final String MSG_TOKEN_INVALID = "Invalid token";

    // Utils
    public static final String UTF_8 = "UTF-8";
    public static final String EMPTY_STRING = "";

    // Public Endpoints
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/actuator/**"
    );

    /**
     * Returns the list of public endpoints. These endpoints do not require authentication.
     */
    public static List<String> getPublicEndpoints() {
        return PUBLIC_ENDPOINTS;
    }
}

