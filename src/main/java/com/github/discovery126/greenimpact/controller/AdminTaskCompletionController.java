package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.TaskUserResponse;
import com.github.discovery126.greenimpact.model.TaskCompletionStatus;
import com.github.discovery126.greenimpact.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminTaskCompletionController {
    private final TaskUserService taskUserService;
    @GetMapping("/completed-tasks")
    public ResponseEntity<BaseSuccessResponse<Page<TaskUserResponse>>> getCompletedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "status,desc") String sort) {

        return ResponseEntity
                .ok(BaseSuccessResponse.<Page<TaskUserResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskUserService.getAllTaskCompletion(page,size,sort))
                        .build()
                );
    }

    @PostMapping("/completed-tasks/{id}/answer")
    public ResponseEntity<BaseSuccessResponse<TaskUserResponse>> answerCompletionTask(@PathVariable Long id,
                                                                                      @RequestParam("status") TaskCompletionStatus status) {
        return ResponseEntity
                .ok(BaseSuccessResponse.<TaskUserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskUserService.answerCompletionTask(id,status))
                        .build()
                );
    }
}
