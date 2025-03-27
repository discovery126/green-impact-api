package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.RegisterDto;
import com.github.discovery126.greenimpact.exception.EmailAlreadyExistsException;
import com.github.discovery126.greenimpact.exception.UsernameAlreadyExistsException;
import com.github.discovery126.greenimpact.model.City;
import com.github.discovery126.greenimpact.model.Credential;
import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.CredentialRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final PasswordEncoder passwordEncoder;
    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CityService cityService;

    public void register(RegisterDto registerDto) {

        final String roleByDefault = "USER";
        final String cityDefault = "Таганрог";
        final City city = cityService.getCity(cityDefault);
        final Role role = roleService.getRole(roleByDefault);

        Optional<User> existedUser = userRepository.findByUsername(registerDto.getUsername());
        if (existedUser.isPresent()) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        Optional<Credential> existedCredentialsUser = credentialRepository.findByEmail(registerDto.getEmail());
        if (existedCredentialsUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        Set<Role> userRoles = Set.of(role);

        User user = User.builder()
                .username(registerDto.getUsername())
                .roles(userRoles)
                .city(city)
                .build();

        Credential credential = Credential.builder()
                .user(user)
                .email(registerDto.getEmail())
                .passwordHash(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userRepository.save(user);

        credentialRepository.save(credential);

    }
}
