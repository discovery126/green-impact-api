package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.mapper.TaskMapper;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.github.discovery126.greenimpact.utils.UpdateUtils.updateFieldIfChanged;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskCategoryService taskCategoryService;
    private final SecuritySessionContext securitySessionContext;

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAllByOrderByTaskTypeDescPointsDesc()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
    public List<TaskResponse> getAllTasksForUsers() {
        return taskRepository.findAllTasks()
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
                .expiredDate(taskRequest.getExpiredDate())
                .build();
        return taskMapper.toResponse(taskRepository.save(task));
    }

    public TaskResponse updateTask(TaskRequest taskRequest, Long taskId) {

        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty())
            throw new CustomException(ValidationConstants.TASK_ID_NOT_FOUND);
        Task task = taskOptional.get();

        TaskCategory taskCategory = taskCategoryService.getTaskCategory(taskRequest.getCategoryId());
        TaskType taskType = TaskType.valueOf(taskRequest.getTaskType().toUpperCase());

        updateFieldIfChanged(task.getTitle(),taskRequest.getTitle(), task::setTitle);
        updateFieldIfChanged(task.getDescription(),taskRequest.getDescription(), task::setDescription);
        updateFieldIfChanged(task.getPoints(),taskRequest.getPoints(), task::setPoints);
        updateFieldIfChanged(task.getExpiredDate(),taskRequest.getExpiredDate(), task::setExpiredDate);
        updateFieldIfChanged(task.getTaskCategory(),taskCategory, task::setTaskCategory);
        updateFieldIfChanged(task.getTaskType(),taskType, task::setTaskType);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new CustomException(ValidationConstants.TASK_ID_NOT_FOUND);
        }
    }

    public List<TaskResponse> getTasksForCurrentUser(Long userId) {
        return taskRepository.findAllAvailableTasksForUser(userId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public List<TaskResponse> getActiveTasks() {
        return taskRepository.findAllUncompletedActiveTasksByUser(securitySessionContext.getId())
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }
}
