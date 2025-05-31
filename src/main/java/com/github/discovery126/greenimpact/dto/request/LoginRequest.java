package com.github.discovery126.greenimpact.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email обязателен для заполнения")
    @Size(max=100, message = "Email должен содержать не более 100 символов")
    private String email;

    @NotBlank(message = "Пароль обязателен для заполнения")
    @Size(max=72, message = "Пароль должен содержать не менее 8 и не более 72 символов")
    private String password;
}
