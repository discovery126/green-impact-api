package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/tasks")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminTaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getAllTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskService.getAllTasks())
                        .build()
                );
    }
    @PostMapping
    public ResponseEntity<BaseSuccessResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseSuccessResponse.<TaskResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .data(taskService.createTask(taskRequest))
                        .build()
                );
    }

    @PostMapping("{id}")
    public ResponseEntity<BaseSuccessResponse<TaskResponse>> updateTask(@Valid @RequestBody TaskRequest taskRequest,
                                                                        @PathVariable Long id) {
        return ResponseEntity
                .ok(BaseSuccessResponse.<TaskResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskService.updateTask(taskRequest,id))
                        .build()
                );
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
