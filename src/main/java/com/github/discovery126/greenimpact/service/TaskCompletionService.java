package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.repository.TaskCompletionRepository;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCompletionService {
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TakenTaskRepository takenTaskRepository;

    public void saveCompletion(CompleteTaskRequest completeTaskRequest,
                               Long userId,
                               Long taskId,
                               List<String> fileUrls
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Задание с id %d не найдено".formatted(taskId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(userId)));

        if (task.getTaskType() == TaskType.LIMITED &&
                takenTaskRepository.existsByUserIdAndTaskId(userId, taskId)) {
            throw new TaskAlreadyTakenException("Такое задание уже выполнено");
        }

        if (task.getTaskType() == TaskType.DAILY &&
                taskCompletionRepository.existsByUserIdAndTaskIdAndDate(userId, taskId)) {
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

        taskCompletion.setTaskProof(taskProofs);

        taskCompletionRepository.save(taskCompletion);
    }
}
