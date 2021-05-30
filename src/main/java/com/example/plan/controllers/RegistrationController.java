package com.example.plan.controllers;


import com.example.plan.entities.Role;
import com.example.plan.entities.User;
import com.example.plan.repos.UserRepo;
import com.example.plan.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String regpage(@ModelAttribute("user") User user){

        return "registration";
    }

    @PostMapping
    public String reguser(@ModelAttribute("user") User user){




        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);


        return "redirect:/";
    }




}
