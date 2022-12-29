package com.example.tocare.DAL;

import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.DAL.api.IData;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.BLL.Listener.Refresh;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class Data implements IData {

    private static Data single_instance = null;
    private static final String TAG = "DataUser";
    private static AuthCredential credential;
    private static PhoneCallback phoneCallback;
    private boolean isAdmin;
    private Map<String, UserModel> users;
    private Map<String, List<Task>> tasks;
    private Map<String, ListenerRegistration> listenerRegistrationTasks;
    private Map<String, ListenerRegistration> listenerRegistrationUser;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private CollectionReference userCollectionRef;
    private CollectionReference taskCollectionRef;
    private CollectionReference notificationCollectionRef;
    private Refresh refresh;

    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            phoneCallback.onVerificationCompleted(true, credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            phoneCallback.onVerificationFailed(e);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            phoneCallback.onCodeSent(verificationId, token);
        }
    };

    private Data() {
        this.users = new HashMap<>();
        this.tasks = new HashMap<>();
        this.listenerRegistrationTasks = new HashMap<>();
        this.listenerRegistrationUser = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        userCollectionRef = db.collection("User");
        taskCollectionRef = db.collection("Task");
//        notificationCollectionRef=db.collection("Notification");

    }

    public static Data getInstance() {
        if (single_instance == null)
            single_instance = new Data();
        return single_instance;
    }

    public  void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, FirebaseCallback callback) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid());
        reference.set(userModel)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        createUserTask(firebaseUser, callback);
                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(false, e, null);
                });
    }

    private  void createUserTask(@NonNull FirebaseUser firebaseUser, FirebaseCallback callback) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Task")
                .document(firebaseUser.getUid());
        Map<String, List<com.example.tocare.BLL.Model.Task>> mapTask = new HashMap<>();
        mapTask.put(firebaseUser.getUid(), new ArrayList<>());
        reference.set(mapTask)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {

                        callback.onCallback(true, null, firebaseUser);
                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(false, e, null);
                });
    }

    public  void startPhoneNumberVerification(String phoneNumber, Activity activity, PhoneCallback callback) {
        phoneCallback = callback;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public  void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token, Activity activity) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public  void signInWithAuthCredential(PhoneAuthCredential credential, PhoneCallback phoneCallback) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    phoneCallback.onCallback(true, null, task);
                }).addOnFailureListener(failure -> {
                    phoneCallback.onCallback(false, failure, null);

                });
    }

    public  void verifyPhoneNumberWithCode(String code, String mVerificationId, PhoneCallback callback) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithAuthCredential(credential, callback);
    }

    public void buildUser(FirebaseCallback callback) {
        userCollectionRef
                .document(firebaseUser.getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            isAdmin = (boolean) map.get("admin");

                            if (isAdmin) {
                                Admin admin = document.toObject(Admin.class);
                                users.put(admin.getId(), admin);
                                updateAdminUI(admin);
                                getUsersData(admin);
                            } else {
                                User user = document.toObject(User.class);
                                users.put(user.getId(), user);
                                updateUserUI(user);
                            }
                            callback.onCallback(true, null, firebaseUser);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(false, e, null);
                    Log.d(TAG, "get failed with ", e);
                });
    }

    @Override
    public AuthCredential createCredentialSignIn(String email, String password) {
        credential = EmailAuthProvider.getCredential(email, password);
        return credential;
    }

    private void updateUserUI(@NonNull User user) {

        DocumentReference reference = userCollectionRef
                .document(firebaseUser.getUid());

        ListenerRegistration mainReference = reference
                .addSnapshotListener((value, error) -> {
                    // עדכון ב notification
                    users.put(user.getId(), value.toObject(User.class));
                    System.out.println("------updateUserUI::User--------");
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                });

        listenerRegistrationUser.put(firebaseUser.getUid(), mainReference);

        DocumentReference referenceTask =
                taskCollectionRef
                        .document(user.getId());

        ListenerRegistration mainReferenceTask = referenceTask
                .addSnapshotListener((value, error) -> {
                    System.out.println("------updateUserUI::Task--------");
                    Map<String, Object> masTask = value.getData();
                    Gson gson = new Gson();
                    List<Task> listTask = new ArrayList<>();
                    String tasksString = gson.toJson(masTask.get(user.getId()));
                    List l = gson.fromJson(tasksString, List.class);
                    for (Object obj : l) {
                        String fromJson = gson.toJson(obj);
                        Task task = gson.fromJson(fromJson, Task.class);
                        listTask.add(task);
                    }
                    tasks.put(user.getId(), listTask);
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                });
        listenerRegistrationTasks.put(firebaseUser.getUid(), mainReferenceTask);
    }

    private void updateAdminUI(@NonNull Admin admin) {
        DocumentReference reference = userCollectionRef
                .document(firebaseUser.getUid());

        ListenerRegistration mainReference = reference
                .addSnapshotListener((value, error) -> {
                    // עדכון ב notification
                    users.put(admin.getId(), value.toObject(Admin.class));
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                });

        listenerRegistrationUser.put(firebaseUser.getUid(), mainReference);

        DocumentReference referenceTask = taskCollectionRef
                .document(admin.getId());

        ListenerRegistration mainReferenceTask = referenceTask
                .addSnapshotListener((value, error) -> {
                    Map<String, Object> masTask = value.getData();
                    Gson gson = new Gson();
                    List<Task> listTask = new ArrayList<>();
                    String tasksString = gson.toJson(masTask.get(value.getId()));
                    List l = gson.fromJson(tasksString, List.class);
                    for (Object obj : l) {
                        String fromJson = gson.toJson(obj);
                        Task task = gson.fromJson(fromJson, Task.class);
                        listTask.add(task);

                    }
                    tasks.put(admin.getId(), listTask);
                    System.out.println("------updateAdminUI::Task--------");
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
                });
        listenerRegistrationTasks.put(firebaseUser.getUid(), mainReferenceTask);
    }

    private void getUsersData(@NonNull Admin admin) {
        List<String> childrenId = admin.getChildrenId();
        for (String id : childrenId) {
            addUserSnapshot(id);
            addTaskSnapshot(id);
        }
    }

    @Override
    public UserModel getCurrentUser() {
        return users.get(firebaseUser.getUid());
    }

    @Override
    public void addUserSnapshot(String UserId) {
        DocumentReference referenceUser = userCollectionRef
                .document(UserId);
        ListenerRegistration mainReferenceUser = referenceUser
                .addSnapshotListener((value, error) -> {
                    // עדכון ב notification
                    users.put(UserId, value.toObject(User.class));
                    System.out.println("------updateUsersUI::User--------");
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");

                });
        listenerRegistrationUser.put(UserId, mainReferenceUser);
    }

    @Override
    public void addTaskSnapshot(String taskId) {
        DocumentReference referenceTask = taskCollectionRef
                .document(taskId);
        ListenerRegistration mainReferenceTask = referenceTask
                .addSnapshotListener((value, error) -> {
                    // עדכון ב notification
                    Map<String, Object> masTask = value.getData();
                    Gson gson = new Gson();
                    List<Task> listTask = new ArrayList<>();
                    String tasksString = gson.toJson(masTask.get(taskId));
                    List l = gson.fromJson(tasksString, List.class);
                    for (Object obj : l) {
                        String fromJson = gson.toJson(obj);
                        Task task = gson.fromJson(fromJson, Task.class);
                        listTask.add(task);

                    }
                    tasks.put(taskId, listTask);
                    System.out.println("------updateUsersUI::Task--------");
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");
//                        int reload = navController.getCurrentDestination().getId();
//                        navController.navigate(reload);
                });
        listenerRegistrationTasks.put(taskId, mainReferenceTask);
    }

    public void addNewTask(String userID, Task task) {
        tasks.get(userID).add(task);
    }

    public void deleteTaskByUserId(String userID, Task task) {
        tasks.get(userID).remove(task);
        updateListTaskByUserId(userID);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<Task> getTaskByUserId(String UserId) {
        return tasks.get(UserId);
    }

    public com.google.android.gms.tasks.Task<Void> updateListTaskByUserId(String UserID) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("Task")
                .document(UserID);
        return reference.update(UserID, getTaskByUserId(UserID));
    }

    public List<Task> getAllTask() {

        List<Task> mTask = new ArrayList<>();
        for (Map.Entry<String, List<Task>> entry : tasks.entrySet()) {
            mTask.addAll(entry.getValue());
        }

        return mTask;
    }

    public List<UserModel> getAllUser() {
        System.out.println(users);
       List<UserModel> list = new ArrayList<>(users.values());
       list.remove(getCurrentUser());
        return list;
    }

    public UserModel getUserById(String userID) {
        return users.get(userID);
    }

    @Override
    public List<Task> getTasksByUser(String userId) {
        return null;
    }

    @Override
    public void getAllNotification() {

    }

    @Override
    public void getNotificationByUserId(String userId) {

    }

    @Override
    public void createNewTask() {

    }

    @Override
    public void createNewUserByPhone() {

    }

    @Override
    public void uploadImage() {

    }

    @Override
    public void updateUserTaskByTaskId(String taskId) {

    }

    @Override
    public void updateUser(UserModel user) {

    }

    @Override
    public void updateTaskByTaskIdAndPosition(String taskId, int position) {

    }

    @Override
    public void updateChildren(FirebaseCallback callback, String newUserId) {
        addUserSnapshot(newUserId);
        addTaskSnapshot(newUserId);

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Admin admin = (Admin) getCurrentUser();
                        userCollectionRef.document(admin.getId())
                                .update("childrenId", admin.getChildrenId())
                                .addOnSuccessListener(aVoid -> {
                                    callback.onSuccess(true, null);
                                })
                                .addOnFailureListener(e -> callback.onSuccess(false, e));
                    }

                });
    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteAllUsers() {

    }

    @Override
    public void deleteAllTasksByUserId() {


    }

    @Override
    public void deleteUserTaskByTaskId(String taskId) {

    }

    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void destroyListenerRegistration() {
        Iterator<ListenerRegistration> iterator = listenerRegistrationUser.values().iterator();
        while (iterator.hasNext())
            iterator.next().remove();

        iterator = listenerRegistrationTasks.values().iterator();
        while (iterator.hasNext())
            iterator.next().remove();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public String toString() {
        return "Data{" +
                "\nisAdmin=" + isAdmin +
                "\n, users=" + users +
                "\n, tasks=" + tasks +
                "\n, listenerRegistrationTasks=" + listenerRegistrationTasks +
                "\n, listenerRegistrationUser=" + listenerRegistrationUser +
                "\n, db=" + db +
                "\n, storage=" + storage +
                "\n, firebaseUser=" + firebaseUser +
                "\n, mAuth=" + mAuth +
                "\n, userCollectionRef=" + userCollectionRef +
                "\n, taskCollectionRef=" + taskCollectionRef +
                "\n, notificationCollectionRef=" + notificationCollectionRef +
                "\n, refresh=" + refresh +
                '}';
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }
}
