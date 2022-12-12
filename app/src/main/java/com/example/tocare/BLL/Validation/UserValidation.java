package com.example.tocare.BLL.Validation;

import android.telephony.PhoneNumberUtils;

import java.net.PasswordAuthentication;
import java.util.EnumMap;

public class UserValidation {
    public static final String userNamePattern = "^[a-zA-Z][a-zA-Z0-9_]{6,19}$";
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String passwordPattern = "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){2,})(?=(.*[0-9]){2,})(?=(.*[!@#$%^&*()\\-__+.]){1,}).{8,}$";
    public static final String fullNamePattern = "^[a-zA-Z][a-z ]{3,19}$";


    public static String checkIsEmailOrPhoneNumber(String EmailPhone) {

        if (PhoneNumberUtils.isGlobalPhoneNumber(EmailPhone))
            return "Phone";
        if (EmailPhone.matches(emailPattern))
            return "Email";

        return "invalid Argument";

    }

    public static boolean SignUpValidation(String email, String password, String userName, String fullName,String phone,String cPassword) {

        return isValidUserName(userName) && isValidFullName(fullName) && isStrongPassword(password) && isValidEmail(email);
    }

    public static boolean isValidEmail(String email) {
        if (email.matches(emailPattern))
            return true;
        return false;

    }

    public static boolean isValidUserName(String userName) {
        if (userName.matches(userNamePattern))
            return true;
        return false;

    }

    public static boolean isStrongPassword(String password) {
        if (password.matches(passwordPattern))
            return true;
        return false;

    }

    public static boolean isValidFullName(String fullName) {
        if (fullName.matches(fullNamePattern))
            return true;
        return false;

    }

    private UserValidation() {
    }

    ;

}
