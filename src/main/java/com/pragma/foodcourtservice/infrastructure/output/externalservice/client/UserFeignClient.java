package com.pragma.foodcourtservice.infrastructure.output.externalservice.client;

import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "user-service-client",
        url = "${user-service.url:http://localhost:8081}"
)
public interface UserFeignClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(
            @PathVariable("id") Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}
