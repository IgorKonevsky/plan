package com.example.plan.services;

import com.example.plan.entities.Group;
import com.example.plan.entities.Task;
import com.example.plan.entities.User;
import com.example.plan.repos.GroupRepo;
import com.example.plan.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    public void update(Long id, Group updatedGroup){
        updatedGroup.setId(id);
        groupRepo.save(updatedGroup);
    }

    public List<Group> getGroupsByTeacherId(User user){
        List<Group> finalList = new ArrayList<>();
        List<Group> bufferList = groupRepo.findAll();
        //System.out.println("111111"+bufferList);
        for (int i = 0; i<bufferList.size(); i++){
            Group group = bufferList.get(i);
            if(group.getTeacher().getId().equals(user.getId()))
            //if(group.getTeacher().equals(user))
                finalList.add(group);
            //System.out.println(i+"!!!!!!!!!"+finalList);
        }


        return finalList;

    }

    public List<Group> getGroupsByStudentId(User user){
        List<Group> finalList = new ArrayList<>();
        List<Group> bufferList = groupRepo.findAll();
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

    }

    public void enterGroupByCode(User student, String code){
        List<Group> groupList = groupRepo.findAll();

        for (int i = 0; i<groupList.size(); i++){
            Group group = groupList.get(i);
            if(group.getCode().equals(code)) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                List<User> students = group.getStudents();

                students.add(student);
                group.setStudents(students);

                groupRepo.save(group);



            }
        }


    }

    public void removeGroupStudents(Group group){
        List<User> students = group.getStudents();
        System.out.println(students);
        for(int i =0; i<students.size(); i++){
            User user = students.get(i);
            students.remove(user);
        }
        group.setStudents(students);
        groupRepo.save(group);
    }

}
