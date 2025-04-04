package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.request.LoginRequest;
import com.github.discovery126.greenimpact.dto.request.RegisterRequest;
import com.github.discovery126.greenimpact.dto.response.TokenResponse;
import com.github.discovery126.greenimpact.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RegisterController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> createJwtToken(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity
                .ok(authService.login(loginRequest));
    }
}
