package com.example.plan.services;


import com.example.plan.entities.Group;
import com.example.plan.entities.Material;
import com.example.plan.entities.Task;
import com.example.plan.repos.MaterialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepo materialRepo;

    public List<Material> getMaterialsByGroup(Group group){
        List<Material> finalList = new ArrayList<>();
        List<Material> bufferList = materialRepo.findAll();
        for (int i = 0; i<bufferList.size(); i++){
            Material material = bufferList.get(i);
            if(material.getGroup().equals(group))
                finalList.add(material);
        }


        return finalList;
    }

}
