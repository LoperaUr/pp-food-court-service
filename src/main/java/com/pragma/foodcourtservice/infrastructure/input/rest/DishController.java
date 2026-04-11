package com.pragma.foodcourtservice.infrastructure.input.rest;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import jakarta.validation.Valid;
import com.pragma.foodcourtservice.application.handler.IDishHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final IDishHandler dishHandler;

    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishDTO dishDTO) {
        dishHandler.createDish(dishDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
