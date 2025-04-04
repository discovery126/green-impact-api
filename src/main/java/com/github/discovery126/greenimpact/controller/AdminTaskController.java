package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskCompletionResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.model.TaskCompletionStatus;
import com.github.discovery126.greenimpact.service.TaskCompletionService;
import com.github.discovery126.greenimpact.service.TaskService;
import com.github.discovery126.greenimpact.utils.ValidEnum;
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
    private final TaskCompletionService taskCompletionService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity
                .ok(taskService.getAllTasks());
    }
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(taskRequest));
    }

    @PostMapping("{id}")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody TaskRequest taskRequest,
                                                   @PathVariable Long id) {
        return ResponseEntity
                .ok(taskService.updateTask(taskRequest,id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity
                .noContent()
                .build();
    }
    @GetMapping("/completed-tasks")
    public ResponseEntity<List<TaskCompletionResponse>> getCompletedTasks() {
        return ResponseEntity
                .ok(taskCompletionService.getAllTaskCompletion());
    }

    @PostMapping("/completed-tasks/{id}/answer")
    public ResponseEntity<TaskCompletionResponse> answerCompletionTask(@PathVariable Long id,
                                                                       @RequestParam("status") TaskCompletionStatus status) {
        return ResponseEntity
                .ok(taskCompletionService.answerCompletionTask(id,status));
    }
}
