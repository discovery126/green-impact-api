package com.github.discovery126.greenimpact.controller;

import com.github.discovery126.greenimpact.dto.response.BaseSuccessResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.FileStorageException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.service.S3Service;
import com.github.discovery126.greenimpact.service.TaskService;
import com.github.discovery126.greenimpact.service.TaskUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/user/tasks")
@PreAuthorize("hasAuthority('USER')")
@RequiredArgsConstructor
public class UserTaskController {

    private final TaskUserService taskUserService;
    private final S3Service s3Service;

    @PostMapping("/{taskId}/take")
    public ResponseEntity<Void> takeTask(@PathVariable Long taskId) {
        taskUserService.takeTask(taskId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/{taskId}/submit")
    public ResponseEntity<Void> submitTask(@RequestPart("photos") List<MultipartFile> photos,
                                           @RequestPart(value = "comment", required = false) String comment,
                                           @PathVariable Long taskId) {
        if (comment != null && comment.length() > 255) {
            throw new CustomException(ValidationConstants.TASK_COMMENT_NOT_VALID);
        }
        try {
            s3Service.uploadFile(photos, taskId, comment);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        } catch (IOException e) {
            // TODO: Обработать IOException в Custom Exception Handler
            throw new FileStorageException("Неизвестная ошибка с файлами");
        }
    }
}
