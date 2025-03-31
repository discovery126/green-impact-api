package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.model.RewardType;
import com.github.discovery126.greenimpact.utils.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardRequest {

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 50, message = "Название должно содержать не более 50 символов")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 100, message = "Описание должно содержать не более 100 символов")
    private String description;

    @PositiveOrZero(message = "Количество должно быть положительным числом или нулём")
    private Integer amount;

    @NotNull
    @ValidEnum(enumClass = RewardType.class, message = "Неправильный тип награды")
    @JsonProperty("reward_type")
    private String rewardType;

    @NotNull(message = "Стоимость в баллах не может быть пустой")
    @Positive(message = "Стоимость в баллах должна быть положительным числом")
    @JsonProperty("cost_points")
    private Integer costPoints;

    @NotNull(message = "ID категории не может быть пустым")
    @Positive(message = "ID категории должен быть положительным числом")
    @JsonProperty("category_id")
    private Integer categoryId;
}
