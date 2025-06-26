package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.TaskRequest;
import com.github.discovery126.greenimpact.dto.response.TaskCategoryResponse;
import com.github.discovery126.greenimpact.dto.response.TaskResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.mapper.TaskMapper;
import com.github.discovery126.greenimpact.model.Task;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private SecuritySessionContext securitySessionContext;

    @Mock
    private TaskCategoryService taskCategoryService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldReturnMappedTasksForCurrentUser() {
        Long userId = 42L;

        List<Task> tasksFromDb = List.of(
                Task.builder()
                        .id(7L)
                        .title("Сдача аллюминия")
                        .description("Сдай 1 кг алюминния и загрузи фото")
                        .points(40)
                        .taskType(TaskType.DAILY)
                        .taskCategory(TaskCategory.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build(),
                Task.builder()
                        .id(6L)
                        .title("Сдача бумаги")
                        .description("Сдай 1 кг бумаги и загрузи скриншот")
                        .points(30)
                        .taskType(TaskType.LIMITED)
                        .expiredDate(LocalDateTime.of(2025,4,8,20,0,0))
                        .taskCategory(TaskCategory.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build()
        );

        List<TaskResponse> mappedResponses = List.of(
                TaskResponse.builder()
                        .id(7L)
                        .title("Сдача аллюминия")
                        .description("Сдай 1 кг алюминния и загрузи фото")
                        .points(40)
                        .taskType(TaskType.DAILY)
                        .taskCategory(TaskCategoryResponse.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build(),
                TaskResponse.builder()
                        .id(6L)
                        .title("Сдача бумаги")
                        .description("Сдай 1 кг бумаги и загрузи скриншот")
                        .points(30)
                        .taskType(TaskType.LIMITED)
                        .expiredDate(LocalDateTime.of(2025,4,8,20,0,0))
                        .taskCategory(TaskCategoryResponse.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build()
        );

        when(taskRepository.findAllAvailableTasksForUser(userId)).thenReturn(tasksFromDb);

        when(taskMapper.toResponse(tasksFromDb.get(0))).thenReturn(mappedResponses.get(0));
        when(taskMapper.toResponse(tasksFromDb.get(1))).thenReturn(mappedResponses.get(1));

        List<TaskResponse> actual = taskService.getTasksForCurrentUser(userId);

        assertEquals(mappedResponses, actual);

        verify(taskRepository).findAllAvailableTasksForUser(userId);
        verify(taskMapper, times(tasksFromDb.size())).toResponse(any(Task.class));
    }

    @Test
    void shouldReturnActiveTasksForCurrentUser() {
        Long userId = 42L;

        List<Task> tasksFromDb = List.of(
                Task.builder()
                        .id(7L)
                        .title("Сдача аллюминия")
                        .description("Сдай 1 кг алюминния и загрузи фото")
                        .points(40)
                        .taskType(TaskType.DAILY)
                        .taskCategory(TaskCategory.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build(),
                Task.builder()
                        .id(6L)
                        .title("Сдача бумаги")
                        .description("Сдай 1 кг бумаги и загрузи доказательство")
                        .points(30)
                        .taskType(TaskType.LIMITED)
                        .expiredDate(LocalDateTime.of(2025,4,8,20,0,0))
                        .taskCategory(TaskCategory.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build()
        );

        List<TaskResponse> expectedResponses = List.of(
                TaskResponse.builder()
                        .id(7L)
                        .title("Сдача аллюминия")
                        .description("Сдай 1 кг алюминния и загрузи фото")
                        .points(40)
                        .taskType(TaskType.DAILY)
                        .taskCategory(TaskCategoryResponse.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build(),
                TaskResponse.builder()
                        .id(6L)
                        .title("Сдача бумаги")
                        .description("Сдай 1 кг бумаги и загрузи доказательство")
                        .points(30)
                        .taskType(TaskType.LIMITED)
                        .expiredDate(LocalDateTime.of(2025,4,8,20,0,0))
                        .taskCategory(TaskCategoryResponse.builder()
                                .id(1)
                                .categoryName("Сдача")
                                .build())
                        .build()
        );
        when(securitySessionContext.getId()).thenReturn(userId);
        when(taskRepository.findAllUncompletedActiveTasksByUser(userId)).thenReturn(tasksFromDb);

        when(taskMapper.toResponse(tasksFromDb.get(0))).thenReturn(expectedResponses.get(0));
        when(taskMapper.toResponse(tasksFromDb.get(1))).thenReturn(expectedResponses.get(1));

        List<TaskResponse> actualResponses = taskService.getActiveTasks();

        assertEquals(expectedResponses, actualResponses);

        verify(securitySessionContext).getId();
        verify(taskRepository).findAllUncompletedActiveTasksByUser(userId);
        verify(taskMapper, times(tasksFromDb.size())).toResponse(any(Task.class));
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // given
        Long taskId = 1L;
        TaskRequest request = TaskRequest.builder()
                .title("Сдача аллюминия")
                .description("Сдай 1 кг алюминния и загрузи фото")
                .taskType("DAILY")
                .expiredDate(LocalDateTime.now().plusDays(3))
                .points(40)
                .categoryId(1)
                .build();

        TaskCategory category = new TaskCategory(1,"Сдача");
        Task taskToSave = Task.builder()
                .id(taskId)
                .title("Сдача аллюминия")
                .description("Сдай 1 кг алюминния и загрузи фото")
                .taskType(TaskType.DAILY)
                .expiredDate(LocalDateTime.now().plusDays(3))
                .points(40)
                .taskCategory(category)
                .build();

        Task savedTask = Task.builder()
                .id(taskId)
                .title(taskToSave.getTitle())
                .description(taskToSave.getDescription())
                .points(taskToSave.getPoints())
                .taskType(taskToSave.getTaskType())
                .taskCategory(taskToSave.getTaskCategory())
                .expiredDate(taskToSave.getExpiredDate())
                .build();

        TaskResponse response = TaskResponse.builder()
                .id(taskId)
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .points(savedTask.getPoints())
                .taskType(savedTask.getTaskType())
                .taskCategory(TaskCategoryResponse.builder()
                        .id(savedTask.getTaskCategory().getId())
                        .categoryName(savedTask.getTaskCategory().getCategoryName())
                        .build())
                .expiredDate(savedTask.getExpiredDate())
                .build();

        when(taskCategoryService.getTaskCategory(1)).thenReturn(category);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.toResponse(savedTask)).thenReturn(response);

        // when
        TaskResponse result = taskService.createTask(request);

        // then
        assertEquals(response, result);
        verify(taskCategoryService).getTaskCategory(1);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toResponse(savedTask);
    }
    @Test
    void shouldUpdateTaskSuccessfully() {
        // given
        Long taskId = 1L;
        TaskRequest request = TaskRequest.builder()
                .title("Эко-урок в школе")
                .description("Проведите или посетите урок по экологии. Расскажите о переработке, загрязнении и методах защиты природы. Воспитание сознательности — важная часть устойчивого будущего.")
                .points(15)
                .taskType("LIMITED")
                .expiredDate(LocalDateTime.now().plusDays(5))
                .categoryId(2)
                .build();

        TaskCategory category = new TaskCategory(2,"Образование");
        Task oldTask = Task.builder()
                .id(taskId)
                .title("Сдача аллюминия")
                .description("Сдай 1 кг алюминния и загрузи фото")
                .taskType(TaskType.DAILY)
                .points(40)
                .taskCategory(category)
                .build();

        Task updatedTask = Task.builder()
                .id(taskId)
                .title("Эко-урок в школе")
                .description("Проведите или посетите урок по экологии. Расскажите о переработке, загрязнении и методах защиты природы. Воспитание сознательности — важная часть устойчивого будущего.")
                .points(15)
                .taskType(TaskType.LIMITED)
                .expiredDate(LocalDateTime.now().plusDays(5))
                .taskCategory(category)
                .build();

        TaskResponse response = TaskResponse.builder()
                .id(taskId)
                .title(updatedTask.getTitle())
                .description(updatedTask.getDescription())
                .points(updatedTask.getPoints())
                .taskType(updatedTask.getTaskType())
                .taskCategory(TaskCategoryResponse.builder()
                        .id(updatedTask.getTaskCategory().getId())
                        .categoryName(updatedTask.getTaskCategory().getCategoryName())
                        .build())
                .expiredDate(updatedTask.getExpiredDate())
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));
        when(taskCategoryService.getTaskCategory(2)).thenReturn(category);
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toResponse(updatedTask)).thenReturn(response);

        // when
        TaskResponse result = taskService.updateTask(request, taskId);

        // then
        assertEquals(response, result);
        verify(taskRepository).findById(taskId);
        verify(taskCategoryService).getTaskCategory(2);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toResponse(updatedTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskToUpdateNotFound() {
        // given
        Long taskId = 1L;
        TaskRequest request = new TaskRequest();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // when / then
        CustomException exception = assertThrows(CustomException.class, () ->
                taskService.updateTask(request, taskId)
        );
        assertEquals(ValidationConstants.TASK_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // when
        taskService.deleteTask(taskId);

        // then
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void shouldThrowExceptionWhenTaskToDeleteNotFound() {
        // given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // when / then
        CustomException exception = assertThrows(CustomException.class, () ->
                taskService.deleteTask(taskId)
        );
        assertEquals(ValidationConstants.TASK_ID_NOT_FOUND, exception.getMessage());
    }
}
