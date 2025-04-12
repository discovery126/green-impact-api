package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    boolean existsByUserIdAndEventIdAndConfirmedAtNotNull(Long user_id, Long event_id);
    UserEvent findByUserIdAndEventId(Long userId, Long eventId);
    List<UserEvent> findAllByUserIdOrderByRegisteredAtDesc(Long userId);
}
