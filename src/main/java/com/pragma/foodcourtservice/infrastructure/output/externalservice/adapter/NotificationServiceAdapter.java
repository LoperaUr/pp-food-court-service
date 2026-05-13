package com.pragma.foodcourtservice.infrastructure.output.externalservice.adapter;

import com.pragma.foodcourtservice.domain.api.INotificationServicePort;
import com.pragma.foodcourtservice.domain.constants.DomainConstants;
import com.pragma.foodcourtservice.domain.exception.DomainException;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.client.NotificationFeignClient;
import com.pragma.foodcourtservice.infrastructure.output.externalservice.dto.NotificationRequestDTO;
import com.pragma.foodcourtservice.infrastructure.output.security.helper.TokenRelayService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceAdapter implements INotificationServicePort {

    private final NotificationFeignClient notificationFeignClient;
    private final TokenRelayService tokenRelayService;

    @Override
    public void notifyOrderReady(String recipient, String message) {
        try {
            String authorization = tokenRelayService
                    .resolveAuthorizationHeader()
                    .orElseThrow(() -> new DomainException(DomainConstants.MSG_NOTIFICATION_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED));

            NotificationRequestDTO request = new NotificationRequestDTO();
            request.setRecipient(recipient);
            request.setMessage(message);
            notificationFeignClient.createNotification(request, authorization);
        } catch (FeignException.Unauthorized | FeignException.Forbidden ex) {
            log.error("Notification service unauthorized for recipient {}: {}", recipient, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_NOTIFICATION_SERVICE_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        } catch (FeignException ex) {
            log.error("Notification service communication error for recipient {}: {}", recipient, ex.getMessage());
            throw new DomainException(DomainConstants.MSG_NOTIFICATION_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}



