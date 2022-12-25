package com.example.tocare.BLL.Departments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {

    private String name, type, description;
    private String status;
    private int priority;
    private String permission;
    private String accessibility;
    private Date created;
    private Date done;
    private  String author;
    private List<String> imagesUrl;


    //---------------------------- Constructor ----------------------------------

    public Task(String name, String type, String description, String status, int priority, String permission, String accessibility, Date created, Date done, String author, List<String> imagesUrl) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.permission = permission;
        this.accessibility = accessibility;
        this.created = created;
        this.done = done;
        this.author = author;
        this.imagesUrl = imagesUrl;
    }

    public Task(String name, String type, String description, String status, int priority, List<String> imagesUrl) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.imagesUrl = imagesUrl;
    }

    public Task(String name, String type, String description, String status, int priority, String author, List<String> imagesUrl) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.author = author;
        this.imagesUrl = imagesUrl;
    }

    public Task() {
        imagesUrl=new ArrayList<>();
    }
    //---------------------------- Getter&&Setter -------------------------------


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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDone() {
        return done;
    }

    public void setDone(Date done) {
        this.done = done;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.status = status;
    }


    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


}
