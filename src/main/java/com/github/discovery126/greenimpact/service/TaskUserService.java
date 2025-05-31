package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.response.TaskUserResponse;
import com.github.discovery126.greenimpact.exception.CustomException;
import com.github.discovery126.greenimpact.exception.ValidationConstants;
import com.github.discovery126.greenimpact.mapper.TaskUserMapper;
import com.github.discovery126.greenimpact.model.*;
import com.github.discovery126.greenimpact.repository.TaskRepository;
import com.github.discovery126.greenimpact.repository.TaskUserRepository;
import com.github.discovery126.greenimpact.repository.UserRepository;
import com.github.discovery126.greenimpact.security.SecuritySessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskUserService {
    private final TaskUserRepository taskUserRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final SecuritySessionContext securitySessionContext;
    private final TaskUserMapper taskUserMapper;

    public void takeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ValidationConstants.TASK_ID_NOT_FOUND));


        if (task.getTaskType() == TaskType.LIMITED &&
                taskUserRepository.existsCompletedByUserIdAndTaskId(securitySessionContext.getId(), task.getId())) {
            throw new CustomException(ValidationConstants.TASK_ALREADY_TAKEN);
        }

        if (task.getTaskType() == TaskType.DAILY &&
                taskUserRepository.existsTodayByUserIdAndTaskId(securitySessionContext.getId(), task.getId(), LocalDateTime.now())) {
            throw new CustomException(ValidationConstants.TASK_ALREADY_TAKEN_TODAY);
        }

        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new CustomException(ValidationConstants.USER_NOT_FOUND));

        taskUserRepository.save(TaskUser.builder()
                .user(user)
                .task(task)
                .build()
        );
    }
    public void saveCompletion(String comment,
                               Long taskId,
                               List<String> fileUrls
    ) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ValidationConstants.TASK_ID_NOT_FOUND));
        User user = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new CustomException(ValidationConstants.USER_NOT_FOUND));

        TaskUser taskUser = null;
        if (task.getTaskType() == TaskType.LIMITED) {
            if (taskUserRepository.existsCompletedByUserIdAndTaskId(securitySessionContext.getId(), taskId)) {
                throw new CustomException(ValidationConstants.TASK_ALREADY_COMPLETED);
            }
            taskUser = taskUserRepository.findByUserIdAndTaskId(securitySessionContext.getId(), taskId);

        }
        if (task.getTaskType() == TaskType.DAILY) {
            if (taskUserRepository.existsTodayCompletedByUserAndTaskId(securitySessionContext.getId(), taskId)) {
                throw new CustomException(ValidationConstants.TASK_ALREADY_COMPLETED_TODAY);
            }
            taskUser = taskUserRepository.findUncompletedByUserAndTask(securitySessionContext.getId(), taskId);
        }
        if (taskUser == null) {
            throw new CustomException(ValidationConstants.TASK_ID_NOT_FOUND);
        }
        TaskUser taskUserEntity = TaskUser.builder()
                .id(taskUser.getId())
                .takenAt(taskUser.getTakenAt())
                .user(user)
                .task(task)
                .status(TaskCompletionStatus.PENDING)
                .completedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow")))
                .description(comment)
                .build();

        List<TaskProof> taskProofs = fileUrls.stream()
                .map(file -> TaskProof.builder()
                        .taskUser(taskUserEntity)
                        .imageUrl(file)
                        .build())
                .toList();

        taskUserEntity.setTaskProofs(taskProofs);
        taskUserRepository.save(taskUserEntity);
    }

    public List<TaskUserResponse> getUserCompletionsTasks() {
        return taskUserRepository.findAllCompletedByUserId(securitySessionContext.getId())
                .stream()
                .map(taskUserMapper::toResponse)
                .toList();
    }

    public Page<TaskUserResponse> getAllTaskCompletion(int page,int size, String sort) {

        Sort sortOrder = Sort.by(sort.split(",")[0]);
        if (sort.split(",")[1].equalsIgnoreCase("desc")) {
            sortOrder = sortOrder.descending();
        } else {
            sortOrder = sortOrder.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        return taskUserRepository.findAllCompletionTasks(pageable)
                .map(taskUserMapper::toResponse);
    }

    public TaskUserResponse answerCompletionTask(Long taskCompletionId, TaskCompletionStatus status) {
        TaskUser taskUser = taskUserRepository.findById(taskCompletionId)
                .orElseThrow(() -> new CustomException(ValidationConstants.TASK_COMPLETED_NOT_FOUND));
        User admin = userRepository.findById(securitySessionContext.getId())
                .orElseThrow(() -> new CustomException(ValidationConstants.ADMIN_NOT_FOUND));

        if (taskUser.getAdmin() != null) {
            throw new CustomException(ValidationConstants.TASK_ALREADY_TAKEN_FOR_CHECK);
        }
        if (taskUser.getStatus().equals(TaskCompletionStatus.CONFIRMED) || taskUser.getStatus().equals(TaskCompletionStatus.REJECTED)) {
            throw new CustomException(ValidationConstants.TASK_ALREADY_ANSWERED);
        }
        taskUser.setStatus(status);
        taskUser.setAdmin(admin);
        taskUser.setVerifiedAt(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        taskUser.getUser().setPoints(taskUser.getUser().getPoints() + taskUser.getTask().getPoints());
        return taskUserMapper.toResponse(taskUserRepository.save(taskUser));
    }
}
