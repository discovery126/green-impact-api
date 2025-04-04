package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.exception.TaskCategoryNotFoundException;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {

    final private TaskCategoryRepository taskCategoryRepository;
    public TaskCategory getTaskCategory(Integer taskCategoryId) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(taskCategoryId);
        if (taskCategory.isEmpty()) {
            throw new TaskCategoryNotFoundException("Категория задания с id %d не найдена".formatted(taskCategoryId));
        }
        return taskCategory.get();
    }
}
