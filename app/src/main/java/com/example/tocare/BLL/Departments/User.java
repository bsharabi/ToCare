package com.example.tocare.BLL.Departments;

import java.io.Serializable;

public class User extends UserModel implements Serializable {


    public User(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
    }

    public User() {
        this.setImageUrl("https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a");
    }

    @Override
    public String toString() {
        return "User{}";
    }
}
