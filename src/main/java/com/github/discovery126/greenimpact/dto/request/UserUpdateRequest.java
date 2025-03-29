package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 20, message = "Имя пользователя должно содержать от 3 до 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Имя пользователя может содержать только латинские буквы, цифры, дефисы и подчеркивания")
    @JsonProperty("display_name")
    private String displayName;

    @PositiveOrZero(message = "Количество баллов должно быть неотрицательным числом")
    private Integer points;

    @NotNull(message = "ID города обязателен")
    @Positive(message = "ID города должен быть больше 0")
    @JsonProperty("city_id")
    private Integer cityId;

    @NotNull(message = "У пользователя должна быть хотя бы одна роль")
    @Size(min = 1, message = "Пользователь должен иметь хотя бы одну роль")
    private Set<Integer> roles;
}
