package com.example.tocare;

public class Register extends UserDetails {

    private String ConformPassword;

    public Register(String email, String password, String phone, String name, String lasName, String conformPassword) {
        super(email, password, phone, name, lasName);
        ConformPassword = conformPassword;
    }

}
