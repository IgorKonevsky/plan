package com.example.plan.repos;

import com.example.plan.entities.Task;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @NonNull
    @EntityGraph(attributePaths = { "comments" })
    List<Task> findAll();


}
