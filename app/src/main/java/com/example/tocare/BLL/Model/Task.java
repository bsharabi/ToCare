package com.example.tocare.BLL.Model;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

    private String taskId, type, description;
    private String takenByUserId;
    private String status;
    private int priority;
    private String permission;
    private String bid;
    private String start;
    private String done;
    private String author;
    private List<String> imagesUrl;
    private List<String> imagesUrlFinish;
    private String created;

//---------------------------- Constructor ----------------------------------

    public Task(String taskId, String type, String description, String takenByUserId, int priority, String permission, String bid, String start, String done, String author, List<String> imagesUrl, String created) {
        setTaskId(taskId);
        setType(type);
        setDescription(description);
        setTakenByUserId(takenByUserId);
        setStatus((permission.equals("Public") || takenByUserId.equals("")) ? "Active" : "In Process");
        setPriority(priority);
        setPermission(permission);
        setBid(bid);
        setStart(start);
        setDone(done);
        setAuthor(author);
        setImagesUrl(imagesUrl);
        setCreated(created);
    }

    public Task() {

        setCreated(new Date().toString());
        setImagesUrl(new ArrayList<>());
        setImagesUrlFinish(new ArrayList<>());

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

    }

    public List<String> getImagesUrlFinish() {
        return imagesUrlFinish;
    }

    public void setImagesUrlFinish(List<String> imagesUrlFinish) {
        this.imagesUrlFinish = imagesUrlFinish;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", takenByUserId='" + takenByUserId + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + getPriority() +
                ", permission='" + getPermission() + '\'' +
                ", bid='" + bid + '\'' +
                ", start='" + getStart() + '\'' +
                ", done='" + getDone() + '\'' +
                ", author='" + author + '\'' +
                ", imagesUrl=" + imagesUrl +
                ", imagesUrlFinish=" + getImagesUrlFinish() +
                ", created=" + created +
                '}';
    }
}
