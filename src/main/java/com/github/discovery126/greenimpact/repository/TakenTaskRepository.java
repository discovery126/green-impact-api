package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TakenTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TakenTaskRepository extends JpaRepository<TakenTask, Long> {

    @Query(value = """
SELECT tt.*
FROM tasks t
          JOIN taken_tasks tt ON t.id = tt.task_id AND tt.user_id = :user_id
WHERE t.type = :type;""",nativeQuery = true)
    List<TakenTask> findAllTakenTaskByUserIdAndType(@Param("user_id") Long userId, @Param("type")String taskType);
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    TakenTask findByUserIdAndTaskId(Long userId, Long taskId);
}
