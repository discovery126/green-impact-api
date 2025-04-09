package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = """
SELECT DISTINCT ON (t.id) t.*
FROM tasks t
         LEFT JOIN tasks_users_status tus_today
                   ON t.id = tus_today.task_id
                       AND tus_today.user_id = :userId
                       AND tus_today.taken_at::DATE = CURRENT_DATE
         LEFT JOIN tasks_users_status tus_any
                   ON t.id = tus_any.task_id
                       AND tus_any.user_id = :userId
WHERE tus_today.task_id IS NULL AND t.type='DAILY' 
   OR tus_any.task_id IS NULL AND t.type='LIMITED' AND t.expired_date > now()
""",nativeQuery = true)
    List<Task> findAllAvailableTasksForUser(@Param("userId") Long userId);

    @Query(value = """
SELECT t.*
FROM tasks t
JOIN tasks_users_status tus ON t.id = tus.task_id
WHERE tus.completed_at IS NULL  AND tus.user_id=:userId AND (
    tus.taken_at::DATE = CURRENT_DATE AND t.type='DAILY'
        OR t.type='LIMITED'  AND t.expired_date > now());""",nativeQuery = true)
    List<Task> findAllUncompletedActiveTasksByUser(@Param("userId") Long userId);

    @Query(value = """
SELECT t.*
FROM tasks t
WHERE t.type='DAILY' OR t.type='LIMITED' AND t.expired_date > now();""",nativeQuery = true)
    List<Task> findAllTasks();
}
