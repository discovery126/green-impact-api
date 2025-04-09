package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.response.*;
import com.github.discovery126.greenimpact.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final TaskUserService taskUserService;
    private final UserEventService userEventService;
    private final UserRewardService userRewardService;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<UserResponse>> getUser() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<UserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.getUser())
                        .build()
                );
    }

    @GetMapping("/events")
    public ResponseEntity<BaseSuccessResponse<List<UserEventResponse>>> getUserEvents() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<UserEventResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userEventService.getUserEvents())
                        .build()
                );
    }

    @GetMapping("/tasks")
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.getUserTasks())
                        .build()
                );
    }

    @GetMapping("/active-tasks")
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getActiveTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskService.getActiveTasks())
                        .build()
                );
    }

    @GetMapping("/completed-tasks")
    public ResponseEntity<BaseSuccessResponse<List<TaskUserResponse>>> getCompletedTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskUserResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskUserService.getUserCompletionsTasks())
                        .build()
                );
    }

    @GetMapping("/rewards")
    public ResponseEntity<BaseSuccessResponse<List<UserRewardResponse>>> getUserRewards() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<UserRewardResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userRewardService.getUserRewards())
                        .build()
                );
    }
}
