package com.example.plan.repos;

import com.example.plan.entities.Group;
import com.example.plan.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolesRepo extends JpaRepository<Roles, Long> {
    List<Roles> findAllByGroup(Group group);
    void deleteAllByGroup(Group group);

}
