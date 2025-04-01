package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.exception.FileStorageException;
import com.github.discovery126.greenimpact.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final TakenTaskService takenTaskService;
    private final S3Service s3Service;
    private final RewardService rewardService;
    private final EventService eventService;


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity
                .ok(userService.getUser());
    }
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
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/tasks/{taskId}/take")
    public ResponseEntity<Void> takeTask(@PathVariable Long taskId) {
        takenTaskService.takeTask(taskId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Void> uploadFile(@RequestPart("photos") List<MultipartFile> photos,
                                                   @RequestPart("description") @Valid CompleteTaskRequest completeTaskRequest,
                                                   @PathVariable Long taskId) {
        try {
            s3Service.uploadFile(photos,taskId,completeTaskRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (IOException e) {
            throw new FileStorageException("Неизвестная ошибка с файлами");
        }
    }
    // Rewards

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("rewards/{rewardId}/claim")
    public ResponseEntity<Void> claimReward(@PathVariable Long rewardId) {
        rewardService.claimReward(rewardId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("events/{eventId}/register")
    public ResponseEntity<Void> registerEvent(@PathVariable Long eventId) {
        eventService.registerEvent(eventId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
