package com.example.plan.controllers;

import com.example.plan.entities.*;
import com.example.plan.repos.*;
import com.example.plan.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/group/{group}/student")
public class StudentController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SubtaskRepo subtaskRepo;

    @Autowired
    private SubtaskService subtaskService;


    @GetMapping("/tasks")
    public String allTasks(@PathVariable("group")Group group,Model model){
        model.addAttribute("group",group);
        return "/student/all-tasks";
    }

    @Autowired
    private TaskFileRepo taskFileRepo;

    @Autowired
    private TaskFileService taskFileService;

    @Autowired
    private CommentFileRepo commentFileRepo;

    @Autowired
    private CommentFileServices commentFileServices;

    @PostMapping("/tasks")
    public String selectTaskCategory(@RequestParam("category")String category,@PathVariable("group")Group group){

        return switch (category) {
            case "ACTIVE" -> "redirect:/group/"+group.getId()+"/student/tasks/active";
            case "COMPLETED" -> "redirect:/group/"+group.getId()+"/student/tasks/completed";
            case "FINISHED" -> "redirect:/group/"+group.getId()+"/student/tasks/finished";
            case "EXPIRED" -> "redirect:/group/"+group.getId()+"/student/tasks/expired";
            default -> "redirect:/group/"+group.getId()+"/student/tasks";
        };

    }

    @GetMapping("/tasks/active")
    public String activeTasks(Model model, @AuthenticationPrincipal User student,@PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByStudentIdAndStatus(student.getId(),TaskStatus.ACTIVE,group));
        model.addAttribute("student",student);
        return "/student/active-tasks";
    }

    @GetMapping("/tasks/completed")
    public String completedTasks(Model model, @AuthenticationPrincipal User student,@PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByStudentIdAndStatus(student.getId(),TaskStatus.COMPLETED,group));
        model.addAttribute("student",student);
        return "/student/completed-tasks";
    }

    @GetMapping("/tasks/finished")
    public String finishedTasks(Model model, @AuthenticationPrincipal User student,@PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByStudentIdAndStatus(student.getId(),TaskStatus.FINISHED,group));
        model.addAttribute("student",student);
        return "/student/finished-tasks";
    }

    @GetMapping("/tasks/expired")
    public String expiredTasks(Model model, @AuthenticationPrincipal User student,@PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByStudentIdAndStatus(student.getId(),TaskStatus.EXPIRED,group));
        model.addAttribute("student",student);
        return "/student/expired-tasks";
    }

    @GetMapping("/tasks/{id}")
    public String taskPage(@PathVariable("id") Task task, Model model, @PathVariable("group")Group group){
        model.addAttribute("task",task);
        model.addAttribute("comments",task.getComments());
        model.addAttribute("group",group);
        List<TaskFile> taskFiles = taskFileService.getTaskFilesByTask(task);
        List<Subtask> subtasks = subtaskService.getSubtasksByTask(task);

        model.addAttribute("taskFiles",taskFiles);
        model.addAttribute("subtasks",subtasks);

        return "/student/one-task";
    }

    @PatchMapping("/tasks/{id}")
    public String editTask(@PathVariable("id")Long id,Model model,Task task){
        //model.addAttribute("task",task);
        model.addAttribute("task",task);

        if(task.getStatus().equals(TaskStatus.COMPLETED)){
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            task.setDelivery(LocalDateTime.now());
        }
        if(task.getStatus()!=TaskStatus.COMPLETED){
            task.setDelivery(null);
        }
        if(task.getStatus().equals(TaskStatus.COMPLETED)){
            task.setProgress(Progress.WORKING);
        }

        taskService.update(id,task);

        return "redirect:/group/"+task.getGroup().getId()+"/student/tasks/"+task.getId();
        //return "redirect:/main";
        //return "redirect:/teacher/tasks/student/"+task.getStudent_id();
    }

}
