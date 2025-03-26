package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.RegisterDto;
import com.github.discovery126.greenimpact.model.Credential;
import com.github.discovery126.greenimpact.service.CredentialService;
import com.github.discovery126.greenimpact.service.CustomDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/register")
@RequiredArgsConstructor
public class RegisterController {

    private final CredentialService credentialService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
        credentialService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
