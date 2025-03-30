package com.github.discovery126.greenimpact.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 20, message = "Имя пользователя должно содержать от 3 до 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Имя пользователя может содержать только латинские буквы, цифры, дефисы и подчеркивания")
    @JsonProperty("display_name")
    private String displayName;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну цифру и один специальный символ")
    private String password;

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
