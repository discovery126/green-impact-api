package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.RoleResponse;
import com.github.discovery126.greenimpact.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .nameRole(role.getNameRole())
                .build();
    }
}
