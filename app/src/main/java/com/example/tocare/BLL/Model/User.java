package com.example.tocare.BLL.Model;


public class User extends UserModel  {

    private String parentId;

    public User(String id, String userName, String name,String lastName, String phone, String bio, String imageUrl, boolean isAdmin) {
        super(id, userName, name,lastName, phone, bio, imageUrl, isAdmin);
    }

    public User(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, String birthday, String country, String codePhone, String parentId) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin, birthday, country, codePhone);
        this.parentId = parentId;
    }

    public User(String id, String userName, String name, String lastName, String phone, String bio, String imageUrl, boolean isAdmin, String parentId) {
        super(id, userName, name, lastName, phone, bio, imageUrl, isAdmin);
        this.parentId = parentId;
    }

    public User(String parentId) {
        this.parentId = parentId;
    }

    public User() {
        this.setImageUrl("https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a");
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
