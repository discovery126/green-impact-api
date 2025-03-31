package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.service.TaskService;
import com.github.discovery126.greenimpact.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity
                .ok(userService.getTasksForCurrentUserOrAll());
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/active-task")
    public ResponseEntity<List<TaskResponse>> getActiveTasks() {
        return ResponseEntity
                .ok(taskService.getActiveTasks());
    }
}
