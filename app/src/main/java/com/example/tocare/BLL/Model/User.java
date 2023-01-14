package com.example.tocare.BLL.Model;


import androidx.annotation.NonNull;

public class User extends UserModel  {

    private String parentId;


    public User(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, String parentId) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin);
       setParentId(parentId);
    }

    public User() {

    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "parentId='" + getParentId() + '\'' +
                '}';
    }
}
