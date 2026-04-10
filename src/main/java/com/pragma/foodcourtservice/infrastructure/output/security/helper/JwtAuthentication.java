package com.pragma.foodcourtservice.infrastructure.output.security.helper;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record JwtAuthentication(Claims claims, String token) implements Authentication {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Object roleName = claims.get(DomainConstants.KEY_ROLE_NAME);
        if (roleName == null) {
            return Collections.emptyList();
        }
        String role = String.valueOf(roleName);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return claims;
    }

    @Override
    public Object getPrincipal() {
        return claims.getSubject();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException(DomainConstants.MSG_AUTHENTICATION_STATE_IMMUTABLE);
    }

    @Override
    public String getName() {
        return claims.getSubject();
    }
}



