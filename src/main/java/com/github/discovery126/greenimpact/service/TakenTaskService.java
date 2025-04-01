package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.model.TakenTask;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.model.User;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TakenTaskService {
    private final TakenTaskRepository takenTaskRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final SecuritySessionContext securitySessionContext;

    public void takeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Задание с id %d не найдено".formatted(taskId)));


        if (task.getTaskType() == TaskType.LIMITED &&
                takenTaskRepository.existsByUserIdAndTaskId(securitySessionContext.getId(), task.getId())) {
            throw new TaskAlreadyTakenException("Такое задание уже взято");
        }

        if (task.getTaskType() == TaskType.DAILY &&
                takenTaskRepository.existsByUserIdAndTaskIdAndTakenAt(securitySessionContext.getId(), task.getId(), LocalDate.now())) {
            throw new TaskAlreadyTakenException("Такое задание уже взято сегодня");
        }

        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id %d не найден"
                        .formatted(securitySessionContext.getId())));

        takenTaskRepository.save(TakenTask.builder()
                .user(user)
                .task(task)
                .build()
        );

    }
    public void deleteTakenTask(TakenTask takenTask) {
        takenTaskRepository.delete(takenTask);
    }
    public TakenTask getTaskByUserIdAndTaskIdDaily(Long userId, Long taskId) {
        return takenTaskRepository.findByUserIdAndTaskIdAndTakenAt(userId,taskId,LocalDate.now());
    }
    public TakenTask getTaskByUserIdAndTaskId(Long userId, Long taskId) {
        return takenTaskRepository.findByUserIdAndTaskId(userId,taskId);
    }
}
