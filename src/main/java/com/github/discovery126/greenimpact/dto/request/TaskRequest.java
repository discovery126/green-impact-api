package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.TaskType;
import com.github.discovery126.greenimpact.utils.ValidEnum;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Название задания не может быть пустым")
    private String title;

    @NotBlank(message = "Описание задания не может быть пустым")
    private String description;

    @ValidEnum(enumClass = TaskType.class, message = "Неправильный тип задания")
    @JsonProperty("task_type")
    private String taskType;

    @FutureOrPresent(message = "Дата начала должна быть в настоящем или будущем времени")
    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @Future(message = "Дата окончания должна быть в будущем")
    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @NotNull(message = "Количество очков не может быть пустым")
    @Positive(message = "Очки должны быть не меньше 1")
    private Integer points;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории должен быть положительным числом")
    @JsonProperty("category_id")
    private Integer categoryId;
}
