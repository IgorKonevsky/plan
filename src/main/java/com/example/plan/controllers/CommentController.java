package com.example.plan.controllers;

import com.example.plan.entities.Comment;
import com.example.plan.entities.Role;
import com.example.plan.entities.Task;
import com.example.plan.entities.User;
import com.example.plan.repos.CommentRepo;
import com.example.plan.services.CommentService;
import com.example.plan.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RolesService rolesService;

    @PostMapping("/{id}")
    public String create(@AuthenticationPrincipal User author, Comment comment, @PathVariable("id")Task task) {
        if(comment.getText().trim().length()>0)
        commentService.create(comment, author,task);
        if (rolesService.getUserRole(author,task.getGroup()).equals(Role.STUDENT)) {
            return "redirect:/group/"+task.getGroup().getId()+"/student/tasks/" + task.getId();
        }
        else {
            return "redirect:/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId();
        }

    }
}
