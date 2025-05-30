package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.TaskCategoryResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.service.TaskCategoryService;
import com.github.discovery126.greenimpact.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskCategoryService taskCategoryService;

    @GetMapping
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getTasks() {
            return ResponseEntity
                    .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                            .code(HttpStatus.OK.value())
                            .data(taskService.getAllTasksForUsers())
                            .build()
                    );
    }

    @GetMapping("/categories")
    public ResponseEntity<BaseSuccessResponse<List<TaskCategoryResponse>>> getAllTasksCategories() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskCategoryResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskCategoryService.getAllCategories())
                        .build()
                );
    }
}
