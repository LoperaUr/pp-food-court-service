package com.pragma.foodcourtservice.domain.api;

public interface INotificationServicePort {
    void notifyOrderReady(String recipient, String message);
}