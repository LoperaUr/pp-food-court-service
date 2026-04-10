package com.pragma.foodcourtservice.infrastructure.output.security.adapter;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.model.User;
import com.pragma.foodcourtservice.domain.spi.IAuthenticationContextPort;
import com.pragma.foodcourtservice.infrastructure.output.security.helper.JwtAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationContextAdapter implements IAuthenticationContextPort {

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthentication(Claims claims, String ignoredToken))) {
            return null;
        }

        User user = new User();
        user.setId(parseUserId(claims.get(DomainConstants.KEY_USER_ID)));
        user.setRole(claims.get(DomainConstants.KEY_ROLE_NAME, String.class));
        user.setEmail(claims.getSubject());
        return user;
    }

    private Long parseUserId(Object userIdClaim) {
        if (userIdClaim instanceof Number number) {
            return number.longValue();
        }

        if (userIdClaim instanceof String text && !text.isBlank()) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }
}

