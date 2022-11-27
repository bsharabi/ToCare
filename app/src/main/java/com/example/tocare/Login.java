package com.example.tocare;

import android.content.Intent;
import android.view.View;

public class Login implements View.OnClickListener {


    private String email;
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void onClick(View view) {

    }
}
