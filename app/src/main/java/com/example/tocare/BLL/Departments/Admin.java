package com.example.tocare.BLL.Departments;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class Admin extends UserModel {


    private Map<String,User> childrenId;

    //---------------------------- Constructor ----------------------------------
    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId=new HashMap<>();
    }

    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin, Map<String,User> children) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId = children;
    }

    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin, ArrayList<String> tasks, Map<String,User> children) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin, tasks);
        this.childrenId = children;
    }
    public Admin(){
        childrenId=new HashMap<>();
    }

    //---------------------------- Getter&&Setter -------------------------------
    public Map<String,User> getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(Map<String,User> childrenId) {
        this.childrenId = childrenId;
    }
}
