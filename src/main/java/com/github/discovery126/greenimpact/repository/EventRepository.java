package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
