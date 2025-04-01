package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CityMapper cityMapper;
    private final RoleMapper roleMapper;

    public UserResponse toResponse(User user) {
        if (user == null)
            return null;

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .points(user.getPoints())
                .city(cityMapper.toResponse(user.getCity()))
                .roles(user.getRoles()
                        .stream()
                        .map(roleMapper::toResponse)
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }
}