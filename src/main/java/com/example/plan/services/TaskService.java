package com.example.plan.services;

import com.example.plan.entities.*;
import com.example.plan.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepo taskRepo;

    public List<Task> getTasksByStudentIdAndGroup(Long id, Group group){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getStudent().getId().equals(id)&&task.getGroup().getId().equals(group.getId()))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByTeacherId(Long id){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getTeacher().getId().equals(id))
                finalList.add(task);
        }


        return finalList;

    }
    public List<Task> getTasksByTeacherIdAndStatus(Long id, TaskStatus taskStatus, Group group){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().getId().equals(group.getId()) && task.getTeacher().getId().equals(id)&&task.getStatus().equals(taskStatus))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByStudentIdAndStatus(Long id, TaskStatus taskStatus,Group group){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().getId().equals(group.getId()) && task.getStudent().getId().equals(id)&&task.getStatus().equals(taskStatus))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByTeacherIdAndProgress(Long id, Progress progress, Group group){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().getId().equals(group.getId()) && task.getTeacher().getId().equals(id)&&task.getProgress().equals(progress))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getByGroup(Group group){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().getId().equals(group.getId()))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByCommonCode(Group group,String code){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().equals(group) && !task.getCommon().equals("not")  && task.getCommon().equals(code))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByCommonCodeAndStatus(Group group,String code,TaskStatus status){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().equals(group) && task.getCommon().equals(code) && task.getStatus().equals(status))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByCommonCodeAndProgress(Group group,String code, Progress progress){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().equals(group) && task.getCommon().equals(code) && task.getProgress().equals(progress))
                finalList.add(task);
        }


        return finalList;

    }

    public List<Task> getTasksByStudentAndProgress(Group group, User student, Progress progress){
        List<Task> finalList = new ArrayList<>();
        List<Task> bufferList = taskRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Task task = bufferList.get(i);
            if(task.getGroup().equals(group)&& task.getStudent().getId().equals(student.getId()) && task.getProgress().equals(progress) )
                finalList.add(task);
        }


        return finalList;

    }




    public void update(Long id,Task updatedTask){
        updatedTask.setId(id);
        taskRepo.save(updatedTask);
    }
}
