package com.example.tocare.BLL.Model;

import java.util.ArrayList;

import java.util.List;

public class Admin extends UserModel {


    private List<String> childrenId;

    //---------------------------- Constructor ----------------------------------
    public Admin(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId = new ArrayList<>();
    }

    public Admin(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, List<String> children) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId = children;
    }


    public Admin() {
        childrenId = new ArrayList<>();
    }

    //---------------------------- Getter&&Setter -------------------------------
    public List<String> getChildrenId() {
        return childrenId;
    }

    public void addNewUser(String userId) {
        childrenId.add(userId);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "childrenId=" + childrenId +
                '}' + '\n' + super.toString();
    }
}
