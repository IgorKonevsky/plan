package com.example.plan.controllers;

import com.example.plan.entities.Group;
import com.example.plan.entities.Role;
import com.example.plan.entities.User;
import com.example.plan.repos.GroupRepo;
import com.example.plan.repos.RolesRepo;
import com.example.plan.repos.UserRepo;
import com.example.plan.services.GroupService;
import com.example.plan.services.RolesService;
import com.example.plan.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesRepo rolesRepo;

    @Autowired
    private RolesService rolesService;

    public static String groupCode(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @GetMapping("/")
    public String hello(Model model,@AuthenticationPrincipal User user){

        List<Group> groupsTeacher = groupService.getGroupsByTeacherId(user);
        //List<Group> groupsStudent = groupService.getGroupsByStudentId(user);
        //List<Group> groupsTeacher = user.getGroups();
        List<Group> groupsStudent = user.getGroups();


//        System.out.println(groupsTeacher);
//        System.out.println(groupsStudent);
        model.addAttribute("groupsTeacher",groupsTeacher);
        model.addAttribute("groupsStudent",groupsStudent);

        return "hello";
    }

    @PostMapping("/group")
    public String createGroup(@AuthenticationPrincipal User user, Group group){


        group.setTeacher(user);
        group.setCode(groupCode());
        groupRepo.save(group);
        rolesService.setTeacher(user,group);


        return "redirect:/";
    }

    @PostMapping("/find")
    public String enterGroup(@AuthenticationPrincipal User user,@RequestParam("code") String code){

        //groupService.enterGroupByCode(user,code);
        Group group = userService.enterGroupByCode(user,code);
        rolesService.setStudent(user,group);
        return "redirect:/";

    }

    @PatchMapping("/group/{id}")
    public String editGroup(@PathVariable("id")Long id,Model model,Group group){


        model.addAttribute("task",group);
        groupService.update(id,group);

        return "redirect:/";

    }

    @DeleteMapping("/group/{id}")
    public String deleteGroup(@PathVariable("id") Group group){
        //rolesRepo.deleteAllByGroup(group);
        groupService.removeGroupStudents(group);
        rolesService.deleteRolesByGroup(group);
        groupRepo.delete(group);
        return "redirect:/";
    }

    @GetMapping("/group/{id}")
    public String mainpage(@AuthenticationPrincipal User user, Model model,@PathVariable("id")Group group){
        model.addAttribute("user",user);
        model.addAttribute("group",group);

        if(rolesService.getUserRole(user,group).equals(Role.TEACHER)){
            return "/teacher/main";
        }
        else

        return "/student/main";
    }
}
