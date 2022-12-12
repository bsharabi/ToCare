package com.example.tocare.BLL.Departments;

import java.util.List;

public class Task {

    private String name, type, Description;
    private String status;
    private int priority;
    private List<String> imagesUrl;


    //---------------------------- Constructor ----------------------------------

    public Task(String name, String type, String description, String status, int priority, List<String> imagesUrl) {
        this.name = name;
        this.type = type;
        Description = description;
        this.status = status;
        this.priority = priority;
        this.imagesUrl = imagesUrl;
    }

    //---------------------------- Getter&&Setter -------------------------------

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
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", Description='" + Description + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                ", imagesUrl=" + imagesUrl +
                '}';
    }
}
