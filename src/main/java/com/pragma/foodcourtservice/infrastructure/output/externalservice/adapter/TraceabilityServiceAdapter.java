package com.pragma.foodcourtservice.infrastructure.output.externalservice.adapter;

import com.pragma.foodcourtservice.domain.api.ITraceabilityServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.domain.model.Traceability;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.client.TraceabilityFeignClient;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.TraceabilityRequestDTO;
import com.pragma.foodcourtservice.infrastructure.output.security.helper.TokenRelayService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TraceabilityServiceAdapter implements ITraceabilityServicePort {

    private final TraceabilityFeignClient traceabilityFeignClient;
    private final TokenRelayService tokenRelayService;

    @Override
    public void registerOrderStatusChange(Long orderId,
                                          Long clientId,
                                          String clientEmail,
                                          LocalDateTime date,
                                          String previousStatus,
                                          String newStatus,
                                          Long employeeId) {
        try {
            String authorization = tokenRelayService
                    .resolveAuthorizationHeader()
                    .orElseThrow(() -> new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED));

            TraceabilityRequestDTO request = new TraceabilityRequestDTO();
            request.setOrderId(orderId);
            request.setClientId(clientId);
            request.setClientEmail(clientEmail);
            request.setDate(date);
            request.setPreviousStatus(previousStatus);
            request.setNewStatus(newStatus);
            request.setEmployeeId(employeeId);

            traceabilityFeignClient.registerEvent(request, authorization);
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Traceability service unauthorized for order {}: {}", orderId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (FeignException ex) {
            log.error("Traceability service communication error for order {}: {}", orderId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public List<Traceability> getTraceabilityByOrderId(Long orderId) {
        try {
            String authorization = tokenRelayService
                    .resolveAuthorizationHeader()
                    .orElseThrow(() -> new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED));

            return traceabilityFeignClient.getTraceabilityByOrderId(orderId, authorization);
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Traceability service unauthorized for order {}: {}", orderId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (FeignException ex) {
            log.error("Traceability service communication error for order {}: {}", orderId, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_TRACEABILITY_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}

