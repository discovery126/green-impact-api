package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.TaskCompletionResponse;
import com.github.discovery126.greenimpact.dto.response.TaskProofResponse;
import com.github.discovery126.greenimpact.model.TaskCompletion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCompletionMapper {
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public TaskCompletionResponse toResponse(TaskCompletion taskCompletion) {
        return TaskCompletionResponse.builder()
                .id(taskCompletion.getId())
                .userResponse(userMapper.toResponse(taskCompletion.getUser()))
                .taskResponse(taskMapper.toResponse(taskCompletion.getTask()))
                .adminResponse(userMapper.toResponse(taskCompletion.getAdmin()))
                .status(taskCompletion.getStatus().name())
                .completedAt(taskCompletion.getCompletedAt())
                .verifiedAt(taskCompletion.getVerifiedAt())
                .taskProof(taskCompletion.getTaskProofs()
                        .stream()
                        .map(proof -> TaskProofResponse.builder().id(proof.getId())
                                .imageUrl(proof.getImageUrl()).build())
                        .toList())
                .build();
    }
}
