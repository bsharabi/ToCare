package com.example.tocare.BLL.Listener;


import com.google.firebase.auth.AuthCredential;

public interface FirebaseLoginCallback {
    void onSuccess(boolean success, Exception e, AuthCredential credential);
}
