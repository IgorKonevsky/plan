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
    public String create(@AuthenticationPrincipal User author,@RequestParam("text")String text , @RequestParam("file") MultipartFile file, @PathVariable("id")Task task) throws IOException {
        Comment comment = new Comment();
        if(text.length()>0 || file!=null){
            comment.setText(text);
            comment.setTask(task);
            comment.setAuthor(author);

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

                //commentService.create(comment, author,task, commentFile);
                comment.setCommentFile(commentFile);
                commentRepo.save(comment);
                commentFileRepo.save(commentFile);
            }
            else{
                //commentService.create(comment, author,task, null);
                comment.setCommentFile(null);
                commentRepo.save(comment);
            }



        }

        if (rolesService.getUserRole(author,task.getGroup()).equals(Role.STUDENT)) {
            String subject = "?????????? ??????????????????????!";
            String message = String.format("?????????????? %s ???? ???????????? %s ?????????????? ?????????? ?????????????????????? ?? ?????????????? %s\n???????????? - %s",task.getStudent().getFullname(),task.getGroup().getTitle(),task.getTitle(),"http://localhost:8080/group/"+task.getGroup().getId()+"/student/tasks/" + task.getId());
            mailSender.send(task.getTeacher().getEmail(),subject,message);
            return "redirect:/group/"+task.getGroup().getId()+"/student/tasks/" + task.getId();
        }
        else {
            String subject = "?????????? ??????????????????????!";
            String message = String.format("?????????????????????????? %s ???? ???????????? %s ?????????????? ?????????? ?????????????????????? ?? ?????????????? %s\n???????????? - %s",task.getTeacher().getFullname(),task.getGroup().getTitle(),task.getTitle(),"http://localhost:8080/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId());
            mailSender.send(task.getStudent().getEmail(),subject,message);
            return "redirect:/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId();
        }

    }
}
