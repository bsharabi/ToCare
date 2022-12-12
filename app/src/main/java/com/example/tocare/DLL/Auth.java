package com.example.tocare.DLL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth {

    private static Auth single_instance = null;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    private Auth() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public static Auth getInstance() {
        if (single_instance == null)
            single_instance = new Auth();
        return single_instance;
    }





}
