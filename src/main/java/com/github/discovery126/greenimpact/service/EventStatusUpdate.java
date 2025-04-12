package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class EventStatusUpdate implements StatusUpdater {
    private final EventRepository eventRepository;

    @Override
    @Scheduled(cron = "0 */5 * * * *")
    public void processStatusUpdate() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        // Обновляет каждые 5 минут
        // Обновляет каждую сущности на уровне базы данных из-за @Modifying
        eventRepository.updateStatusForActiveEvents(now);
        eventRepository.updateStatusForCompletedEvents(now);
    }
}
