package com.example.tocare.BLL.Departments;

import java.util.HashMap;
import java.util.Map;

public abstract class UserModel {

    private String id, userName, fullName, phone, bio, imageUrl;
    private boolean isAdmin;
    private Map<String, Object> tasks;

    //---------------------------- Constructor ----------------------------------

    public UserModel(String id, String userName, String fullName, String phone, String bio, String imageUrl, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;
        this.tasks = new HashMap<>();
    }

    public UserModel(String id, String userName, String fullName, String phone, String bio, String imageUrl, boolean isAdmin, Map<String, Object> tasks) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;
        this.tasks = tasks;
    }
    public UserModel() {

    }

    //---------------------------- Getter&&Setter -------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Map<String, Object> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Object> tasks) {
        this.tasks = tasks;
    }

    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAdmin=" + isAdmin +
                ", tasks=" + tasks +
                '}';
    }
}
