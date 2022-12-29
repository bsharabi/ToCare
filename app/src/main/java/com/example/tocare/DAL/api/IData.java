package com.example.tocare.DAL.api;

import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public interface IData {
    // ----------------- init ---------------

    /**
     * @param callback
     */
    void buildUser(FirebaseCallback callback);

    AuthCredential createCredentialSignIn(String email, String password);

    //----------------- Get -------------------
    List<UserModel> getAllUser();

    List<Task> getAllTask();

    UserModel getUserById(String userId);

    List<Task> getTasksByUser(String userId);

    void getAllNotification();

    void getNotificationByUserId(String userId);

    UserModel getCurrentUser();

    void addUserSnapshot(String UserId);

    void addTaskSnapshot(String taskId);




    //----------------- Post ------------------
    void createNewTask();

    void createNewUserByPhone();

    void uploadImage();

    //----------------- Put -------------------

    void updateUserTaskByTaskId(String taskId);

    void updateUser(UserModel user);

    void updateTaskByTaskIdAndPosition(String taskId, int position);

    void updateChildren(FirebaseCallback callback,String newUserId);

    //----------------- Delete ----------------
    void deleteAllTasks();

    void deleteAllUsers();

    void deleteAllTasksByUserId();


    void deleteUserTaskByTaskId(String taskId);

    void deleteUserById(String userId);

    // ------------------ destroy -------------------
    void destroyListenerRegistration();


}
