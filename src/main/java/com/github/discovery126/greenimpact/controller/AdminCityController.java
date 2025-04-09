package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
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
    public ResponseEntity<BaseSuccessResponse<List<CityResponse>>> getAllCities() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<CityResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(cityService.getAllCities())
                        .build()
                );
    }
    @PostMapping
    public ResponseEntity<BaseSuccessResponse<CityResponse>> createCity(@RequestBody @Valid CityRequest cityRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseSuccessResponse.<CityResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(cityService.createCity(cityRequest))
                        .build()
                );
    }
    @DeleteMapping("{id}")
    public ResponseEntity<BaseSuccessResponse<Void>> deleteCity(@PathVariable Integer id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent()
                .build();
    }
}
