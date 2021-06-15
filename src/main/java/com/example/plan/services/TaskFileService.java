package com.example.plan.services;

import com.example.plan.entities.Task;
import com.example.plan.entities.TaskFile;
import com.example.plan.repos.TaskFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskFileService {

    @Autowired
    private TaskFileRepo taskFileRepo;


    public List<TaskFile> getTaskFilesByTask(Task task){
        List<TaskFile> finalList = new ArrayList<>();
        List<TaskFile> bufferList = taskFileRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            TaskFile taskFile = bufferList.get(i);
            if(taskFile.getTask().equals(task))
                finalList.add(taskFile);
        }


        return finalList;

    }

}
