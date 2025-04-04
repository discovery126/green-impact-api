package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.dto.response.*;
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
    private final UserEventService userEventService;
    private final UserRewardService userRewardService;
    private final TaskCompletionService taskCompletionService;


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity
                .ok(userService.getUser());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/events")
    public ResponseEntity<List<UserEventResponse>> getUserEvents() {
        return ResponseEntity
                .ok(userEventService.getUserEvents());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/rewards")
    public ResponseEntity<List<UserRewardResponse>> getUserRewards() {
        return ResponseEntity
                .ok(userRewardService.getUserRewards());
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/completed-tasks")
    public ResponseEntity<List<TaskCompletionResponse>> getCompletedTasks() {
        return ResponseEntity
                .ok(taskCompletionService.getUserTaskCompletion());
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<TaskResponse>> getTasks() {
        return ResponseEntity
                .ok(userService.getUserTasks());
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
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("events/{eventId}/confirm")
    public ResponseEntity<UserEventResponse> confirmEvent(@PathVariable Long eventId,
                                                          @RequestParam String eventCode) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.confirmEvent(eventId,eventCode));
    }
}
