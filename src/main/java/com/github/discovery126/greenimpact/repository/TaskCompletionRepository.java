package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TaskCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
}
