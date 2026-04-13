package com.pragma.foodcourtservice.infrastructure.output.security.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.infrastructure.constants.InfrastructureConstants;
import com.pragma.foodcourtservice.infrastructure.output.security.adapter.JwtServiceAdapter;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServiceAdapter jwtServiceAdapter;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(JwtServiceAdapter jwtServiceAdapter, ObjectMapper objectMapper) {
        this.jwtServiceAdapter = jwtServiceAdapter;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtServiceAdapter.validateToken(token);

            if (claims.getExpiration().before(new Date())) {
                setResponseProperties(response, HttpServletResponse.SC_UNAUTHORIZED, InfrastructureConstants.MSG_TOKEN_EXPIRED, request);
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(claims, token));
        } catch (Exception e) {
            setResponseProperties(response, HttpServletResponse.SC_FORBIDDEN, InfrastructureConstants.MSG_TOKEN_INVALID, request);
            return;
        }

        filterChain.doFilter(request, response);

    }

    private void setResponseProperties(HttpServletResponse response, int status, String message, HttpServletRequest request) throws IOException {
        Map<String, Object> payload = new HashMap<>();
        payload.put(InfrastructureConstants.KEY_STATUS, status);
        payload.put(InfrastructureConstants.KEY_MESSAGE, message);
        payload.put(InfrastructureConstants.KEY_PATH, request != null ? request.getRequestURI() : InfrastructureConstants.EMPTY_STRING);

        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(InfrastructureConstants.UTF_8);

        String json = objectMapper.writeValueAsString(payload);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(DomainConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(DomainConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

}