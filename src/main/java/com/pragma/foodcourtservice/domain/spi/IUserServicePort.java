package com.pragma.foodcourtservice.domain.spi;

public interface IUserServicePort {
    boolean userExistsWithOwnerRole(Long userId);
}

