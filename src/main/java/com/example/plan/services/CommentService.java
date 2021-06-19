package com.example.plan.services;

import com.example.plan.entities.Comment;
import com.example.plan.entities.CommentFile;
import com.example.plan.entities.Task;
import com.example.plan.entities.User;
import com.example.plan.repos.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public void create(String text, User author, Task task, CommentFile commentFile){
        Comment comment = new Comment();
        comment.setText(text);
        comment.setAuthor(author);
        comment.setTask(task);
        if(commentFile!=null);
            comment.setCommentFile(commentFile);

        commentRepo.save(comment);

    }

    public void deleteCommentsByTask(Task task){
        List<Comment> comments = commentRepo.findAll();

        for(int i=0; i<comments.size();i++){
            Comment comment = comments.get(i);
            if (comment.getTask().equals(task))
                commentRepo.delete(comment);
        }
    }
}
