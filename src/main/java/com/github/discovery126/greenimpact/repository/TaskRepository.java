package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
