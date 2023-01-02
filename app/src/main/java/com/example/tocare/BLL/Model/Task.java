package com.example.tocare.BLL.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

    private String taskId, type, description;
    private String userIdTask;
    private String status;
    private int priority;
    private String permission;
    private String accessibility;
    private String start;
    private String done;
    private String author;
    private List<String> imagesUrl;
    private Date created;



    //---------------------------- Constructor ----------------------------------

    public Task(String taskId, String type, String description, String userIdTask, String status, int priority, String permission, String accessibility, String start, String done, String author, List<String> imagesUrl ) {
        this.taskId = taskId;
        this.type = type;
        this.description = description;
        this.userIdTask = userIdTask;
        this.status = status;
        this.priority = priority;
        this.permission = permission;
        this.accessibility = accessibility;
        this.start = start;
        this.done = done;
        this.author = author;
        this.imagesUrl = imagesUrl;
        this.created = new Date();
    }

    public Task(String name, String type, String description, String status, int priority, String permission, String accessibility, String start, String done, String author, List<String> imagesUrl) {
        this.taskId = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.permission = permission;
        this.accessibility = accessibility;
        this.start = start;
        this.done = done;
        this.author = author;
        this.imagesUrl = imagesUrl;
        this.created = new Date();
        setStart("");
    }

    public Task(String name, String type, String description, String status, int priority, List<String> imagesUrl) {
        this.taskId = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.imagesUrl = imagesUrl;
        created = new Date();
        setStart("");
    }

    public Task(String taskId, String type, String description, String status, int priority, String author, List<String> imagesUrl) {
        this.taskId = taskId;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.author = author;
        this.imagesUrl = imagesUrl;
        created = new Date();
        setStart("");
    }

    public Task() {
        imagesUrl = new ArrayList<>();
        setStart("");
    }

    public Task(String description, String id) {
        imagesUrl = new ArrayList<>();
        setStart("");
        this.description = description;
        userIdTask = id;

    }
    //---------------------------- Getter&&Setter -------------------------------


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUserIdTask() {
        return userIdTask;
    }

    public void setUserIdTask(String userIdTask) {
        this.userIdTask = userIdTask;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {


        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(start);
            Date currentDate = new Date();
            if (currentDate.compareTo(date) < 0)
                this.status = "Nactive";
            else this.status = "Active";
        } catch (ParseException e) {
            // handle parsing exception
        }

    }


    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


    @Override
    public String toString() {
        return "Task{" +
                "name='" + taskId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                ", permission='" + permission + '\'' +
                ", accessibility='" + accessibility + '\'' +
                ", created=" + start +
                ", done=" + done +
                ", author='" + author + '\'' +
                ", imagesUrl=" + imagesUrl +
                '}';
    }
}
