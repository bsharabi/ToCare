package com.example.tocare.BLL.Departments;

import java.io.Serializable;

public abstract class  UserModel implements Serializable {

    private String id, userName, name, lastName, phone, bio, imageUrl;
    private boolean isAdmin;
    private String birthday;
    private String country;
    private String codePhone;



    //---------------------------- Constructor ----------------------------------


    public UserModel(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, String birthday, String country, String codePhone) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;
        this.birthday = birthday;
        this.country = country;
        this.codePhone = codePhone;
    }

    public UserModel(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;

    }

    public UserModel(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, Tasks tasks) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.isAdmin = isAdmin;

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

    //---------------------------- Methods --------------------------------------


    //---------------------------- Override --------------------------------------


//    @Override
//    public String toString() {
//        return "UserModel{" +
//                "id='" + id + '\'' +
//                ", userName='" + userName + '\'' +
//                ", name='" + name + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", phone='" + phone + '\'' +
//                ", bio='" + bio + '\'' +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", isAdmin=" + isAdmin +
//                '}';
//    }
}
