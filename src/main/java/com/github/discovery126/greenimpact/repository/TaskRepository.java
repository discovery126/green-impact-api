package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = """
    SELECT t.*
    FROM tasks t
             LEFT JOIN taken_tasks tt ON t.id = tt.task_id AND tt.user_id = :user_id
    WHERE
        (t.type = 'DAILY' OR (t.type = 'LIMITED' AND NOW() BETWEEN t.start_date AND t.end_date))
      AND tt.task_id IS NULL
      """,nativeQuery = true)
    List<Task> findAllTaskForUser(@Param("user_id") Long userId);

}
