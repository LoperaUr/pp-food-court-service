package com.pragma.foodcourtservice.domain.spi;

import com.pragma.foodcourtservice.domain.model.User;

public interface IAuthenticationServicePort {
    User getAuthenticatedUser();
}

