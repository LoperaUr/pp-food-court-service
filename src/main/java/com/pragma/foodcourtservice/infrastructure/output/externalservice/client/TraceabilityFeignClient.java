package com.pragma.foodcourtservice.infrastructure.output.externalservice.client;

import com.pragma.foodcourtservice.domain.model.Traceability;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.TraceabilityRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "traceability-service-client",
        url = "${traceability-service.url:http://localhost:8083}"
)
public interface TraceabilityFeignClient {

    @PostMapping("/api/v1/traceability")
    void registerEvent(
            @RequestBody TraceabilityRequestDTO request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );

    @GetMapping("/api/v1/traceability/orders/{orderId}")
    List<Traceability> getTraceabilityByOrderId(
            @PathVariable Long orderId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    );
}

