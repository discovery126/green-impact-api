package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.UserRequest;
import com.github.discovery126.greenimpact.dto.request.UserUpdateRequest;
import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.UserResponse;
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

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        return ResponseEntity
                .ok(BaseSuccessResponse.<Page<UserResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.getAllUsers(page,size,sort))
                        .build()
                );
    }

    @PostMapping
    public ResponseEntity<BaseSuccessResponse<UserResponse>> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseSuccessResponse.<UserResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(userService.createUser(userRequest))
                        .build()
                );
    }
    @PostMapping("{id}")
    public ResponseEntity<BaseSuccessResponse<UserResponse>> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                                   @PathVariable Long id) {

        return ResponseEntity
                .ok(BaseSuccessResponse.<UserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.updateUser(userUpdateRequest,id))
                        .build()
                );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent()
                .build();
    }

}
