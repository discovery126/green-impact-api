package com.github.discovery126.greenimpact.security;

import com.github.discovery126.greenimpact.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecuritySessionContext {

    public String getEmail() throws UnauthorizedException {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not found in current session context");
        }

        return authentication.getName(); // Name -> email
    }
    public Long getId() throws UnauthorizedException {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not found in current session context");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getId();
    }

}