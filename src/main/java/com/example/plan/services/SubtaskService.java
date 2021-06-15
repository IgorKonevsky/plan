package com.example.plan.services;

import com.example.plan.repos.SubtaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubtaskService {

    @Autowired
    private SubtaskRepo subtaskRepo;



}
