package com.example.tocare.BLL.Listener;

import com.google.firebase.auth.FirebaseUser;

public interface FirebaseCallback extends Callback {
    void onCallback(boolean success, Exception e, FirebaseUser user);
    void onSuccess(boolean success, Exception e);

}