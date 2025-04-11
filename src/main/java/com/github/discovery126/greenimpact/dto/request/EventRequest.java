package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class EventRequest {

    @NotNull(message = "Название мероприятия не может быть пустым.")
    @Size(min = 5, max = 100, message = "Название мероприятия должно содержать от 5 до 100 символов.")
    private String title;

    @NotNull(message = "Описание мероприятия не может быть пустым.")
    @Size(min = 10, max = 255, message = "Описание мероприятия должно содержать от 20 до 255 символов.")
    private String description;

    @PositiveOrZero(message = "Количество баллов должно быть положительным числом или равно нулю.")
    @JsonProperty("event_points")
    private Integer eventPoints;

    @NotNull(message = "Дата начала мероприятия не может быть пустой.")
    @FutureOrPresent(message = "Дата начала мероприятия должна быть в будущем или настоящем.")
    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания мероприятия не может быть пустой.")
    @FutureOrPresent(message = "Дата окончания мероприятия должна быть в будущем или настоящем.")
    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @NotNull(message = "Имя организатора не может быть пустым")
    @Size(min = 1, max = 100, message = "Имя организатора должно содержать от 1 до 100 символов.")
    @JsonProperty("organiser_name")
    private String organiserName;

    @NotNull(message = "Телефон организатора не может быть пустым.")
    @Pattern(regexp = "^\\+?[0-9]{11}$", message = "Телефон организатора должен содержать 11 цифр и может начинаться с '+'.")
    @JsonProperty("organiser_phone")
    private String organiserPhone;

    @NotNull(message = "Код мероприятия не может быть пустым.")
    @Size(min = 1, max = 20, message = "Код мероприятия должен содержать от 1 до 20 символов.")
    @JsonProperty("event_code")
    private String eventCode;

    @NotNull(message = "Улица не может быть пустой")
    @Size(min = 1, max = 100, message = "Улица должна содержать от 1 до 100 символов.")
    private String street;

    @PositiveOrZero(message = "Номер дома должен быть положительным числом или равен 0.")
    @JsonProperty("house_number")
    private Integer houseNumber;

    @NotNull(message = "Город не может быть пустым")
    @JsonProperty("city_id")
    private Integer cityId;
}
