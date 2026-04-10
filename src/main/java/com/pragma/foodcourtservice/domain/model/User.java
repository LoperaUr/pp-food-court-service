package com.pragma.foodcourtservice.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String name;
    private String lastName;
    private String identificationNumber;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
    private String role;

    public boolean notSameRole(Role requiredRole) {
        return !this.getRole().equalsIgnoreCase(requiredRole.name());
    }

    public boolean hasNoRole() {
        return this.getRole() == null || this.getRole().isEmpty();
    }
}
