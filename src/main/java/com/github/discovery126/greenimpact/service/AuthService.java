package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.LoginRequest;
import com.github.discovery126.greenimpact.dto.request.RegisterRequest;
import com.github.discovery126.greenimpact.dto.response.TokenResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.security.CustomUserDetails;
import com.github.discovery126.greenimpact.security.JwtTokenUtils;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService,
                       JwtTokenUtils jwtTokenUtils,
                       AuthenticationManager authenticationManager) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public void register(RegisterRequest registerRequest) {
        if (registerRequest.getPassword().getBytes().length > 72) {
            throw new CustomException(ValidationConstants.PASSWORD_CANT_BE_MORE_THAN_72);
        }
       userService.register(registerRequest);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        if (loginRequest.getPassword().getBytes().length > 72) {
            throw new CustomException(ValidationConstants.PASSWORD_CANT_BE_MORE_THAN_72);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenUtils.generateAccessToken(customUserDetails);
//        String refreshToken = jwtTokenUtils.generateRefreshToken(customUserDetails);


        return new TokenResponse(accessToken);
    }
}
