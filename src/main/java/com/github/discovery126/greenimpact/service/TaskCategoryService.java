package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.TaskCategoryResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.repository.TaskCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {

    final private TaskCategoryRepository taskCategoryRepository;

    public List<TaskCategoryResponse> getAllCategories() {
        return taskCategoryRepository.findAll()
                .stream()
                .map(category -> TaskCategoryResponse.builder()
                        .id(category.getId())
                        .categoryName(category.getCategoryName())
                        .build()
                ).
                toList();
    }
    public TaskCategory getTaskCategory(Integer taskCategoryId) {
        Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(taskCategoryId);
        if (taskCategory.isEmpty()) {
            throw new CustomException(ValidationConstants.TASK_CATEGORY_NOT_FOUND);
        }
        return taskCategory.get();
    }
}
