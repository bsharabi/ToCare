package com.example.tocare.DAL.api;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Listener.UploadCallback;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public interface IData {
    // ----------------- init ---------------


    AuthCredential createCredentialSignIn(String email, String password);

    //----------------- Get -------------------


    UserModel getCurrentUser();




    //----------------- Post ------------------
    void addPost(String postID, @NonNull Task task, UploadCallback callback);


    //----------------- Put -------------------



    //----------------- Delete ----------------

    void deleteUserById(String userId);

    // ------------------ destroy -------------------
    void destroyListenerRegistration();

}



