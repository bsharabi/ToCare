package com.example.tocare;

public abstract class UserDetails {

    private String email;
    private String password;
    private String phone;
    private String name;
    private String lasName;

    public UserDetails(String email, String password, String phone, String name, String lasName) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.lasName = lasName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        if (email.matches(UserValidation.patternEmail))
            throw new Exception("Invalid argument");
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception {
        if (password.matches(UserValidation.patternPassword))
            throw new Exception("Invalid argument");
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLasName() {
        return lasName;
    }

    public void setLasName(String lasName) {
        this.lasName = lasName;
    }
}
