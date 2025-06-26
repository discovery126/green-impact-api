package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.TaskUserResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.mapper.TaskUserMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TaskUserRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskUserServiceTest {

    @Mock
    private TaskUserRepository taskUserRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecuritySessionContext securitySessionContext;

    @Mock
    private TaskUserMapper taskUserMapper;

    @InjectMocks
    private TaskUserService taskUserService;

    @Test
    void shouldAnswerCompletionTaskSuccessfully() {
        // given
        Long taskCompletionId = 1L;
        Long userId = 2L;
        Long adminId = 3L;

        User user = new User();
        user.setId(userId);
        user.setPoints(50);

        User admin = new User();
        admin.setId(adminId);

        Task task = Task.builder()
                .id(1L)
                .title("Test")
                .points(20)
                .taskType(TaskType.DAILY)
                .expiredDate(LocalDateTime.now().plusDays(3))
                .build();

        TaskUser taskUser = TaskUser.builder()
                .id(taskCompletionId)
                .user(user)
                .task(task)
                .status(TaskCompletionStatus.PENDING)
                .build();

        TaskUser savedTaskUser = TaskUser.builder()
                .id(taskCompletionId)
                .user(user)
                .task(task)
                .status(TaskCompletionStatus.CONFIRMED)
                .admin(admin)
                .verifiedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                .build();

        when(taskUserRepository.findById(taskCompletionId)).thenReturn(Optional.of(taskUser));
        when(securitySessionContext.getId()).thenReturn(adminId);
        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(taskUserRepository.save(any(TaskUser.class))).thenReturn(savedTaskUser);
        when(taskUserMapper.toResponse(savedTaskUser)).thenReturn(new TaskUserResponse());

        // when
        TaskUserResponse result = taskUserService.answerCompletionTask(taskCompletionId, TaskCompletionStatus.CONFIRMED);

        // then
        assertNotNull(result);
        assertEquals(70, user.getPoints()); // 50 + 20
        assertEquals(TaskCompletionStatus.CONFIRMED, taskUser.getStatus());
        assertEquals(admin, taskUser.getAdmin());
        assertNotNull(taskUser.getVerifiedAt());
        verify(taskUserRepository).save(any(TaskUser.class));
    }

    @Test
    void shouldThrowExceptionIfAnotherAdminIsCheckingTheTask() {
        // given
        Long taskCompletionId = 1L;
        Long adminId = 3L;
        Long anotherAdminId = 5L;

        User currentAdmin = new User();
        currentAdmin.setId(adminId);

        User anotherAdmin = new User();
        anotherAdmin.setId(anotherAdminId);

        TaskUser taskUser = TaskUser.builder()
                .id(taskCompletionId)
                .status(TaskCompletionStatus.CONFIRMED)
                .admin(anotherAdmin)
                .build();

        when(taskUserRepository.findById(taskCompletionId)).thenReturn(Optional.of(taskUser));
        when(securitySessionContext.getId()).thenReturn(adminId);
        when(userRepository.findById(adminId)).thenReturn(Optional.of(currentAdmin));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                taskUserService.answerCompletionTask(taskCompletionId, TaskCompletionStatus.CONFIRMED)
        );

        assertEquals(ValidationConstants.TASK_ALREADY_TAKEN_FOR_CHECK, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfTaskAlreadyConfirmed() {
        // given
        Long taskCompletionId = 1L;
        Long adminId = 3L;

        User admin = new User();
        admin.setId(adminId);

        TaskUser taskUser = TaskUser.builder()
                .id(taskCompletionId)
                .status(TaskCompletionStatus.CONFIRMED)
                .build();

        when(taskUserRepository.findById(taskCompletionId)).thenReturn(Optional.of(taskUser));
        when(securitySessionContext.getId()).thenReturn(adminId);
        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                taskUserService.answerCompletionTask(taskCompletionId, TaskCompletionStatus.CONFIRMED)
        );

        assertEquals(ValidationConstants.TASK_ALREADY_ANSWERED, exception.getMessage());
    }
    @Test
    void shouldThrowExceptionIfTaskAlreadyRejected() {
        // given
        Long taskCompletionId = 1L;
        Long adminId = 3L;

        User admin = new User();
        admin.setId(adminId);

        TaskUser taskUser = TaskUser.builder()
                .id(taskCompletionId)
                .status(TaskCompletionStatus.REJECTED)
                .build();

        when(taskUserRepository.findById(taskCompletionId)).thenReturn(Optional.of(taskUser));
        when(securitySessionContext.getId()).thenReturn(adminId);
        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                taskUserService.answerCompletionTask(taskCompletionId, TaskCompletionStatus.REJECTED)
        );

        assertEquals(ValidationConstants.TASK_ALREADY_ANSWERED, exception.getMessage());
    }


}
