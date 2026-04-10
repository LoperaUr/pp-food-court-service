package com.pragma.foodcourtservice.infrastructure.output.security.helper;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
public class TokenRelayService {

    public Optional<String> resolveAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthentication jwtAuthentication) {
            String token = jwtAuthentication.token();
            if (token != null && !token.isBlank()) {
                return Optional.of(DomainConstants.TOKEN_PREFIX + token);
            }
        }

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return Optional.empty();
        }

        HttpServletRequest request = attrs.getRequest();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(authHeader);
    }
}

