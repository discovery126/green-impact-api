package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email обязателен для заполнения")
    @Email(message = "Email должен быть действительным")
    @Size(max=100, message = "Email должен содержать не более 100 символов")
    private String email;

    @NotBlank(message = "Отображаемое имя обязательно")
    @Size(min = 3, max = 25, message = "Отображаемое имя должно быть от 3 до 25 символов")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9 _.-]{2,24}$",
            message = "Имя может содержать только буквы, цифры, пробелы, -, _, и ."
    )
    @JsonProperty("display_name")
    private String displayName;

    @NotBlank(message = "Пароль обязателен для заполнения")
    @Size(min = 8,max=72, message = "Пароль должен содержать не менее 8 и не более 72 символов")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, одну цифру и один специальный символ")
    private String password;

}
