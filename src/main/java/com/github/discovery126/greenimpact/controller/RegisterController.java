package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.RegisterDto;
import com.github.discovery126.greenimpact.service.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/register")
@RequiredArgsConstructor
public class RegisterController {

    private final CredentialService credentialService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDto registerDto) {
        credentialService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
