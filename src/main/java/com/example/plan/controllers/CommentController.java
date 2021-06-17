package com.example.plan.controllers;

import com.example.plan.entities.*;
import com.example.plan.repos.CommentFileRepo;
import com.example.plan.repos.CommentRepo;
import com.example.plan.services.CommentService;
import com.example.plan.services.MailSender;
import com.example.plan.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentFileRepo commentFileRepo;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private MailSender mailSender;

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/{id}")
    public String create(@AuthenticationPrincipal User author, Comment comment, @RequestParam("file") MultipartFile file, @PathVariable("id")Task task) throws IOException {
        if(comment.getText().trim().length()>0 || file!=null){

            if(file!=null){
                CommentFile commentFile = new CommentFile();
                File uploadDir = new File(uploadPath);

                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                commentFile.setFilename(resultFilename);
                commentFile.setOriginalname(file.getOriginalFilename());
                commentFile.setComment(comment);
                commentFileRepo.save(commentFile);
                commentService.create(comment, author,task, commentFile);
            }
            else{
                commentService.create(comment, author,task, null);
            }



        }

        if (rolesService.getUserRole(author,task.getGroup()).equals(Role.STUDENT)) {
            String subject = "Новый комментарий!";
            String message = String.format("Студент %s из группы %s добавил новый комментарий к заданию %s\nСсылка - %s",task.getStudent().getFullname(),task.getGroup().getTitle(),task.getTitle(),"http://localhost:8080/group/"+task.getGroup().getId()+"/student/tasks/" + task.getId());
            mailSender.send(task.getTeacher().getEmail(),subject,message);
            return "redirect:/group/"+task.getGroup().getId()+"/student/tasks/" + task.getId();
        }
        else {
            String subject = "Новый комментарий!";
            String message = String.format("Преподаватель %s из группы %s добавил новый комментарий к заданию %s\nСсылка - %s",task.getTeacher().getFullname(),task.getGroup().getTitle(),task.getTitle(),"http://localhost:8080/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId());
            mailSender.send(task.getStudent().getEmail(),subject,message);
            return "redirect:/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId();
        }

    }
}
