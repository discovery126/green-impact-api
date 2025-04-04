package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.UserRequest;
import com.github.discovery126.greenimpact.dto.request.UserUpdateRequest;
import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.mapper.UserMapper;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRequest));
    }
    @PostMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                                   @PathVariable Long id) {

        return ResponseEntity
                .ok(userService.updateUser(userUpdateRequest,id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent()
                .build();
    }

}
