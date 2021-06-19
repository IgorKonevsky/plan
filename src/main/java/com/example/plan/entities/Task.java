package com.example.plan.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "task")

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


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    @OneToMany(mappedBy = "task")
    private List<Subtask> subtasks;



    @Column(name = "common")
    private String common;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    /*@ManyToMany(cascade = Ca)
    @JoinTable(

    )
    List<Tag> tags;*/



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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getDelivery() {
        return delivery;
    }

    public void setDelivery(LocalDateTime delivery) {
        this.delivery = delivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Set<Grade> getGrade() {
        return grade;
    }

    public void setGrade(Set<Grade> grade) {
        this.grade = grade;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
