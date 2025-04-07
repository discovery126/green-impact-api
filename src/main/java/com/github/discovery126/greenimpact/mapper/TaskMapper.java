package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.TaskCategoryResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .points(task.getPoints())
                .taskCategory(TaskCategoryResponse.builder()
                        .id(task.getTaskCategory().getId())
                        .categoryName(task.getTaskCategory().getCategoryName())
                        .build())
                .taskType(task.getTaskType())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .build();
    }
}
