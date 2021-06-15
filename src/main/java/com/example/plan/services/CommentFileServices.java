package com.example.plan.services;

import com.example.plan.entities.CommentFile;
import com.example.plan.entities.Task;
import com.example.plan.entities.TaskFile;
import com.example.plan.repos.CommentFileRepo;
import com.example.plan.repos.TaskFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentFileServices {


    @Autowired
    private CommentFileRepo commentFileRepo;


    /*public List<CommentFile> getCommentFilesByTask(Task task){
        List<CommentFile> finalList = new ArrayList<>();
        List<CommentFile> bufferList = commentFileRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            CommentFile commentFile = bufferList.get(i);
            if(commentFile.getComment().getTask().equals(task))
                finalList.add(commentFile);
        }


        return finalList;

    }*/
}
