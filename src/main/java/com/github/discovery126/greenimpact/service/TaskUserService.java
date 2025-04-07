package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskUserResponse;
import com.github.discovery126.greenimpact.exception.TaskAlreadyAnsweredException;
import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.exception.TaskNotFoundException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.mapper.TaskUserMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.TaskUserRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskUserService {
    private final TaskUserRepository taskUserRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final SecuritySessionContext securitySessionContext;
    private final TaskUserMapper taskUserMapper;

    public void takeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Задание с id %d не найдено".formatted(taskId)));


        if (task.getTaskType() == TaskType.LIMITED &&
                taskUserRepository.existsCompletedByUserIdAndTaskId(securitySessionContext.getId(), task.getId())) {
            throw new TaskAlreadyTakenException("Такое задание уже взято");
        }

        if (task.getTaskType() == TaskType.DAILY &&
                taskUserRepository.existsTodayByUserIdAndTaskId(securitySessionContext.getId(), task.getId(), LocalDateTime.now())) {
            throw new TaskAlreadyTakenException("Такое задание уже взято сегодня");
        }

        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(securitySessionContext.getId())));

        taskUserRepository.save(TaskUser.builder()
                .user(user)
                .task(task)
                .build()
        );
    }
    public void saveCompletion(CompleteTaskRequest completeTaskRequest,
                               Long taskId,
                               List<String> fileUrls
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задание с id %d не найдено".formatted(taskId)));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(securitySessionContext.getId())));

        TaskUser taskUser = null;
        if (task.getTaskType() == TaskType.LIMITED) {
            if (taskUserRepository.existsCompletedByUserIdAndTaskId(securitySessionContext.getId(), taskId)) {
                throw new TaskAlreadyTakenException("Такое задание уже выполнено");
            }
            taskUser = taskUserRepository.findByUserIdAndTaskId(securitySessionContext.getId(), taskId);

        }
        if (task.getTaskType() == TaskType.DAILY) {
            if (taskUserRepository.existsTodayCompletedByUserAndTaskId(securitySessionContext.getId(), taskId)) {
                throw new TaskAlreadyTakenException("Такое задание уже выполнено сегодня");
            }
            taskUser = taskUserRepository.findUncompletedByUserAndTask(securitySessionContext.getId(), taskId);
        }
        if (taskUser == null) {
            throw new TaskNotFoundException("Задание с id %d не найдено".formatted(taskId));
        }
        TaskUser taskUserEntity = TaskUser.builder()
                .id(taskUser.getId())
                .takenAt(taskUser.getTakenAt())
                .user(user)
                .task(task)
                .status(TaskCompletionStatus.PENDING)
                .completedAt(LocalDateTime.now())
                .description(completeTaskRequest.getDescription())
                .build();

        List<TaskProof> taskProofs = fileUrls.stream()
                .map(file -> TaskProof.builder()
                        .taskUser(taskUserEntity)
                        .imageUrl(file)
                        .build())
                .toList();

        taskUserEntity.setTaskProofs(taskProofs);
        taskUserRepository.save(taskUserEntity);
    }

    public List<TaskUserResponse> getUserCompletionsTasks() {
        return taskUserRepository.findAllCompletedByUserId(securitySessionContext.getId())
                .stream()
                .map(taskUserMapper::toResponse)
                .toList();
    }

    public List<TaskUserResponse> getAllTaskCompletion() {
        return taskUserRepository.findAllCompletionTasks()
                .stream()
                .map(taskUserMapper::toResponse)
                .toList();
    }

    public TaskUserResponse answerCompletionTask(Long taskCompletionId, TaskCompletionStatus status) {
        TaskUser taskUser = taskUserRepository.findById(taskCompletionId)
                .orElseThrow(() -> new TaskNotFoundException("Выполненное задание с id %d не найдено"
                        .formatted(taskCompletionId)));

        User admin = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Админ с таким id %d не найден"
                        .formatted(securitySessionContext.getId())));
        if (taskUser.getAdmin() != null && !taskUser.getAdmin().equals(admin)) {
            throw new TaskAlreadyTakenException("Другой админ занимается этой проверкой");
        }
        if (taskUser.getStatus() == TaskCompletionStatus.CONFIRMED) {
            throw new TaskAlreadyAnsweredException("Это задание уже проверенно");
        }
        taskUser.setStatus(status);
        taskUser.setAdmin(admin);
        taskUser.setVerifiedAt(LocalDateTime.now());
        taskUser.getUser().setPoints(taskUser.getUser().getPoints() + taskUser.getTask().getPoints());
        return taskUserMapper.toResponse(taskUserRepository.save(taskUser));
    }
}
