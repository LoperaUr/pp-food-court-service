package com.pragma.foodcourtservice.infrastructure.input.rest;

import com.pragma.foodcourtservice.application.dto.DishDTO;
import com.pragma.foodcourtservice.application.dto.DishStatusDTO;
import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import jakarta.validation.Valid;
import com.pragma.foodcourtservice.application.handler.IDishHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class DishController {

    private final IDishHandler dishHandler;

    @PostMapping
    public ResponseEntity<Void> createDish(@Valid @RequestBody DishDTO dishDTO) {
        dishHandler.createDish(dishDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/dishes/{id}")
    public ResponseEntity<Void> updateDish(
            @PathVariable Long id,
            @RequestBody DishDTO dishDTO) {
        dishHandler.updateDish(id, dishDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/dishes/{id}/status")
    public ResponseEntity<Void> updateDishStatus(
            @PathVariable Long id,
            @Valid @RequestBody DishStatusDTO dishStatusDTO) {
        dishHandler.updateDishStatus(id, dishStatusDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restaurants/{id}/dishes")
    public ResponseEntity<PageResponseDTO<DishDTO>> getDishesByRestaurant(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        PageResponseDTO<DishDTO> response = dishHandler.getDishesByRestaurant(id, categoryId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
