package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.TaskCategory;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.utils.ValidEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String title;

    private String description;

    @JsonProperty("task_type")
    @ValidEnum(enumClass = TaskType.class,message = "Неправильный тип задания")
    private TaskType taskType;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    private LocalDateTime endDate;

    private Integer points;

    @JsonProperty("category_id")
    private TaskCategory taskCategory;
}
