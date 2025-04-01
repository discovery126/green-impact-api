package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.TaskAlreadyTakenException;
import com.github.discovery126.greenimpact.model.TakenTask;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.repository.TakenTaskRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskValidationService {
    private final TakenTaskRepository takenTaskRepository;
    private final SecuritySessionContext securitySessionContext;

    public void validateTaskTaken(Task task,String message) {
        Long userId = securitySessionContext.getId();

        if (task.getTaskType() == TaskType.LIMITED &&
                takenTaskRepository.existsByUserIdAndTaskId(userId, task.getId())) {
            throw new TaskAlreadyTakenException(message);
        }

        if (task.getTaskType() == TaskType.DAILY &&
                takenTaskRepository.existsByUserIdAndTaskIdAndTakenAt(userId, task.getId(), LocalDate.now())) {
            TakenTask takenTask = takenTaskRepository.findByUserIdAndTaskIdAndTakenAt(userId, task.getId(), LocalDate.now());
            throw new TaskAlreadyTakenException(message.concat(" сегодня"));
        }
    }
}