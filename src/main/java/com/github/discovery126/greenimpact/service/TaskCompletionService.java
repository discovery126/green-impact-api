package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskCompletionResponse;
import com.github.discovery126.greenimpact.exception.TaskAlreadyAnsweredException;
import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.exception.TaskNotFoundException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.mapper.TaskCompletionMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.repository.TaskCompletionRepository;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCompletionService {
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TakenTaskRepository takenTaskRepository;
    private final TaskCompletionMapper taskCompletionMapper;
    private final SecuritySessionContext securitySessionContext;


    public void saveCompletion(CompleteTaskRequest completeTaskRequest,
                               Long taskId,
                               List<String> fileUrls
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Задание с id %d не найдено".formatted(taskId)));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(securitySessionContext.getId())));

        if (task.getTaskType() == TaskType.LIMITED &&
                takenTaskRepository.existsByUserIdAndTaskId(securitySessionContext.getId(), taskId)) {
            throw new TaskAlreadyTakenException("Такое задание уже выполнено");
        }

        if (task.getTaskType() == TaskType.DAILY &&
                taskCompletionRepository.existsByUserIdAndTaskIdAndDate(securitySessionContext.getId(), taskId)) {
            throw new TaskAlreadyTakenException("Такое задание уже выполнено сегодня");
        }

        TaskCompletion taskCompletion = TaskCompletion.builder()
                .description(completeTaskRequest.getDescription())
                .user(user)
                .task(task)
                .status(TaskCompletionStatus.PENDING)
                .build();

        List<TaskProof> taskProofs = fileUrls.stream()
                .map(file -> TaskProof.builder()
                        .taskCompletion(taskCompletion)
                        .imageUrl(file)
                        .build())
                .toList();

        taskCompletion.setTaskProofs(taskProofs);

        taskCompletionRepository.save(taskCompletion);
    }

    public List<TaskCompletionResponse> getUserTaskCompletion() {
        return taskCompletionRepository.findAllByUserId(securitySessionContext.getId())
                .stream()
                .map(taskCompletionMapper::toResponse)
                .toList();
    }

    public List<TaskCompletionResponse> getAllTaskCompletion() {
        return taskCompletionRepository.findAll()
                .stream()
                .map(taskCompletionMapper::toResponse)
                .toList();
    }

    public TaskCompletionResponse answerCompletionTask(Long taskCompletionId, TaskCompletionStatus status) {
        TaskCompletion taskCompletion = taskCompletionRepository.findById(taskCompletionId)
                .orElseThrow(() -> new TaskNotFoundException("Выполненное задание с id %d не найдено"
                        .formatted(taskCompletionId)));

        User admin = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Админ с таким id %d не найден"
                        .formatted(securitySessionContext.getId())));
        if (taskCompletion.getAdmin() != null && !taskCompletion.getAdmin().equals(admin)) {
            throw new TaskAlreadyTakenException("Другой админ занимается этой проверкой");
        }
        if (taskCompletion.getStatus() == TaskCompletionStatus.CONFIRMED) {
            throw new TaskAlreadyAnsweredException("Это задание уже проверенно");
        }
        taskCompletion.setStatus(status);
        taskCompletion.setAdmin(admin);
        taskCompletion.setVerifiedAt(LocalDateTime.now());
        taskCompletion.getUser().setPoints(taskCompletion.getUser().getPoints() + taskCompletion.getTask().getPoints());
        return taskCompletionMapper.toResponse(taskCompletionRepository.save(taskCompletion));

    }
}
