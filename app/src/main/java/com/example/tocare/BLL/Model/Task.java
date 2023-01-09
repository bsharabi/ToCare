package com.example.tocare.BLL.Model;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

    private String taskId, type, description;
    private String takenByUserId;
    private String takenByUserName;
    private String status;
    private int priority;
    private String permission;
    private String bid;
    private String start;
    private String done;
    private String author;
    private List<String> imagesUrl;
    private Date created;
//---------------------------- Constructor ----------------------------------

    public Task(String taskId, String type, String description, String status, int priority, String permission, String bid, String start, String done, String author, List<String> imagesUrl, Date created) {
        this.taskId = taskId;
        this.type = type;
        this.description = description;
        this.takenByUserId = "";
        this.takenByUserName = "";
        this.status = status;
        this.priority = priority;
        this.permission = permission;
        this.bid = bid;
        this.start = start;
        this.done = done;
        this.author = author;
        this.imagesUrl = imagesUrl;
        this.created = created;
    }

    public Task() {
        this.created = new Date();
        this.imagesUrl=new ArrayList<>();
    }
//---------------------------- Getter&&Setter -------------------------------

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

    public String getTakenByUserId() {
        return takenByUserId;
    }

    public void setTakenByUserId(String takenByUserId) {
        this.takenByUserId = takenByUserId;
    }

    public String getTakenByUserName() {
        return takenByUserName;
    }

    public void setTakenByUserName(String takenByUserName) {
        this.takenByUserName = takenByUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;

    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", takenByUserId='" + takenByUserId + '\'' +
                ", takenByUserName='" + takenByUserName + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                ", permission='" + permission + '\'' +
                ", bid='" + bid + '\'' +
                ", start='" + start + '\'' +
                ", done='" + done + '\'' +
                ", author='" + author + '\'' +
                ", imagesUrl=" + imagesUrl +
                ", created=" + created +
                "}\n";
    }
}
