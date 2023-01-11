package com.example.tocare.BLL.Model;

import java.util.ArrayList;

import java.util.List;

public class Admin extends UserModel {


    //---------------------------- Constructor ----------------------------------

    public Admin(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin);
    }

    public Admin() {
    }

//---------------------------- Getter&&Setter -------------------------------


    @Override
    public String toString() {
        return "Admin{" +
                '}' + '\n' + super.toString();
    }
}
