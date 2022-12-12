package com.example.tocare.BLL.Departments;
import java.util.HashMap;
import java.util.Map;

public class Admin extends UserModel {

    private static Admin single_instance = null;
    private Map<String, User> children;

    //---------------------------- Constructor ----------------------------------
    public Admin(String id, String userName, String fullName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, fullName, phone, bio, imageUrl, isAdmin);
        this.children=new HashMap<>();
    }

    public Admin(String id, String userName, String fullName, String phone, String bio, String imageUrl, boolean isAdmin, Map<String, User> children) {
        super(id, userName, fullName, phone, bio, imageUrl, isAdmin);
        this.children = children;
    }

    public Admin(String id, String userName, String fullName, String phone, String bio, String imageUrl, boolean isAdmin, Map<String, Object> tasks, Map<String, User> children) {
        super(id, userName, fullName, phone, bio, imageUrl, isAdmin, tasks);
        this.children = children;
    }
    public Admin(){

    }

    //---------------------------- Getter&&Setter -------------------------------

    public Map<String, User> getChildren() {
        return children;
    }

    public void setChildren(Map<String, User> children) {
        this.children = children;
    }
}
