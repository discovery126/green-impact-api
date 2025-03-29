package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.RoleNotFoundException;
import com.github.discovery126.greenimpact.model.Role;
import com.github.discovery126.greenimpact.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RolesRepository rolesRepository;

    public Role getRole(String roleName) {
        Optional<Role> role = rolesRepository.findByNameRole(roleName);
        if (role.isEmpty())
            throw new RoleNotFoundException("Роль с именем %s не найдена".formatted(roleName));

        return role.get();
    }

    public Role getRole(Integer id) {
        Optional<Role> role = rolesRepository.findById(id);
        if (role.isEmpty())
            throw new RoleNotFoundException("Роль с номером %d не найдена".formatted(id));

        return role.get();
    }
}
