package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.mapper.UserMapper;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Page<User> userDtoPage = userService.getAllUsers(page,size,sort);
        return ResponseEntity
                .ok(userDtoPage.map(userMapper::toResponse));
    }

}
