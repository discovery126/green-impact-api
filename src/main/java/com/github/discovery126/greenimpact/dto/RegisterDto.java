package com.github.discovery126.greenimpact.dto;

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
public class RegisterDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;


    @NotBlank(message = "DisplayName is required")
    @Size(min = 3, max = 25, message = "DisplayName must be between 3 and 25 characters")
    @Pattern(
            regexp = "^[A-Za-zА-Яа-яЁё][A-Za-zА-Яа-яЁё0-9 _.-]{2,24}$",
            message = "The name can only contain letters, numbers, spaces, -, _, and ."
    )
    @JsonProperty("display_name")
    private String displayName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%^&*])(?=\\S+$).{8,}$", message = "Password must contain at least one uppercase letter, one number, and one special character")
    private String password;

}
