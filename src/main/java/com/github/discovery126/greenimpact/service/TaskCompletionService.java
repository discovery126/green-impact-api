package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.dto.request.CompleteTaskRequest;
import com.github.discovery126.greenimpact.model.TakenTask;
import com.github.discovery126.greenimpact.model.TaskCompletion;
import com.github.discovery126.greenimpact.model.TaskCompletionStatus;
import com.github.discovery126.greenimpact.model.TaskProof;
import com.github.discovery126.greenimpact.repository.TaskCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCompletionService {
    private final TaskCompletionRepository taskCompletionRepository;
    private final TakenTaskService takenTaskService;

    public void saveCompletion(CompleteTaskRequest completeTaskRequest,
                               Long userId,
                               Long taskId,
                               List<String> fileUrls
    ) {
        TakenTask takenTask = takenTaskService.getTaskByUserIdAndTaskId(userId,taskId);

        TaskCompletion taskCompletion = TaskCompletion.builder()
                .description(completeTaskRequest.getDescription())
                .takenTask(takenTask)
                .status(TaskCompletionStatus.PENDING)
                .build();

        List<TaskProof> taskProofs = fileUrls.stream()
                .map(file -> TaskProof.builder()
                        .taskCompletion(taskCompletion)
                        .imageUrl(file)
                        .build())
                .toList();

        taskCompletion.setTaskProof(taskProofs);

        taskCompletionRepository.save(taskCompletion);
    }
}
