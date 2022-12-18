package com.example.tocare.BLL.Departments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {

    private String id, userName, name, lastName, phone, bio, imageUrl;
    private boolean isAdmin;
    private List<String> tasks;

    //---------------------------- Constructor ----------------------------------

    public UserModel(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;
        this.tasks = new ArrayList<>();
    }

    public UserModel(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, List<String> tasks) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;
        this.tasks = tasks;
    }

    public UserModel() {
        tasks = new ArrayList<>();
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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAdmin=" + isAdmin +
                ", tasks=" + tasks +
                '}';
    }
}
