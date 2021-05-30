package com.example.plan.services;

import com.example.plan.entities.Group;
import com.example.plan.entities.User;
import com.example.plan.repos.GroupRepo;
import com.example.plan.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);

    }

    public List<User> findAllStudents(){
        List<User> finalList = new ArrayList<>();
        List<User> bufferList = userRepo.findAll();

        for (int i = 0; i<bufferList.size(); i++){
            User user = bufferList.get(i);
            //if(user.isStudent())
                finalList.add(user);
        }


        return finalList;
    }


    /*public List<Group> getGroupsByStudentId(User user){
        List<Group> finalList = new ArrayList<>();
        Set<Group> bufferList = user.getGroups();
        for (int i = 0; i<bufferList.size(); i++){

            Group group = bufferList.get(i);
            if(!group.getStudents().isEmpty()) {
                //if (group.getStudents().get(i).getId().equals(id))
                if(group.getStudents().contains(user))
                    //if(group.getStudents().stream().toList().get(i).getId().equals(id))
                    finalList.add(group);
            }
        }


        return finalList;

    }*/

    public Group enterGroupByCode(User student,String code){
        List<Group> groupList = groupRepo.findAll();
        Group finalGroup = null;
        for (int i = 0; i<groupList.size(); i++){
            Group group = groupList.get(i);
            if(group.getCode().equals(code)) {
                finalGroup = group;
                List<Group> groups = student.getGroups();

                groups.add(group);
                student.setGroups(groups);

                userRepo.save(student);
            }
        }
        return finalGroup;
    }

}
