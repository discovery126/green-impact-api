package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.CityRequest;
import com.github.discovery126.greenimpact.dto.response.CityResponse;
import com.github.discovery126.greenimpact.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/cities")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminCityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities() {
        return ResponseEntity
                .ok(cityService.getAllCities());
    }
    @PostMapping
    public ResponseEntity<CityResponse> createCity(@RequestBody @Valid CityRequest cityRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cityService.createCity(cityRequest));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent()
                .build();
    }
}
