package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.request.RegisterRequest;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import com.github.discovery126.greenimpact.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final SecuritySessionContext securitySessionContext;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/login")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> login() {
        return ResponseEntity
                .ok()
                .build();
    }
}
