package com.example.tocare.BLL.Departments;

public class User extends UserModel {


    public User(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{}";
    }
}
