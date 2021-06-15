package com.example.plan.entities;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Subtask {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean status;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
