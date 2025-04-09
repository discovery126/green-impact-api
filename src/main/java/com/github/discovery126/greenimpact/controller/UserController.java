package com.github.discovery126.greenimpact.controller;


import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.*;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.FileStorageException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.service.*;
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
    private final TaskUserService taskUserService;
    private final S3Service s3Service;
    private final RewardService rewardService;
    private final EventService eventService;
    private final UserEventService userEventService;
    private final UserRewardService userRewardService;


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ResponseEntity<BaseSuccessResponse<UserResponse>> getUser() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<UserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.getUser())
                        .build()
                );
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/events")
    public ResponseEntity<BaseSuccessResponse<List<UserEventResponse>>> getUserEvents() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<UserEventResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userEventService.getUserEvents())
                        .build()
                );
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/rewards")
    public ResponseEntity<BaseSuccessResponse<List<UserRewardResponse>>> getUserRewards() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<UserRewardResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userRewardService.getUserRewards())
                        .build()
                );
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/completed-tasks")
    public ResponseEntity<BaseSuccessResponse<List<TaskUserResponse>>> getCompletedTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskUserResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskUserService.getUserCompletionsTasks())
                        .build()
                );
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(userService.getUserTasks())
                        .build()
                );
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/active-tasks")
    public ResponseEntity<BaseSuccessResponse<List<TaskResponse>>> getActiveTasks() {
        return ResponseEntity
                .ok(BaseSuccessResponse.<List<TaskResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(taskService.getActiveTasks())
                        .build()
                );
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/tasks/{taskId}/take")
    public ResponseEntity<Void> takeTask(@PathVariable Long taskId) {
        taskUserService.takeTask(taskId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/tasks/{taskId}/submit")
    public ResponseEntity<Void> uploadFile(@RequestPart("photos") List<MultipartFile> photos,
                                                   @RequestPart(value = "comment",required = false) String comment,
                                                   @PathVariable Long taskId) {
        if (comment != null && comment.length() > 255) {
            throw new CustomException(ValidationConstants.TASK_COMMENT_NOT_VALID);
        }
        try {
            s3Service.uploadFile(photos,taskId,comment);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (IOException e) {
            throw new FileStorageException("Неизвестная ошибка с файлами");
        }
    }
    // Rewards

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("rewards/{rewardId}/exchange")
    public ResponseEntity<Void> exchangeReward(@PathVariable Long rewardId) {
        rewardService.exchangeReward(rewardId);
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
        eventService.confirmEvent(eventId,eventCode);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
