package com.example.tocare.DAL;

import android.app.Activity;

import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.BLL.Listener.FollowingCallback;
import com.example.tocare.BLL.Listener.OnChangeCallback;
import com.example.tocare.BLL.Listener.SearchCallback;
import com.example.tocare.BLL.Listener.UploadCallback;
import com.example.tocare.BLL.Listener.UserCallback;
import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.BLL.Model.Comment;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.DAL.api.IData;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.BLL.Listener.Refresh;
import com.example.tocare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Map<String, Object> following;
    private Map<String, Object> followers;
    private Map<String, Object> saved;

    private List<ListenerRegistration> listenerRegistrations;
    private Map<String, List<Task>> tasks;


    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final FirebaseUser firebaseUser;
    private final FirebaseAuth mAuth;
    private final CollectionReference userCollectionRef;
    private final CollectionReference taskCollectionRef;
    private final CollectionReference commentsCollectionRef;
    private final CollectionReference followingCollectionRef;
    private final CollectionReference followersCollectionRef;
    private final CollectionReference ChildrenCollectionRef;
    private final CollectionReference postCollectionRef;
    private final CollectionReference notificationCollectionRef;
    private Refresh refresh;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

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
        this.following = new HashMap<>();
        this.followers = new HashMap<>();
        this.saved = new HashMap<>();

        this.listenerRegistrations = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        userCollectionRef = db.collection("User");
        taskCollectionRef = db.collection("Task");
        followingCollectionRef = db.collection("Following");
        followersCollectionRef = db.collection("Followers");
        postCollectionRef = db.collection("Posts");
        ChildrenCollectionRef = db.collection("Children");
        notificationCollectionRef = db.collection("Notification");
        commentsCollectionRef = db.collection("Comments");

    }

    public static Data getInstance() {
        if (single_instance == null)
            single_instance = new Data();
        return single_instance;
    }

    public void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, FirebaseCallback callback) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid());
        reference.set(userModel)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        callback.onCallback(true, null, firebaseUser);

                    }
                }).addOnFailureListener(e -> {
                    callback.onCallback(false, e, null);
                });
    }

    public void startPhoneNumberVerification(String phoneNumber, Activity activity, PhoneCallback callback) {
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

    public void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token, Activity activity) {
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

    public void signInWithAuthCredential(PhoneAuthCredential credential, PhoneCallback phoneCallback) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    phoneCallback.onCallback(true, null, task);
                }).addOnFailureListener(failure -> {
                    phoneCallback.onCallback(false, failure, null);

                });
    }

    public void verifyPhoneNumberWithCode(String code, String mVerificationId, PhoneCallback callback) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithAuthCredential(credential, callback);
    }

    @Override
    public AuthCredential createCredentialSignIn(String email, String password) {
        credential = EmailAuthProvider.getCredential(email, password);
        return credential;
    }

    public void updateUserUI(Callback callback) {

        ListenerRegistration listener = userCollectionRef
                .document(firebaseUser.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);

                        callback.onCallback(false, error, null);
                        return;
                    }
                    if (value != null && value.exists()) {
                        users.put(firebaseUser.getUid(), value.toObject(UserModel.class));

                        getAllFollowingSnapShot();
                        getAllFollowersSnapShot();
                        callback.onCallback(true, null, firebaseUser);
                        getAllFollowing(callback);
                        Log.d(TAG, "DocumentReferenceCurrentUser:success");
                        Log.w(TAG, "document:updateUserUI exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:updateUserUI does not exists.");
                    }
                });
        listenerRegistrations.add(listener);
    }

    @Override
    public UserModel getCurrentUser() {
        return users.get(firebaseUser.getUid());
    }

    public String getCurrentUserId() {
        return firebaseUser.getUid();
    }

    @Override
    public void addUserSnapshot(String UserId) {
        ListenerRegistration mainReferenceUser = userCollectionRef
                .document(UserId)
                .addSnapshotListener((value, error) -> {
                    // עדכון ב notification
                    users.put(UserId, value.toObject(User.class));
                    System.out.println("------updateUsersUI::User--------");
                    Log.d(TAG, "DocumentReferenceCurrentUser:success");

                });
        listenerRegistrations.add(mainReferenceUser);
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

    public UserModel getUserChildById(String userID) {
        return users.get(userID);
    }


    public void getFollowingByUserId(String userId, FollowingCallback callback) {
        followingCollectionRef.document(userId).collection("following").get().addOnCompleteListener(task -> {
            List<String> list = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentSnapshot snap : task.getResult().getDocuments()) {
                    list.add(snap.getId());
                }
                callback.result(true, list);

            }
        }).addOnFailureListener(e -> callback.result(false, null));

    }

    public void getPostsByUserId(List<String> following, FollowingCallback callback) {


    }

    public void getUserById(String userID, UserCallback callback) {
        userCollectionRef.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel user = task.getResult().toObject(UserModel.class);
                callback.result(true, user);
            }
        }).addOnFailureListener(e -> {
            callback.result(false, null);
        });
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

    public void addChildren(FirebaseCallback callback, String newUserId) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Admin admin = (Admin) getCurrentUser();
                        ChildrenCollectionRef.document("Parent:" + admin.getId())
                                .collection(admin.getId())
                                .document(newUserId)
                                .set(new HashMap<String, Object>() {{
                                    put("isChild", true);
                                }})
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
        Iterator<ListenerRegistration> iterator = listenerRegistrations.iterator();
        while (iterator.hasNext())
            iterator.next().remove();
        FirebaseAuth.getInstance().signOut();
    }

    public void destroyData() {
        this.users.clear();
        this.tasks.clear();
        this.following.clear();
        this.followers.clear();
        this.saved.clear();
        this.listenerRegistrations.clear();

    }

    public void uploadImage(Uri ImageUri, String uriFileExtension, String postId, UploadCallback callback) {
        if (ImageUri != null) {

            StorageReference storageReference = storage.getReference("posts").child(System.currentTimeMillis() + "." + postId + uriFileExtension);
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            UploadTask task = storageReference.putFile(ImageUri, metadata);
            task.continueWithTask(taskWith -> {
                com.google.android.gms.tasks.Task<Uri> downloadUrl = null;
                if (taskWith.isComplete()) {
                    downloadUrl = storageReference.getDownloadUrl();
                }
                return downloadUrl;
            }).addOnCompleteListener(taskComplete -> {
                if (taskComplete.isSuccessful()) {
                    Uri downloadUri = taskComplete.getResult();
                    String myUri = downloadUri.toString();
                    callback.onSuccessUpload(true, null, myUri, postId);
                }
            }).addOnFailureListener(e -> {
                callback.onSuccessUpload(false, e, null, postId);
            });

        }

    }

    public String getRandomIdByCollectionName(String collectionName) {
        return db.collection("Posts").document().getId();
    }

    public void setRefresh(Refresh refresh) {
        this.refresh = refresh;
    }

    public void destroy() {
        destroyListenerRegistration();
        destroyData();
        single_instance = null;
    }

    public Map<String, Object> getFollowing() {
        return following;
    }

    public Map<String, Object> getFollowers() {
        return followers;
    }

    public Map<String, Object> getSaved() {
        return saved;
    }

    public void searchUsersAdmin(String search, SearchCallback callback) {
        Query query = db.collection("User")
                .whereEqualTo("admin", false)
                .orderBy("userName")
                .startAt(search)
                .endAt(search + "\uf8ff");
        query.addSnapshotListener((value, error) -> {
            if (value != null) {
                List<UserModel> list = new ArrayList<>();
                for (DocumentSnapshot snap : value.getDocuments()) {
                    list.add(snap.toObject(UserModel.class));
                }
                callback.result();
            }
        });
    }

    //    public void getAllFollowersByUser(String userId,  Map<String, Object> mFollowers) {
//        ListenerRegistration listener = db.collection("Followers")
//                .document(userId).addSnapshotListener((value, error) -> {
//                    if (error != null) {
//                        Log.w(TAG, "Listen failed.", error);
//                        return;
//                    }
//                    if (value != null && value.exists()) {
//                        mFollowers = value.getData();
//                        Log.w(TAG, "document exists.");
//                        // The document exists
//
//                    } else {
//                        // The document does not exist
//                        Log.w(TAG, "document does not exists.");
//
//                    }
//                });
//        listenerRegistrations.add(listener);
//    }
//
//    public void getAllFollowingByUser(String userId) {
//        ListenerRegistration listener = FirebaseFirestore.getInstance().collection("Following")
//                .document(userId).addSnapshotListener((value, error) -> {
//                    if (error != null) {
//                        Log.w(TAG, "Listen failed.", error);
//                        return;
//                    }
//
//                    if (value != null && value.exists()) {
//                        following = value.getData();
//                        System.out.println(following);
//                        Log.w(TAG, "document exists.");
//                        // The document exists
//
//                    } else {
//                        // The document does not exist
//                        Log.w(TAG, "document does not exists.");
//
//                    }
//                });
//        listenerRegistrations.add(listener);
//    }
    public void searchUsers(String search, List<UserModel> list, SearchCallback callback) {
        Query query = db.collection("User")
                .orderBy("userName")
                .startAt(search)
                .endAt(search + "\uf8ff");
        query.addSnapshotListener((value, error) -> {
            list.clear();
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            if (value != null && !value.isEmpty()) {
                for (DocumentSnapshot snap : value.getDocuments()) {
                    list.add(snap.toObject(UserModel.class));
                }
                callback.result();
                Log.w(TAG, "document:searchUsers exists.");
                // The document exists

            } else {
                callback.result();
                // The document does not exist
                Log.w(TAG, "document:searchUsers does not exists.");
            }

        });
    }

    public void getAllFollowers(Callback callback) {
        followersCollectionRef
                .document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        if (task != null && task.getResult().exists()) {
                            followers = task.getResult().getData();
                            callback.onSuccess(true, null);
                            Log.w(TAG, "document:AllFollowers exists.");
                            // The document exists
                        } else {
                            callback.onSuccess(true, null);
                            // The document does not exist
                            Log.w(TAG, "document:AllFollowers does not exists.");
                        }
                    }
                }).addOnFailureListener(error -> {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onSuccess(false, error);
                });
    }

    public void getAllFollowing(Callback callback) {
        followingCollectionRef
                .document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isComplete()) {

                        if (task != null && task.getResult().exists()) {
                            following = task.getResult().getData();

                            getAllFollowers(callback);
                            Log.w(TAG, "document:AllFollowing exists.");
                            // The document exists

                        } else {
                            getAllFollowers(callback);
                            // The document does not exist
                            Log.w(TAG, "document:AllFollowing does not exists.");
                        }
                        following.put(firebaseUser.getUid(),true);
                    }
                }).addOnFailureListener(error -> {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onSuccess(false, error);
                });


    }

    public void getAllFollowersSnapShot() {
        ListenerRegistration listener = followersCollectionRef
                .document(firebaseUser.getUid()).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);

                        return;
                    }
                    if (value != null && value.exists()) {
                        followers = value.getData();
                        System.out.println();
                        Log.w(TAG, "document exists.");
                        // The document exists
                    } else {

                        // The document does not exist
                        Log.w(TAG, "document does not exists.");

                    }
                });
        listenerRegistrations.add(listener);
    }

    public void getAllFollowingSnapShot() {
        System.out.println("\n\n\n\n-----------------------------------------------\n\n\n\n");
        ListenerRegistration listener = followingCollectionRef
                .document(firebaseUser.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        following = value.getData();
                        following.put(firebaseUser.getUid(),true);
                        System.out.println(following);
                        Log.w(TAG, "document exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document does not exists.");
                    }
                });
        listenerRegistrations.add(listener);
    }

    public void addFollow(String followUser, final TextView textView) {
        followingCollectionRef
                .document(firebaseUser.getUid())
                .set(new HashMap<String, Object>() {{
                    put(followUser, true);
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    textView.setText(R.string.Following);
                })
                .addOnFailureListener(e -> {
                    textView.setText(R.string.Follow);
                });

        followersCollectionRef
                .document(followUser)
                .set(new HashMap<String, Boolean>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    textView.setText(R.string.Following);
                })
                .addOnFailureListener(e -> {
                    textView.setText(R.string.Follow);
                });
    }

    public void deleteFollow(String followUser, final TextView textView) {
        followingCollectionRef
                .document(firebaseUser.getUid())
                .set(new HashMap<String, Object>() {{
                    put(followUser, FieldValue.delete());
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> {

                    textView.setText(R.string.Follow);
                })
                .addOnFailureListener(e -> {
                    textView.setText(R.string.Following);
                });

        followersCollectionRef
                .document(followUser)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> {
                    textView.setText(R.string.Follow);
                })
                .addOnFailureListener(e -> {
                    textView.setText(R.string.Following);
                });
    }

    public void getAllSaved(String userId) {
        ListenerRegistration listener = db.collection("Saved")
                .document(userId).addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        saved = value.getData();
                        Log.w(TAG, "document exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document does not exists.");

                    }
                });
        listenerRegistrations.add(listener);
    }

    public void addSavedItem(String postId) {
        db.collection("Saved")
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Save Item : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Save Item : " + postId + " Failed.");

                });
    }

    public void deleteSavedItem(String postId) {
        FirebaseFirestore.getInstance().collection("Saved")
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Item : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Delete Item : " + postId + " Failed.");

                });
    }

    public void addLikeToTask(String postId) {
        db.collection("Likes")
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Like added : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Task add : " + postId + " Failed.");

                });
    }

    public void deleteLikeFromTask(String postId) {
        db.collection("Likes")
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Like deleted : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Task deleted : " + postId + " Failed.");

                });
    }

    public void getLikeByPostId(final TextView likes, String postId) {
        ListenerRegistration listener = db.collection("Likes").document(postId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        likes.setText(value.getData().size() + " likes");
                        Log.w(TAG, "document:LikeByPostId exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        likes.setText("0" + " likes");
                        Log.w(TAG, "document:LikeByPostId does not exists.");

                    }
                });
        listenerRegistrations.add(listener);
    }

    public void isLiked(final ImageView imageView, String postId) {
        ListenerRegistration listener = db.collection("Likes").document(postId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        if (value.getData().containsKey(firebaseUser.getUid())) {
                            imageView.setImageResource(R.drawable.ic_red_like);
                            imageView.setTag("liked");
                        } else {
                            imageView.setImageResource(R.drawable.ic_like);
                            imageView.setTag("like");
                        }
                        Log.w(TAG, "document:Liked exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:Liked does not exists.");
                    }
                });
        listenerRegistrations.add(listener);
    }

    public void isSaved(final ImageView imageView, String postId) {
        ListenerRegistration listener = db.collection("Saved").document(postId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        if (value.getData().containsKey(firebaseUser.getUid())) {
                            imageView.setImageResource(R.drawable.ic_saved);
                            imageView.setTag("saved");
                        } else {
                            imageView.setImageResource(R.drawable.ic_save);
                            imageView.setTag("save");
                        }
                        Log.w(TAG, "document:Save exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:Save does not exists.");
                    }


                });
        listenerRegistrations.add(listener);
    }

    public void addPost(String postId, @NonNull Task task, UploadCallback callback) {
        db.collection("Posts")
                .document(postId)
                .set(task)
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Task added : " + postId + " Success.");
                                callback.onSuccess(true, null);
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Task add : " + postId + " Failed.");
                    callback.onSuccess(false, e);
                });
    }

    public void deletePost(String postId) {
        postCollectionRef
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                                deleteAllLike(postId);
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Delete Post: " + postId + " Failed.");
                });
    }

    public void deleteAllLike(String postId) {
        db.collection("Likes")
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                                deleteAllComment(postId);
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Delete Post: " + postId + " Failed.");
                });
    }

    public void deleteAllComment(String postId) {
        db.collection("Comments")
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                deleteAllSaved(postId);
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Delete Post: " + postId + " Failed.");
                });
    }

    public void deleteAllSaved(String postId) {
        db.collection("Saved")
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Delete Post: " + postId + " Failed.");
                });
    }

    public void getAllPost(List<Task> mTask, OnChangeCallback callback) {
        ListenerRegistration listener = db.collection("Posts")
                .whereIn("author", Arrays.asList(getFollowing().keySet().toArray()))
                .addSnapshotListener((value, error) -> {
                    mTask.clear();
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            mTask.add(doc.toObject(Task.class));
                        }
                        Log.w(TAG, "document:Post exists.");
                        // The document exists
                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:Post does not exists.");
                    }
                    callback.onChange();

                });
        listenerRegistrations.add(listener);
    }

    public void getAllComment(List<Comment> mComments, String postId, OnChangeCallback callback) {

        ListenerRegistration listener =
                commentsCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                mComments.clear();
                                Map map = value.getData();
                                for (Object obj : map.values()) {
                                    String commentId = (String) ((Map) obj).get("commentId");
                                    String author = (String) ((Map) obj).get("author");
                                    String comment = (String) ((Map) obj).get("comment");
                                    String publish = (String) ((Map) obj).get("publish");
                                    Comment commentObj = new Comment(commentId,postId, author, comment, publish);
                                    mComments.add(commentObj);

                                }
                                System.out.println(mComments);
                                Log.w(TAG, "document:AllComment exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:AllComment does not exists.");

                            }
                            callback.onChange();
                        });
        listenerRegistrations.add(listener);
    }

    public void addComment(String postId, @NonNull Comment comment, @NonNull final EditText newComment) {
        String key = commentsCollectionRef.document().getId();
        newComment.setEnabled(false);
        comment.setCommentId(key);
        commentsCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(key, comment);
                }}, SetOptions.merge())
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Comment added : " + postId + " Success.");
                                newComment.setText("");
                                newComment.setEnabled(true);
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Comment add : " + postId + " Failed.");
                    newComment.setEnabled(true);
                });
    }

    public void deleteComment(String postId, String commentId) {
        commentsCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {
                    {
                        put(commentId, FieldValue.delete());
                    }
                }, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Like deleted : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Task deleted : " + postId + " Failed.");

                });
    }

    public void getCommentByPostId(final TextView likes, String postId) {
        ListenerRegistration listener =
                commentsCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                likes.setText("View All " + value.getData().size() + " Comments");
                                Log.w(TAG, "document:CommentByPostId exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                likes.setText("0" + " Comments");
                                Log.w(TAG, "document:CommentByPostId does not exists.");

                            }
                        });
        listenerRegistrations.add(listener);
    }

}




