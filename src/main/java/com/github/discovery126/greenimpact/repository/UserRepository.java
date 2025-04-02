package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.dto.response.RatingResponse;
import com.github.discovery126.greenimpact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
SELECT users.display_name, COALESCE(SUM(tasks_points.points), 0) + COALESCE(SUM(events_points.points), 0) AS total_points
FROM users
LEFT JOIN (
        SELECT tasks_completion.user_id, tasks.points
        FROM tasks_completion
        LEFT JOIN tasks ON tasks_completion.task_id = tasks.id
        WHERE tasks_completion.verified_at >= :startDate
          AND tasks_completion.verified_at < :endDate
    ) AS tasks_points ON users.id = tasks_points.user_id
LEFT JOIN (
        SELECT users_events.user_id, events.event_points AS points
        FROM users_events
        LEFT JOIN events ON users_events.event_id = events.id
        WHERE users_events.confirmed_at >= :startDate
          AND users_events.confirmed_at < :endDate
    ) AS events_points ON users.id = events_points.user_id
GROUP BY users.display_name
ORDER BY total_points DESC;
""",nativeQuery = true)
    List<RatingResponse> findRatingByStartDateAndEndDate(@Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
    Optional<User> findByEmail(String email);
    Optional<User> findByDisplayName(String displayName);
}