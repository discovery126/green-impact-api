package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TakenTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TakenTaskRepository extends JpaRepository<TakenTask, Long> {

    @Query(value = """
SELECT tt.*
FROM taken_tasks tt
         JOIN tasks t ON tt.task_id = t.id
         LEFT JOIN tasks_completion tc
                   ON tt.id = tc.taken_task_id
                       AND (tc.completed_at::date) = CURRENT_DATE
WHERE t.type = :type
  AND (tt.taken_at::DATE) = CURRENT_DATE
  AND tt.user_id = :user_id
  AND tc.taken_task_id IS NULL;""",nativeQuery = true)
    List<TakenTask> findAllTakenTaskByUserIdAndType(@Param("user_id") Long userId, @Param("type")String taskType);
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    TakenTask findByUserIdAndTaskId(Long userId, Long taskId);
}
