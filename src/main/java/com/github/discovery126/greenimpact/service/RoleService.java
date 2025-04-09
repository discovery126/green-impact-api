package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
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
            throw new CustomException(ValidationConstants.ROLE_NAME_NOT_FOUND);

        return role.get();
    }

    public Role getRole(Integer id) {
        Optional<Role> role = rolesRepository.findById(id);
        if (role.isEmpty())
            throw new CustomException(ValidationConstants.ROLE_ID_NOT_FOUND);

        return role.get();
    }
}
