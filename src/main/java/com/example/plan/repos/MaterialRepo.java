package com.example.plan.repos;

import com.example.plan.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepo extends JpaRepository<Material, Long> {
}
