package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TakenTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TakenTaskRepository extends JpaRepository<TakenTask, Long> {

    boolean existsByUserIdAndTaskIdAndTakenAt(Long userId, Long taskId, LocalDate takenAt);
    boolean existsByUserIdAndTaskId(Long userId, Long taskId);
    TakenTask findByUserIdAndTaskId(Long userId, Long taskId);
    TakenTask findByUserIdAndTaskIdAndTakenAt(Long userId, Long taskId,LocalDate takenAt);
}
