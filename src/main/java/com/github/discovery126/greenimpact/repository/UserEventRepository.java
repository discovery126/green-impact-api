package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    UserEvent findByUserIdAndEventId(Long userId, Long eventId);
}
