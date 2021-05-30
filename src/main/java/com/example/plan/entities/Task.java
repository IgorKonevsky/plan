package com.example.plan.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task")
@Data
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
   // @CollectionTable(name = "deliveries", joinColumns = @JoinColumn(name = "task_id"))
    private LocalDateTime delivery;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;




    @ElementCollection(targetClass = Grade.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "grades", joinColumns = @JoinColumn(name = "task_id"))
    @Enumerated(EnumType.STRING)
    private Set<Grade> grade;


    @Enumerated(EnumType.STRING)
    private Progress progress;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    @Column(name = "common")
    private String common;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;



    public boolean isActive(){
        return status.equals(TaskStatus.ACTIVE);
    }
    public boolean isCompleted(){
        return status.equals(TaskStatus.COMPLETED);
    }
    public boolean isFinished(){
        return status.equals(TaskStatus.FINISHED);
    }
    public boolean isExpired(){
        return status.equals(TaskStatus.EXPIRED);
    }

    public boolean isCompletedOrFinished(){
        return status == TaskStatus.COMPLETED || status == TaskStatus.FINISHED;
    }

    public boolean progressWorking(){
        return progress.equals(Progress.WORKING);
    }
    public boolean progressStuck(){
        return progress.equals(Progress.STUCK);
    }
    public boolean progressNotSelected(){
        return progress.equals(Progress.NOT_SELECTED);
    }

    /*public boolean isExcellent(){
        return status.equals(Grade.EXCELLENT);
    }
    public boolean isGood(){
        return status.equals(Grade.GOOD);
    }
    public boolean isPassably(){
        return status.equals(Grade.PASSABLY);
    }*/


}
