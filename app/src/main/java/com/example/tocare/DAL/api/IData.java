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
    List<UserModel> getAllUser();

    List<Task> getAllTask();

    UserModel getUserChildById(String userId);

    List<Task> getTasksByUser(String userId);

    void getAllNotification();

    void getNotificationByUserId(String userId);

    UserModel getCurrentUser();

    void addUserSnapshot(String UserId);




    //----------------- Post ------------------
    void addPost(String postID, @NonNull Task task, UploadCallback callback);

    void createNewUserByPhone();

    void uploadImage();

    //----------------- Put -------------------

    void updateUserTaskByTaskId(String taskId);

    void updateUser(UserModel user);

    void updateTaskByTaskIdAndPosition(String taskId, int position);


    //----------------- Delete ----------------
    void deleteAllTasks();

    void deleteAllUsers();

    void deleteAllTasksByUserId();


    void deleteUserTaskByTaskId(String taskId);

    void deleteUserById(String userId);

    // ------------------ destroy -------------------
    void destroyListenerRegistration();


}
