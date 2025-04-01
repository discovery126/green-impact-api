package com.github.discovery126.greenimpact.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskRequest {

    @Size(max=255, message = "Комментарий не может быть больше 255 символов")
    private String description;
}
