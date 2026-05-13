package com.pragma.foodcourtservice.infrastructure.input.rest;

import com.pragma.foodcourtservice.application.dto.OrderDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.handler.IOrderHandler;
import com.pragma.foodcourtservice.domain.model.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderHandler orderHandler;

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        orderHandler.createOrder(orderDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<OrderDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam()OrderStatus status
            ) {
        PageResponseDTO<OrderDTO> ordersPage = orderHandler.getOrders(page, size, status);
        return ResponseEntity.ok(ordersPage);
    }

    @PutMapping("/{orderId}/assign")
    public ResponseEntity<Void> assignEmployeeToOrder(@PathVariable Long orderId) {
        orderHandler.assignEmployeeToOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{orderId}/ready")
    public ResponseEntity<Void> markOrderAsReady(@PathVariable Long orderId) {
        orderHandler.markOrderAsReady(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Void> markOrderAsDelivered(
            @PathVariable Long orderId,
            @RequestParam String securityCode
            ) {
        orderHandler.markOrderAsDelivered(orderId, securityCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
