package com.pragma.foodcourtservice.infraestructure.output.security.helper;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public record JwtAuthentication(Claims claims) implements Authentication {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Object roleName = claims.get("role_name");
        if (roleName == null) {
            return Collections.emptyList();
        }
        String role = String.valueOf(roleName);
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return claims;
    }

    @Override
    public Object getPrincipal() {
        return claims.get("sub");
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("No se puede cambiar el estado de autenticación");
    }

    @Override
    public String getName() {
        return (String) claims.get("sub");
    }
}



