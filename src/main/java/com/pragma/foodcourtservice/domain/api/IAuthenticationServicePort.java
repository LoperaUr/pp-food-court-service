package com.pragma.foodcourtservice.domain.api;

import com.pragma.foodcourtservice.domain.model.User;

public interface IAuthenticationServicePort {
    User getAuthenticatedUser();
}

