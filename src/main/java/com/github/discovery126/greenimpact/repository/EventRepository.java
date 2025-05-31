package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = 'ACTIVE' WHERE e.startDate < :now AND e.endDate > :now AND e.status != 'ACTIVE'")
    void updateStatusForActiveEvents(@Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.status = 'COMPLETED' WHERE e.endDate < :now AND e.status != 'COMPLETED'")
    void updateStatusForCompletedEvents(@Param("now") LocalDateTime now);

    List<Event> findAllByOrderByStartDateDesc();
}
