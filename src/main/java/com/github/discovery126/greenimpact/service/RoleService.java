package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RolesRepository rolesRepository;

    public Role getRole(String roleName) {
        Optional<Role> role = rolesRepository.findByNameRole(roleName);
        if (role.isEmpty())
            throw new UsernameNotFoundException("Role not found: %s ".formatted(roleName));

        return role.get();
    }
}
