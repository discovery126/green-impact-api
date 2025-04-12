package com.github.discovery126.greenimpact.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_points", nullable = false)
    private Integer eventPoints;

    @Column(name = "event_start_at", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "event_end_at", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "organiser_name", nullable = false)
    private String organiserName;

    @Column(name ="organiser_phone", nullable = false)
    private String organiserPhone;

    @Column(name = "event_code", nullable = false)
    private String eventCode;

    @Column(nullable = false)
    private String street;

    @Column(name ="house_number")
    private Integer houseNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Europe/Moscow"));

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
