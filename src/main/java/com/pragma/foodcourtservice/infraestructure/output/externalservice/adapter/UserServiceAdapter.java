package com.pragma.foodcourtservice.infraestructure.output.externalservice.adapter;

import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.spi.IUserServicePort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceAdapter implements IUserServicePort {

    @Value("${user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    @Override
    public boolean userExistsWithOwnerRole(Long userId) {
        try {
            // Make a call to the user-service to validate if user exists with OWNER role
            // The endpoint should return the user details
            String url = userServiceUrl + "/users/" + userId;
            UserResponse response = restTemplate.getForObject(url, UserResponse.class);

            if (response == null) {
                throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            // Validate if the user has OWNER role
            if (!"OWNER".equalsIgnoreCase(response.getRole())) {
                throw new DomainException(DomainConstants.MSG_OWNER_INVALID_ROLE, HttpStatus.BAD_REQUEST);
            }

            return true;
        } catch (HttpClientErrorException.NotFound e) {
            throw new DomainException(DomainConstants.MSG_OWNER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            throw new DomainException("Error connecting to user service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Inner class for mapping user response
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserResponse {
        private Long id;
        private String name;
        private String lastName;
        private String role;
    }
}

