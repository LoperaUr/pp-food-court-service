package com.pragma.foodcourtservice.infrastructure.output.externalservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String identificationNumber;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
    private String role;

}
