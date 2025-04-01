package com.github.discovery126.greenimpact.service;

import com.github.discovery126.greenimpact.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventStatusUpdate implements StatusUpdater {
    private final EventRepository eventRepository;

    @Override
    @Scheduled(cron = "0 */10 * * * *")
    public void processStatusUpdate() {
        LocalDateTime now = LocalDateTime.now();
        // Обновляет каждые 10 минут
        // Обновляет каждую сущности на уровне базы данных из-за @Modifying
        eventRepository.updateStatusForActiveEvents(now);
        eventRepository.updateStatusForCompletedEvents(now);
    }
}
