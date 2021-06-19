package com.example.plan.controllers;


import com.example.plan.entities.*;
import com.example.plan.repos.*;
import com.example.plan.services.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/group/{group}/teacher")
public class TeacherController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private TaskService taskService;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TaskFileRepo taskFileRepo;

    @Autowired
    private TaskFileService taskFileService;

    @Autowired
    private CommentFileRepo commentFileRepo;

    @Autowired
    private CommentFileServices commentFileServices;

    @Autowired
    private SubtaskRepo subtaskRepo;

    @Autowired
    private SubtaskService subtaskService;

    @Autowired
    private MaterialRepo materialRepo;

    @Autowired
    private MaterialService materialService;


    @Autowired
    private MailSender mailSender;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/students")
    public String getStudents(Model model,@PathVariable("group")Group group){
        //model.addAttribute("students", userService.findAllStudents());
        model.addAttribute("students", group.getStudents());

        return "/teacher/students";
    }

    @GetMapping("/tasks/student/{id}/new")
    public String newTask(@PathVariable("id") User student,Model model,Task task,@PathVariable("group")Group group){
        model.addAttribute("student", student);
        model.addAttribute("task",task);
        model.addAttribute("group",group);
        return "/teacher/new-task";
    }

    @PostMapping("/tasks/student/{id}")
    public String createTask(@AuthenticationPrincipal User teacher, @RequestParam("title")String title,Task task1,@RequestParam("description")String description,@RequestParam(name = "subtask[]",required = false)String[] subtaskNames,@PathVariable("id") User student, Model model, @PathVariable("group")Group group){
        Task task = new Task();
        List<Subtask> subtaskList = new ArrayList<>();
        task.setGroup(group);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime start = LocalDateTime.now();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+start);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //start = formatter.format(start);
        //start = LocalDateTime.parse(dateFormat.format(start));
        task.setStart(start);
        task.setTeacher(teacher);
        task.setStudent(student);
        task.setStatus(TaskStatus.ACTIVE);
        task.setProgress(Progress.NOT_SELECTED);
        task.setTitle(title);
        task.setDeadline(task1.getDeadline());
        task.setDescription(description);
        if(subtaskNames!=null) {
            for (String subtaskName : subtaskNames) {
                Subtask subtask = new Subtask();
                subtask.setTitle(subtaskName);
                subtask.setStatus(false);
                subtask.setTask(task);
                subtaskList.add(subtask);

            }
        }

        taskRepo.save(task);
        subtaskRepo.saveAll(subtaskList);

        return "redirect:/group/"+group.getId()+"/teacher/students";
    }

    @PostMapping("/tasks/common")
    public String createCommonTask(@AuthenticationPrincipal User teacher, Task task1,@RequestParam(name = "file") MultipartFile file,@RequestParam(name = "subtask[]",required = false)String[] subtaskNames, Model model, @PathVariable("group")Group group) throws IOException {
        System.out.println("##########################################################################"+Arrays.toString(subtaskNames));
        List<User> students = group.getStudents();
        List<Task> tasks = new ArrayList<>();
        List<Subtask> subtaskList = new ArrayList<>();

        List<TaskFile> taskFiles = new ArrayList<>();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime start = LocalDateTime.now();
        String code = MainController.groupCode();
        String uuidFile;
        String resultFilename = null;
        boolean flag = false;
        if(file!=null){
            flag=true;
        }
        File uploadDir = new File(uploadPath);

        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }
        if(flag) {
            uuidFile = UUID.randomUUID().toString();
            resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
        }


        for (int i = 0; i<students.size();i++) {
            Task task = new Task();
            TaskFile taskFile = new TaskFile();

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+start);
            task.setStart(start);
            task.setTeacher(teacher);
            task.setStatus(TaskStatus.ACTIVE);
            task.setProgress(Progress.NOT_SELECTED);
            task.setCommon(code);
            task.setTitle(task1.getTitle());
            task.setDeadline(task1.getDeadline());
            task.setDescription(task1.getDescription());
            task.setStudent(students.get(i));
            task.setGroup(group);
            if(subtaskNames!=null) {
                for (String subtaskName : subtaskNames) {
                    Subtask subtask = new Subtask();
                    subtask.setTitle(subtaskName);
                    subtask.setStatus(false);
                    subtask.setTask(task);
                    subtaskList.add(subtask);

                }
            }
            String message = String.format("Здравствуйте, вам было дано новое задание - %s",task1.getTitle());
            mailSender.send(students.get(i).getEmail(),"Новое задание!",message);


            if(flag){
                taskFile.setOriginalname(file.getOriginalFilename());
                taskFile.setFilename(resultFilename);
                taskFile.setTask(task);
                //taskFileRepo.save(taskFile);
                taskFiles.add(taskFile);
            }
            tasks.add(task);
        }


        taskRepo.saveAll(tasks);
        taskFileRepo.saveAll(taskFiles);
        if(subtaskList!=null){
            subtaskRepo.saveAll(subtaskList);
        }

        /*task.setGroup(group);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        task.setStart(time.atDate(date));
        task.setTeacher(teacher);

        task.setStatus(TaskStatus.ACTIVE);
        task.setProgress(Progress.NOT_SELECTED);
        task.setCommon(true);
        for (User student : students) {
            task.setStudent(student);

        }*/


        return "redirect:/group/"+group.getId()+"/teacher/students";
    }



    @GetMapping("/tasks/student/{id}")
    public String studentTasks(@PathVariable("id") User student, Model model,@PathVariable("group")Group group){
        model.addAttribute("student",student);
        model.addAttribute("group",group);
        model.addAttribute("tasks",taskService.getTasksByStudentIdAndGroup(student.getId(),group));
        return "/teacher/one-student-tasks";
    }

    @GetMapping("/tasks/{id}")
    public String taskPage(@PathVariable("id")Task task,Model model,@PathVariable("group")Group group){
        model.addAttribute("task",task);
        model.addAttribute("comments",task.getComments());
        model.addAttribute("group",group);

        List<TaskFile> taskFiles = taskFileService.getTaskFilesByTask(task);
        List<Subtask> subtasks = subtaskService.getSubtasksByTask(task);
        System.out.println("subtasks:");
        System.out.println(subtasks);

        model.addAttribute("taskFiles",taskFiles);
        model.addAttribute("subtasks",subtasks);

        if(task.getStatus()!=TaskStatus.FINISHED){
            task.setGrade(null);
            taskService.update(task.getId(),task);
        }
        return "/teacher/one-task";
    }

    @PatchMapping("/tasks/{id}")
    public String editTask(@PathVariable("id")Long id,Model model,Task task,@PathVariable("group")Group group,@RequestParam(name = "subtask[]",required = false)boolean[] subtaskStatuses,@RequestParam(name = "subtaskIndexes[]",required = false)boolean[] subtaskIndexes){
        //model.addAttribute("task",task);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(Arrays.toString(subtaskStatuses));
        List<Subtask> subtaskList = subtaskService.getSubtasksByTaskId(id);

        System.out.println("before");
        System.out.println(subtaskList);
        for(int i = 0; i < subtaskList.size(); i++){
            if(subtaskStatuses!=null)
            subtaskList.get(i).setStatus(subtaskStatuses[i]);
            else subtaskList.get(i).setStatus(false);
        }
        System.out.println("after");
        System.out.println(subtaskList);
        subtaskRepo.saveAll(subtaskList);
        model.addAttribute("task",task);
        taskService.update(id,task);
        if (task.getGrade()!=null){
            task.setStatus(TaskStatus.FINISHED);
        }
        if(task.getStatus()!=TaskStatus.COMPLETED || task.getStatus()!=TaskStatus.FINISHED){
            task.setDelivery(null);
        }
        return "redirect:/group/"+task.getGroup().getId()+"/teacher/tasks/" + task.getId();
        //return "redirect:/main";
        //return "redirect:/teacher/tasks/student/"+task.getStudent_id();
    }


    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable("id")Task task,@PathVariable("group")Group group){
        commentService.deleteCommentsByTask(task);
        taskRepo.delete(task);
        return "redirect:/group/"+group.getId()+"/teacher/tasks/student/"+task.getStudent().getId();
    }

    @DeleteMapping("/students/{id}")
    public String removeStudent(@PathVariable("id")User student,@PathVariable("group")Group group){
        List<Group> groups = student.getGroups();
        for(int i=0;i<groups.size();i++){
            if(groups.get(i).equals(group))
                groups.remove(i);
        }
        student.setGroups(groups);
        userRepo.save(student);
        return "redirect:/group/"+group.getId()+"/teacher/students";
    }

    @GetMapping("/tasks")
    public String allTasks(@PathVariable("group")Group group,Model model){
        model.addAttribute("group",group);
        return "/teacher/all-tasks";
    }

    @PostMapping("/tasks")
    public String selectTaskCategory(@RequestParam("category")String category, @PathVariable("group")Group group){

        return switch (category) {
            case "ACTIVE" -> "redirect:/group/"+group.getId()+"/teacher/tasks/active";
            case "COMPLETED" -> "redirect:/group/"+group.getId()+"/teacher/tasks/completed";
            case "FINISHED" -> "redirect:/group/"+group.getId()+"/teacher/tasks/finished";
            case "EXPIRED" -> "redirect:/group/"+group.getId()+"/teacher/tasks/expired";
            default ->  "redirect:/group/"+group.getId()+"/teacher/tasks";
        };


        //if()
    }

    @GetMapping("/tasks/active")
    public String activeTasks(Model model, @AuthenticationPrincipal User teacher, @PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByTeacherIdAndStatus(teacher.getId(),TaskStatus.ACTIVE,group));
        model.addAttribute("students",userService.findAllStudents());
        return "/teacher/active-tasks";
    }

    @GetMapping("/tasks/completed")
    public String completedTasks(Model model, @AuthenticationPrincipal User teacher, @PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByTeacherIdAndStatus(teacher.getId(),TaskStatus.COMPLETED,group));
        model.addAttribute("students",userService.findAllStudents());
        return "/teacher/completed-tasks";
    }

    @GetMapping("/tasks/finished")
    public String finishedTasks(Model model, @AuthenticationPrincipal User teacher, @PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByTeacherIdAndStatus(teacher.getId(),TaskStatus.FINISHED,group));
        model.addAttribute("students",userService.findAllStudents());
        return "/teacher/finished-tasks";
    }

    @GetMapping("/tasks/expired")
    public String expiredTasks(Model model, @AuthenticationPrincipal User teacher, @PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByTeacherIdAndStatus(teacher.getId(),TaskStatus.EXPIRED,group));
        model.addAttribute("students",userService.findAllStudents());
        return "/teacher/expired-tasks";
    }
    @GetMapping("/tasks/stuck")
    public String stuckTasks(Model model, @AuthenticationPrincipal User teacher,@PathVariable("group")Group group){
        model.addAttribute("tasks",taskService.getTasksByTeacherIdAndProgress(teacher.getId(),Progress.STUCK,group));
        model.addAttribute("students",userService.findAllStudents());
        return "/teacher/stuck-tasks";
    }





    @GetMapping("/stats")
    public String allStats(@PathVariable("group")Group group){

        return "/teacher/all-stats";
    }

    @PostMapping("/stats")
    public String selectStatsCategory(@RequestParam("category")String category,@PathVariable("group")Group group) {

        return switch (category) {
            case "STUDENTS" -> "redirect:/group/"+group.getId()+"/teacher/stats/students";
            case "TASKS" -> "redirect:/group/"+group.getId()+"/teacher/stats/tasks";

            default -> "redirect:/group/"+group.getId()+"/teacher/stats";
        };
    }

    @GetMapping("/stats/tasks")
    public String tasksStats(@PathVariable("group")Group group, Model model){

        List<Task> tasks = taskService.getByGroup(group);
        List<String> codes = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Task> commonTasks = new ArrayList<>();
        for(Task task : tasks){
            if(task.getCommon()!=null && !task.getCommon().equals("not") && !codes.contains(task.getCommon())){
                codes.add(task.getCommon());
                titles.add(task.getTitle());
                commonTasks.add(task);
            }
        }
        model.addAttribute("titles",titles);
        model.addAttribute("tasks",commonTasks);
        return "/teacher/task-stats";
    }

    @GetMapping("/stats/tasks/{code}")
    public String getOneTaskStats(@PathVariable("code")String code,@PathVariable("group")Group group,Model model ){
        List<Task> tasks = taskService.getTasksByCommonCode(group,code);
        int statusActive = 0;
        int statusCompleted = 0;
        int statusFinished = 0;
        int statusExpired = 0;
        int progressWorking = 0;
        int progressStuck = 0;
        float gradeCounter = 0;
        float gradeSum = 0;
        float avgScore;

        for(int i=0;i<tasks.size();i++){
            Task task = tasks.get(i);
            String title = tasks.get(0).getTitle();
            LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();
            String description = tasks.get(0).getDescription();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
            model.addAttribute("description",description);

            if(task.getGrade()!=null){
                gradeCounter++;
                System.out.println(task.getGrade());
                if (task.getGrade().equals(Grade.PASSABLY)) {
                    gradeSum+=3;
                }
                else if(task.getGrade().equals(Grade.GOOD)){
                    gradeSum+=4;
                }
                else if(task.getGrade().equals(Grade.EXCELLENT)){
                    gradeSum+=5;
                }
            }
            switch (task.getStatus()){
                case ACTIVE -> {
                    statusActive++;
                    break;
                }
                case COMPLETED -> {
                    statusCompleted++;
                    break;
                }
                case EXPIRED -> {
                    statusExpired++;
                    break;
                }
                case FINISHED -> {
                    statusFinished++;

                    break;
                }
            }

            switch (task.getProgress()){
                case WORKING -> {
                    progressWorking++;
                    break;
                }
                case STUCK -> {
                    progressStuck++;
                    break;
                }
            }



        }
        if(gradeCounter!=0) {
            avgScore = gradeSum / gradeCounter;
            System.out.println("gradeSum: " + gradeSum);
            System.out.println("gradeCounter: " + gradeCounter);
            System.out.println("avgScore: " + avgScore);
        }
        model.addAttribute("statusActive",statusActive);
        model.addAttribute("statusCompleted",statusCompleted);
        model.addAttribute("statusExpired",statusExpired);
        model.addAttribute("statusFinished",statusFinished);
        model.addAttribute("progressWorking",progressWorking);
        model.addAttribute("progressStuck",progressStuck);
        return "/teacher/one-common-task-stats";
    }

    @GetMapping("/stats/tasks/{code}/active")
    public String commonTaskActive(@PathVariable("code")String code,@PathVariable("group")Group group,Model model){
        List<Task> tasks = taskService.getTasksByCommonCodeAndStatus(group,code,TaskStatus.ACTIVE);

        if(tasks.size()>0){
           String title = tasks.get(0).getTitle();
           LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
        }

        model.addAttribute("tasks",tasks);
        model.addAttribute("code",code);

        return "/teacher/common-task-active";
    }


    @GetMapping("/stats/tasks/{code}/completed")
    public String commonTaskCompleted(@PathVariable("code")String code,@PathVariable("group")Group group,Model model){
        List<Task> tasks = taskService.getTasksByCommonCodeAndStatus(group,code,TaskStatus.COMPLETED);
        if(tasks.size()>0){
            String title = tasks.get(0).getTitle();
            LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
        }
        model.addAttribute("tasks",tasks);
        model.addAttribute("code",code);
        return "/teacher/common-task-completed";
    }



    @GetMapping("/stats/tasks/{code}/finished")
    public String commonTaskFinished(@PathVariable("code")String code,@PathVariable("group")Group group,Model model){
        List<Task> tasks = taskService.getTasksByCommonCodeAndStatus(group,code,TaskStatus.FINISHED);
        if(tasks.size()>0){
            String title = tasks.get(0).getTitle();
            LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
        }
        model.addAttribute("tasks",tasks);
        model.addAttribute("code",code);
        return "/teacher/common-task-finished";
    }



    @GetMapping("/stats/tasks/{code}/expired")
    public String commonTaskExpired(@PathVariable("code")String code,@PathVariable("group")Group group,Model model){
        List<Task> tasks = taskService.getTasksByCommonCodeAndStatus(group,code,TaskStatus.EXPIRED);
        if(tasks.size()>0){
            String title = tasks.get(0).getTitle();
            LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
        }
        model.addAttribute("tasks",tasks);
        model.addAttribute("code",code);
        return "/teacher/common-task-expired";
    }



    @GetMapping("/stats/tasks/{code}/stuck")
    public String commonTaskStuck(@PathVariable("code")String code,@PathVariable("group")Group group,Model model){
        List<Task> tasks = taskService.getTasksByCommonCodeAndProgress(group,code,Progress.STUCK);
        if(tasks.size()>0){
            String title = tasks.get(0).getTitle();
            LocalDate deadline = tasks.get(0).getDeadline();
            LocalDateTime start = tasks.get(0).getStart();

            model.addAttribute("title",title);
            model.addAttribute("deadline",deadline);
            model.addAttribute("start",start);
        }
        model.addAttribute("tasks",tasks);
        model.addAttribute("code",code);
        return "/teacher/common-task-stuck";
    }

    @GetMapping("/materials")
    public String materialsPage(@PathVariable("group")Group group,Model model){
        List<Material> materials = materialService.getMaterialsByGroup(group);
        model.addAttribute("materials",materials);


        return "/teacher/materials";
    }

    @PostMapping("/material")
    public String uploadMaterial(@PathVariable("group")Group group,@RequestParam(name = "file") MultipartFile file,Material material1) throws IOException {

        if(file!=null){
            Material material = new Material();
            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            material.setFilename(resultFilename);
            material.setOriginalname(file.getOriginalFilename());
            material.setGroup(group);
            material.setTitle(material1.getTitle());
            material.setDescription(material1.getDescription());
            materialRepo.save(material);
        }
        return "redirect:/group/"+group.getId()+"/teacher/materials";

    }

    @DeleteMapping("/material/{id}")
    public String deleteMaterial(@PathVariable("group")Group group,@PathVariable("id")Material material){
        materialRepo.delete(material);
        return "redirect:/group/"+group.getId()+"/teacher/materials";
    }

    @GetMapping("/editcommon/{code}")
    public String editCommonTask(@PathVariable("group")Group group,@PathVariable("code")String code,Model model){
        List<Task> tasks = taskService.getTasksByCommonCode(group,code);
        Task task = null;
        if(tasks.size()>0){
            task = tasks.get(0);
        }
        model.addAttribute("task",task);
        return "/teacher/edit-common-task";
    }

    @DeleteMapping("/deletecommon/{code}")
    public String deleteCommonTask(@PathVariable("group")Group group,@PathVariable("code")String code){

        List<Task> tasks = taskService.getTasksByCommonCode(group,code);
        for(int i=0; i<tasks.size(); i++){
            Task task = tasks.get(i);
            subtaskRepo.deleteAll(task.getSubtasks());
            commentRepo.deleteAll(task.getComments());
            taskFileRepo.deleteAll(taskFileService.getTaskFilesByTask(task));
        }
        taskRepo.deleteAll(tasks);


        return "redirect:/group/"+group.getId()+"/teacher/stats/tasks";
    }

    /*@GetMapping("/stats/students")
    public String studentsStats(Model model){
        List<User> students = userService.findAllStudents();
        model.addAttribute("students",students);
        int[] activeCounter = new int[0];
        int[] completedCounter;
        int[] finishedCounter;
        int[] expiredCounter;
        int arrNum = -1;
//        int gradeSum = 0;
//        int gradeNum = 0;

        for(User student : students){
            List<Task> tasks = taskService.getTasksByStudentId(student.getId());
            arrNum++;
            for(Task task : tasks){

                switch (task.getStatus()){
                    case ACTIVE -> {
                        activeCounter[arrNum]++;
                        break;
                    }
                    case COMPLETED -> {
                        completedCounter++;
                        break;
                    }
                    case EXPIRED -> {
                        expiredCounter++;
                        break;
                    }
                    case FINISHED -> {
                        finishedCounter++;

                        break;
                    }


                }
            }
        }
        model.addAttribute("activeCounter",activeCounter);
        model.addAttribute("completedCounter",completedCounter);
        model.addAttribute("expiredCounter",expiredCounter);
        model.addAttribute("finishedCounter",finishedCounter);

        return "/teacher/students-stats";
    }*/
}
