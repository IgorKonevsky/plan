package com.example.plan.services;

import com.example.plan.entities.Subtask;
import com.example.plan.entities.Task;
import com.example.plan.repos.SubtaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubtaskService {

    @Autowired
    private SubtaskRepo subtaskRepo;


    public List<Subtask> getSubtasksByTask(Task task){
        List<Subtask> finalList = new ArrayList<>();
        List<Subtask> bufferList = subtaskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Subtask subtask = bufferList.get(i);
            if(subtask.getTask().equals(task))
                finalList.add(subtask);
        }


        return finalList;
    }
    public List<Subtask> getSubtasksByTaskId(Long id){
        List<Subtask> finalList = new ArrayList<>();
        List<Subtask> bufferList = subtaskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Subtask subtask = bufferList.get(i);
            if(subtask.getTask().getId().equals(id))
                finalList.add(subtask);
        }


        return finalList;
    }

}
