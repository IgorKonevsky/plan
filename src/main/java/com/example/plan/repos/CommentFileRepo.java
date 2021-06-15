package com.example.plan.repos;

import com.example.plan.entities.CommentFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentFileRepo extends JpaRepository<CommentFile, Long> {
}
