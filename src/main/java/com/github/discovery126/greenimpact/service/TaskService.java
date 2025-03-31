package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.mapper.TaskMapper;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskCategoryService taskCategoryService;

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        TaskCategory taskCategory = taskCategoryService.getTaskCategory(taskRequest.getCategoryId());
        TaskType taskType = TaskType.valueOf(taskRequest.getTaskType().toUpperCase());
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .points(taskRequest.getPoints())
                .taskType(taskType)
                .taskCategory(taskCategory)
                .startDate(taskRequest.getStartDate())
                .endDate(taskRequest.getEndDate())
                .build();
        return taskMapper.toResponse(taskRepository.save(task));
    }
}
