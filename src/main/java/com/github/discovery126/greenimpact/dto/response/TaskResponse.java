package com.github.discovery126.greenimpact.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.model.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private long id;

    private String title;

    private String description;

    @JsonProperty("task_type")
    private TaskType taskType;

    @JsonProperty("expired_date")
    private LocalDateTime expiredDate;

    private Integer points;

    @JsonProperty("category")
    private TaskCategoryResponse taskCategory;
}
