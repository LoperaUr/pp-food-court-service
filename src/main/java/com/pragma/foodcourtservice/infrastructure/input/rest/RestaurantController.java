package com.pragma.foodcourtservice.infrastructure.input.rest;

import com.pragma.foodcourtservice.application.dto.PageResponseDTO;
import com.pragma.foodcourtservice.application.dto.EmployeeRestaurantAssignmentDTO;
import com.pragma.foodcourtservice.application.dto.RestaurantDTO;
import com.pragma.foodcourtservice.application.handler.IRestaurantHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/employee-assignment")
    public ResponseEntity<Void> assignEmployeeToOwnerRestaurant(@RequestBody EmployeeRestaurantAssignmentDTO request) {
        restaurantHandler.assignEmployeeToOwnerRestaurant(request.getOwnerId(), request.getEmployeeId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<RestaurantDTO>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PageResponseDTO<RestaurantDTO> response = restaurantHandler.getRestaurants(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
