package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {

    @Pattern(regexp = "^[А-ЯЁ][а-яё-]*[А-ЯЁа-яё]$", message = "Имя города может содержать только Русские буквы и символ '-' ")
    @JsonProperty("name_city")
    private String nameCity;
}
