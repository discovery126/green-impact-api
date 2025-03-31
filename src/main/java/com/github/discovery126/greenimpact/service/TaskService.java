package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.exception.TaskNotFoundException;
import com.github.discovery126.greenimpact.exception.UserNotFoundException;
import com.github.discovery126.greenimpact.mapper.TaskMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.discovery126.greenimpact.utils.UpdateUtils.updateFieldIfChanged;

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

    public TaskResponse updateTask(TaskRequest taskRequest, Long taskId) {

        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty())
            throw new TaskNotFoundException("Задание с id %d не найдено".formatted(taskId));
        Task task = taskOptional.get();

        TaskCategory taskCategory = taskCategoryService.getTaskCategory(taskRequest.getCategoryId());
        TaskType taskType = TaskType.valueOf(taskRequest.getTaskType().toUpperCase());

        updateFieldIfChanged(task.getTitle(),taskRequest.getTitle(), task::setTitle);
        updateFieldIfChanged(task.getDescription(),taskRequest.getDescription(), task::setDescription);
        updateFieldIfChanged(task.getPoints(),taskRequest.getPoints(), task::setPoints);
        updateFieldIfChanged(task.getStartDate(),taskRequest.getStartDate(), task::setStartDate);
        updateFieldIfChanged(task.getEndDate(),taskRequest.getEndDate(), task::setEndDate);
        updateFieldIfChanged(task.getTaskCategory(),taskCategory, task::setTaskCategory);
        updateFieldIfChanged(task.getTaskType(),taskType, task::setTaskType);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new UserNotFoundException("Задание с id %d не найдено".formatted(taskId));
        }
    }
}
