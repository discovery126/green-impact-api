package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.model.TakenTask;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TakenTaskService {
    private final TakenTaskRepository takenTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final SecuritySessionContext securitySessionContext;

    public void takeTask(Long taskId) {
        if (takenTaskRepository.existsByUserIdAndTaskId(securitySessionContext.getId(),taskId)) {
            throw new TaskAlreadyTakenException("Такое задание уже взято");
        }
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(securitySessionContext.getId())));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Задание с id %d не найдено".formatted(taskId)));
        takenTaskRepository.save(TakenTask.builder()
                .user(user)
                .task(task)
                .build()
        );

    }

    public TakenTask getTaskByUserIdAndTaskId(Long userId, Long taskId) {
        return takenTaskRepository.findByUserIdAndTaskId(userId,taskId);
    }
}
