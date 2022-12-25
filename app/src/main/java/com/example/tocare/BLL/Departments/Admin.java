package com.example.tocare.BLL.Departments;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class Admin extends UserModel implements Serializable {


    private List<String> childrenId;

    //---------------------------- Constructor ----------------------------------
    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId=new ArrayList<>();
    }

    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin, List<String> children) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
        this.childrenId = children;
    }

    public Admin(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin, Tasks tasks, List<String> children) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin, tasks);
        this.childrenId = children;
    }
    public Admin(){
        childrenId=new ArrayList<>();
    }

    //---------------------------- Getter&&Setter -------------------------------
    public List<String> getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(List<String> childrenId) {
        this.childrenId = childrenId;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "childrenId=" + childrenId +
                '}'+super.toString();
    }
}
