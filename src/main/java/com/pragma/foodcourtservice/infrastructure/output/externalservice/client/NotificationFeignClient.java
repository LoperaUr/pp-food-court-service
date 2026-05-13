package com.pragma.foodcourtservice.infrastructure.output.externalservice.client;

import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "notification-service-client",
        url = "${notification-service.url:http://localhost:8082}"
)
public interface NotificationFeignClient {

    @PostMapping("/api/v1/notifications")
    void createNotification(
            @RequestBody NotificationRequestDTO request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}


