package com.example.plan.repos;

import com.example.plan.entities.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskFileRepo extends JpaRepository<TaskFile, Long> {
}
