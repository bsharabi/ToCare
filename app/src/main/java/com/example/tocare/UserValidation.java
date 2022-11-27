package com.example.tocare;

public final class UserValidation {

    static String patternPassword = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";

    static String patternEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    private UserValidation() {
    }
}
