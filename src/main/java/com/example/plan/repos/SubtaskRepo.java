package com.example.plan.repos;

import com.example.plan.entities.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepo extends JpaRepository<Subtask, Long> {
}
