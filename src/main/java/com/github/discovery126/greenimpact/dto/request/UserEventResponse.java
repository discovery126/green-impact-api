package com.github.discovery126.greenimpact.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.discovery126.greenimpact.dto.response.EventResponse;
import com.github.discovery126.greenimpact.dto.response.UserResponse;
import com.github.discovery126.greenimpact.model.Event;
import com.github.discovery126.greenimpact.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventResponse {
    private long id;

    private UserResponse userResponse;

    private EventResponse eventResponse;

    @JsonProperty("registered_at")
    private LocalDateTime registeredAt;

    @JsonProperty("confirmed_at")
    private LocalDateTime confirmedAt;
}
