package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionResponse {
    private long id;

    private String description;

    private String status;

    @JsonProperty("completed_at")
    private LocalDateTime completedAt;

    @JsonProperty("verified_at")
    private LocalDateTime verifiedAt;

    @JsonProperty("task")
    private TaskResponse taskResponse;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonProperty("admin")
    private UserResponse adminResponse;

    @JsonProperty("taskProofs")
    private List<TaskProofResponse> taskProof;
}
