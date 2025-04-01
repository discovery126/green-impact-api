package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TaskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    @Query(value = """

            SELECT EXISTS (
            SELECT 1
            FROM tasks_completion tc
            WHERE tc.user_id = :userId
              AND tc.task_id = :taskId
              AND tc.completed_at::DATE = CURRENT_DATE);""",nativeQuery = true)
    boolean existsByUserIdAndTaskIdAndDate(@Param("userId") Long userId,
                                           @Param("taskId") Long taskId);
}
