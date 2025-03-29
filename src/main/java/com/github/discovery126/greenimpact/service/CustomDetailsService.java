package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with %s not found".formatted(email)));

        return CustomUserDetails.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(user.getRoles()
                        .stream()
                        .map(Role::getNameRole)
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}