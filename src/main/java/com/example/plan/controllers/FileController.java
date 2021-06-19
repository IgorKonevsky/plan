package com.example.plan.controllers;


import com.example.plan.entities.CommentFile;
import com.example.plan.entities.Group;
import com.example.plan.entities.Material;
import com.example.plan.entities.TaskFile;
import com.example.plan.repos.TaskFileRepo;
import com.example.plan.services.TaskFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller

public class FileController {

    @Autowired
    private TaskFileRepo taskFileRepo;

    @Autowired
    private TaskFileService taskFileService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/taskfile/{id}")
    public void downloadTaskFile(@PathVariable("id")TaskFile taskFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(uploadPath+"/"+taskFile.getFilename());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());


    }

    @GetMapping("/commentfile/{id}")
    public void downloadCommentFile(@PathVariable("id")CommentFile commentFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(uploadPath+"/"+commentFile.getFilename());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());


    }
    @GetMapping("/material/{id}")
    public void downloadMaterial(@PathVariable("id") Material material, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(uploadPath+"/"+material.getFilename());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(file.getName());
        response.setContentLength((int) file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());


    }

//   @PostMapping("/group/{id}/material")
//    public String uploadMaterial





}
