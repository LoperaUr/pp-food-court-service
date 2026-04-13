package com.pragma.foodcourtservice.testdata.builders;

import com.pragma.foodcourtservice.domain.model.Role;
import com.pragma.foodcourtservice.domain.model.User;

import java.time.LocalDate;

public final class UserBuilder {

    private Long id = 1L;
    private String name = "Usuario";
    private String lastName = "Prueba";
    private String identificationNumber = "10000001";
    private String phoneNumber = "3000000001";
    private LocalDate birthDate = LocalDate.of(1995, 1, 1);
    private String email = "usuario.prueba@mail.com";
    private String password = "SecurePass123";
    private String role = Role.ADMIN.name();

    private UserBuilder() {
    }

    public static UserBuilder anAdmin() {
        return new UserBuilder().withRole(Role.ADMIN.name());
    }

    public static UserBuilder anOwner() {
        return new UserBuilder().withRole(Role.OWNER.name());
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
        return this;
    }

    public UserBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setIdentificationNumber(identificationNumber);
        user.setPhoneNumber(phoneNumber);
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}

