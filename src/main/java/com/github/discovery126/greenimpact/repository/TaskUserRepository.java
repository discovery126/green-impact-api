package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TaskUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskUserRepository extends JpaRepository<TaskUser, Long> {

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM tasks_users_status tus
                WHERE tus.user_id = :userId
                  AND tus.task_id = :taskId
                  AND tus.taken_at::DATE = CURRENT_DATE);""",nativeQuery = true)
    boolean existsTodayByUserIdAndTaskId(Long userId, Long taskId, LocalDateTime takenAt);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM tasks_users_status tus
                WHERE tus.user_id = :userId
                  AND tus.task_id = :taskId
                  AND tus.completed_at IS NOT NULL);""",nativeQuery = true)
    boolean existsCompletedByUserIdAndTaskId(Long userId, Long taskId);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1
                FROM tasks_users_status tc
                WHERE tc.user_id = :userId
                  AND tc.task_id = :taskId
                  AND tc.completed_at::DATE = CURRENT_DATE);""",nativeQuery = true)
    boolean existsTodayCompletedByUserAndTaskId(@Param("userId") Long userId,
                                                @Param("taskId") Long taskId);

    @Query(value = """
            SELECT tuc.*
            FROM tasks_users_status tuc
            WHERE tuc.user_id = :userId AND
                  tuc.taken_at::DATE = CURRENT_DATE AND
                  tuc.completed_at IS NULL;""",nativeQuery = true)
    TaskUser findUncompletedByUserAndTask(@Param("userId") Long userId,
                                          @Param("taskId") Long taskId);
    @Query(value = """
            SELECT tuc.*
            FROM tasks_users_status tuc
            WHERE tuc.user_id = :userId AND
              tuc.taken_at::DATE = CURRENT_DATE AND 
              tuc.completed_at IS NOT NULL;""",nativeQuery = true)
    List<TaskUser> findAllCompletedByUserId(Long userId);

    @Query(value = """
        SELECT tuc.*
        FROM tasks_users_status tuc
        WHERE tuc.completed_at IS NOT NULL
        """, countQuery = """
        SELECT COUNT(*) 
        FROM tasks_users_status tuc
        WHERE tuc.completed_at IS NOT NULL
        """, nativeQuery = true)
    Page<TaskUser> findAllCompletionTasks(Pageable pageable);

    TaskUser findByUserIdAndTaskId(Long userId, Long taskId);
}
