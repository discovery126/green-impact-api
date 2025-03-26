package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.RegisterDto;
import com.github.discovery126.greenimpact.model.Credential;
import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.CredentialRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public void register(RegisterDto registerDto) {

        final String roleByDefault = "USER";
        final Role role = roleService.getRole(roleByDefault);

        Set<Role> userRoles = Set.of(role);

        User user = User.builder()
                .username(registerDto.getUsername())
                .roles(userRoles)
                .build();

        userRepository.save(user);

        Credential credential = Credential.builder()
                .user(user)
                .email(registerDto.getEmail())
                .passwordHash(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        credentialRepository.save(credential);

    }
}
