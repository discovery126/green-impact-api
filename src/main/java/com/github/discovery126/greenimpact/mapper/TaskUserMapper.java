package com.github.discovery126.greenimpact.mapper;

import com.github.discovery126.greenimpact.dto.response.TaskProofResponse;
import com.github.discovery126.greenimpact.dto.response.TaskUserResponse;
import com.github.discovery126.greenimpact.model.TaskUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskUserMapper {
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public TaskUserResponse toResponse(TaskUser taskUser) {
        return TaskUserResponse.builder()
                .id(taskUser.getId())
                .userResponse(userMapper.toResponse(taskUser.getUser()))
                .taskResponse(taskMapper.toResponse(taskUser.getTask()))
                .adminResponse(userMapper.toResponse(taskUser.getAdmin()))
                .status(taskUser.getStatus().name())
                .takenAt(taskUser.getTakenAt())
                .completedAt(taskUser.getCompletedAt())
                .verifiedAt(taskUser.getVerifiedAt())
                .taskProof(taskUser.getTaskProofs()
                        .stream()
                        .map(proof -> TaskProofResponse.builder().id(proof.getId())
                                .imageUrl(proof.getImageUrl()).build())
                        .toList())
                .build();
    }
}
