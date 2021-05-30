package com.example.plan.services;

import com.example.plan.entities.Group;
import com.example.plan.entities.Role;
import com.example.plan.entities.Roles;
import com.example.plan.entities.User;
import com.example.plan.repos.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RolesService {

    @Autowired
    private RolesRepo rolesRepo;

    public void setTeacher(User user, Group group){
        Roles role = new Roles();
        role.setUser(user);
        role.setGroup(group);
        role.setRole(Role.TEACHER);
        rolesRepo.save(role);
    }

    public void setStudent(User user, Group group){
        Roles role = new Roles();
        role.setUser(user);
        role.setGroup(group);
        role.setRole(Role.STUDENT);
        rolesRepo.save(role);
    }

    @Transactional
    public void deleteRolesByGroup(Group group){
        //List<Roles> rolesList = rolesRepo.findAllByGroup(group);
        rolesRepo.deleteAllByGroup(group);

    }

    public Role getUserRole(User user,Group group){
        List<Roles> roles = rolesRepo.findAllByGroup(group);
        Role role = null;
        for(int i = 0; i<roles.size(); i++){
            Roles userRole = roles.get(i);
            if(userRole.getUser().getId().equals(user.getId())){
                role = userRole.getRole();
            }
        }
        return role;
    }
}
