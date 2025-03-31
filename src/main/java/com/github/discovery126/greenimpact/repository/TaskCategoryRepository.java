package com.github.discovery126.greenimpact.repository;

import com.github.discovery126.greenimpact.model.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {
}
