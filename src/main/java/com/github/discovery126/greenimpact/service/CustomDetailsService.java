package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.model.Credential;
import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.repository.CredentialRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credential credentialUser = this.credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with %s not found".formatted(email)));
        return User.builder()
                .username(credentialUser.getEmail())
                .password(credentialUser.getPasswordHash())
                .authorities(credentialUser.getUser().getRoles()
                        .stream()
                        .map(Role::getNameRole)
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}