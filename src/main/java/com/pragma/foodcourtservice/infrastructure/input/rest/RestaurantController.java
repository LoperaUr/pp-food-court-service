package com.pragma.foodcourtservice.infrastructure.input.rest;

import com.pragma.foodcourtservice.application.dto.RestaurantDTO;
import com.pragma.foodcourtservice.application.handler.IRestaurantHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantHandler restaurantHandler;

    @PostMapping
    public ResponseEntity<Void> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        restaurantHandler.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
