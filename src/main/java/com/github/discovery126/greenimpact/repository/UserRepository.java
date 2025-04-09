package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.dto.response.RatingResponse;
import com.github.discovery126.greenimpact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
SELECT u.display_name, 
       COALESCE(tp.total_task_points, 0) + COALESCE(ep.total_event_points, 0) AS total_points
FROM users u
LEFT JOIN (
    SELECT tus.user_id, SUM(t.points) AS total_task_points
    FROM tasks_users_status tus
    LEFT JOIN tasks t ON tus.task_id = t.id
    WHERE tus.verified_at >= :startDate AND tus.verified_at < :endDate
    GROUP BY tus.user_id
) tp ON u.id = tp.user_id
LEFT JOIN (
    SELECT ue.user_id, SUM(e.event_points) AS total_event_points
    FROM users_events ue
    LEFT JOIN events e ON ue.event_id = e.id
    WHERE ue.confirmed_at >= :startDate AND ue.confirmed_at < :endDate
    GROUP BY ue.user_id
) ep ON u.id = ep.user_id
ORDER BY total_points DESC, u.created_at ASC
LIMIT 10;
""",nativeQuery = true)
    List<RatingResponse> findRatingByStartDateAndEndDate(@Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);
    Optional<User> findByEmail(String email);
    Optional<User> findByDisplayName(String displayName);
}