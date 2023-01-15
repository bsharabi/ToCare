package com.example.tocare.DAL;

import android.annotation.SuppressLint;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.BLL.Listener.CallbackNotification;
import com.example.tocare.BLL.Listener.OnChangeCallback;
import com.example.tocare.BLL.Listener.SearchCallback;
import com.example.tocare.BLL.Listener.TaskCallback;
import com.example.tocare.BLL.Listener.UploadCallback;
import com.example.tocare.BLL.Listener.UserCallback;
import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.BLL.Model.Comment;

import com.example.tocare.BLL.Model.Comparing;
import com.example.tocare.BLL.Model.Notification;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.DAL.api.IData;
import com.example.tocare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Data implements IData {

    private static Data single_instance = null;
    private static final String TAG = "DataUser";

    private UserModel currentUser;
    private Map<String, Object> following;
    private Map<String, Object> followers;
    private Map<String, Object> children;
    private final List<Notification> notifications;
    private final Map<String, ListenerRegistration> listenerRegistrations;

    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final FirebaseUser firebaseUser;
    private final CollectionReference userCollectionRef;
    private final CollectionReference commentsCollectionRef;
    private final CollectionReference followingCollectionRef;
    private final CollectionReference followersCollectionRef;
    private final CollectionReference childrenCollectionRef;
    private final CollectionReference postCollectionRef;
    private final CollectionReference savedCollectionRef;
    private final CollectionReference likesCollectionRef;
    private final CollectionReference notificationCollectionRef;

    private Data() {
        this.following = new HashMap<>();
        this.followers = new HashMap<>();
        this.children = new HashMap<>();
        this.notifications = new ArrayList<>();

        this.listenerRegistrations = new HashMap<>();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        userCollectionRef = db.collection("User");
        followingCollectionRef = db.collection("Following");
        followersCollectionRef = db.collection("Followers");
        postCollectionRef = db.collection("Posts");
        childrenCollectionRef = db.collection("Children");
        savedCollectionRef = db.collection("Saved");
        commentsCollectionRef = db.collection("Comments");
        likesCollectionRef = db.collection("Likes");
        notificationCollectionRef = db.collection("Notification");

    }

    public static Data getInstance() {
        if (single_instance == null)
            single_instance = new Data();
        return single_instance;
    }

    public void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, FirebaseCallback callback) {

        userCollectionRef
                .document(firebaseUser.getUid()).
                set(userModel)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        callback.onCallback(true, null, firebaseUser);
                        Log.w(TAG, "create new user DocumentReference:success");
                    }
                }).addOnFailureListener(e -> {
                    Log.w(TAG, "create new user DocumentReference:failed");
                    callback.onCallback(false, e, null);
                });
    }

    public void updateUserUI(Callback callback) {
        getAllFollowingSnapShot();
        getAllFollowersSnapShot();
        getAllNotificationSnapShot();
        getAllChildrenIdSnapShot();
        getAllFollowing(callback);
        ListenerRegistration listener =
                userCollectionRef
                        .document(firebaseUser.getUid())
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                callback.onCallback(false, error, null);
                                return;
                            }
                            if (value != null && value.exists()) {
                                currentUser = value.toObject(UserModel.class);
                                if (currentUser != null && currentUser.isAdmin())
                                    currentUser = value.toObject(Admin.class);
                                else
                                    currentUser = value.toObject(User.class);

                                callback.onCallback(true, null, firebaseUser);
                                Log.d(TAG, "DocumentReferenceCurrentUser:success");
                                Log.w(TAG, "document:updateUserUI exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:updateUserUI does not exists.");
                            }
                        });
        listenerRegistrations.put("user:currentUserModelSnapshot", listener);
    }

    private void getAllFollowers(Callback callback) {
        followersCollectionRef
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        if (task.getResult().exists()) {
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

    private void getAllFollowing(Callback callback) {
        followingCollectionRef
                .document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        if (task.getResult().exists()) {
                            following = task.getResult().getData();

                            getAllFollowers(callback);
                            Log.w(TAG, "document:AllFollowing exists.");
                            // The document exists

                        } else {
                            getAllFollowers(callback);
                            // The document does not exist
                            Log.w(TAG, "document:AllFollowing does not exists.");
                        }
                        following.put(firebaseUser.getUid(), true);
                    }
                }).addOnFailureListener(error -> {
                    Log.w(TAG, "Listen failed.", error);
                    callback.onSuccess(false, error);
                });


    }

    @Override
    public void getAllFollowingByUserId(String userId, final TextView following) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followingCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {

                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                following.setText(value.getData().size() + "");

                                Log.w(TAG, "document:getAllFollowingByUserId exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllFollowingByUserId does not exists.");
                            }

                        });
        listenerRegistrations.put("Temporary:getAllFollowingByUserId" + userId, listener);
    }

    @Override
    public void getAllFollowersByUserId(String userId, final TextView followers) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followersCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {

                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                followers.setText(value.getData().size() + "");
                                Log.w(TAG, "document:getAllFollowersByUserId exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllFollowersByUserId does not exists.");
                            }

                        });
        listenerRegistrations.put("Temporary:getAllFollowersByUserId" + userId, listener);
    }

    private void getAllFollowersSnapShot() {
        ListenerRegistration listener = followersCollectionRef
                .document(firebaseUser.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        followers = value.getData();
                        Log.w(TAG, "document:getAllFollowers exists.");
                        // The document exists
                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:getAllFollowers does not exists.");

                    }
                });
        listenerRegistrations.put("user:currentFollowersSnapShot", listener);
    }

    private void getAllFollowingSnapShot() {
        ListenerRegistration listener =
                followingCollectionRef
                        .document(firebaseUser.getUid())
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                following = value.getData();
                                assert following != null;
                                following.put(firebaseUser.getUid(), true);
                                Log.w(TAG, "document:getAllFollowing exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllFollowing does not exists.");
                            }
                        });
        listenerRegistrations.put("user:currentFollowingSnapShot", listener);
    }

    private void getAllChildrenIdSnapShot() {
        ListenerRegistration listener =
                childrenCollectionRef
                        .document(getCurrentUserId())
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                children = value.getData();
                                Log.w(TAG, "document:getAllChildren exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllChildren does not exists.");
                            }
                        });
        listenerRegistrations.put("user:currentUserAllChildrenIdSnapShot", listener);

    }

    private void getAllNotificationSnapShot() {
        ListenerRegistration listener =
                notificationCollectionRef
                        .whereEqualTo("toUserId", getCurrentUserId())
                        .addSnapshotListener((value, error) -> {
                            notifications.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    notifications.add(doc.toObject(Notification.class));
                                }

                                Log.w(TAG, "document:getAllChildren exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllChildren does not exists.");
                            }
                        });
        listenerRegistrations.put("user:getAllNotificationSnapShot", listener);

    }

    @Override
    public void getAllNotification(List<Notification> mNotification, CallbackNotification callback) {
        ListenerRegistration listener =
                notificationCollectionRef
                        .whereEqualTo("toUserId", getCurrentUserId())
                        .addSnapshotListener((value, error) -> {
                            mNotification.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                callback.onSuccess(false, error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {

                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    mNotification.add(doc.toObject(Notification.class));
                                }
                                System.out.println(mNotification);
                                Log.w(TAG, "document:getAllNotification exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllNotification does not exists.");
                            }
                            callback.onSuccess(true, null);
                        });
        listenerRegistrations.put("Temporary2:getAllNotificationSnapShot", listener);

    }

    @Override
    public void getAllChildren(List<User> mUser, OnChangeCallback callback) {

        ListenerRegistration listener =
                userCollectionRef
                        .whereEqualTo("parentId", getCurrentUserId())
                        .addSnapshotListener((value, error) -> {
                            mUser.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    mUser.add(doc.toObject(User.class));
                                }
                                Log.w(TAG, "document:getAllChildren exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllChildren does not exists.");
                            }
                            callback.onChange();

                        });
        listenerRegistrations.put("Temporary2:currentGetAllChildrenSnapShot", listener);

    }

    @Override
    public void getAllFollowingUser(List<UserModel> mUser, List<String> mFollowingId, OnChangeCallback callback) {
        System.out.println(mFollowingId);
        if (mFollowingId.isEmpty())
            return;
        ListenerRegistration listener =
                userCollectionRef
                        .whereIn("id", mFollowingId)
                        .addSnapshotListener((value, error) -> {
                            mUser.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    mUser.add(doc.toObject(UserModel.class));
                                }
                                Log.w(TAG, "document:getAllFollowingUser exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllChildren does not exists.");
                            }
                            callback.onChange();

                        });
        listenerRegistrations.put("Temporary2:getAllFollowingUser", listener);

    }

    @Override
    public void getAllFollowersUser(List<UserModel> mUser, List<String> mFollowersId, OnChangeCallback callback) {
        System.out.println(mFollowersId);
        if (mFollowersId.isEmpty())
            return;
        ListenerRegistration listener =
                userCollectionRef
                        .whereIn("id", mFollowersId)
                        .addSnapshotListener((value, error) -> {
                            mUser.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    mUser.add(doc.toObject(UserModel.class));
                                }
                                Log.w(TAG, "document:getAllFollowersUser exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllChildren does not exists.");
                            }
                            callback.onChange();

                        });
        listenerRegistrations.put("Temporary2:getAllFollowersUser", listener);

    }

    @Override
    public void getAllFollowingIdByUserId(String userId, List<String> mFollowingId, OnChangeCallback callback) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followingCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {
                            mFollowingId.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                mFollowingId.addAll(value.getData().keySet());

                                Log.w(TAG, "document:getAllFollowingIdByUserId exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllFollowingIdByUserId does not exists.");
                            }
                            callback.onChange();
                        });

        listenerRegistrations.put("Temporary2:getAllFollowingIdByUserId" + userId, listener);

    }

    @Override
    public void getAllFollowersIdByUserId(String userId, List<String> mFollowersId, OnChangeCallback callback) {

        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followersCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {
                            mFollowersId.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                mFollowersId.addAll(value.getData().keySet());

                                Log.w(TAG, "document:getAllFollowersIdByUserId exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:getAllFollowersIdByUserId does not exists.");
                            }
                            callback.onChange();
                        });

        listenerRegistrations.put("Temporary2:getAllFollowersIdByUserId" + userId, listener);

    }

    @Override
    public void updateUserById(String userId, Map<String, Object> user, OnChangeCallback callback) {
        userCollectionRef
                .document(userId)
                .update(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onChange();
                        Log.w(TAG, "document:updateUser:success");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onChange();
                    Log.w(TAG, "document:updateUser: failed.");
                });

    }


    public UserModel getCurrentUser() {
        return currentUser;
    }

    @NonNull
    public String getCurrentUserId() {
        return firebaseUser.getUid();
    }

    @Override
    public void getUserById(String userID, UserCallback callback) {
        userCollectionRef.document(userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserModel user = task.getResult().toObject(UserModel.class);
                        callback.result(true, user);
                    }
                }).addOnFailureListener(e -> callback.result(false, null));
    }

    @Override
    public void addChildren(FirebaseCallback callback, String newUserId) {
        childrenCollectionRef
                .document(getCurrentUserId())
                .set(new HashMap<String, Boolean>() {{
                    put(newUserId, true);
                }}, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(true, null))
                .addOnFailureListener(e -> callback.onSuccess(false, e));
    }

    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void uploadImageProfile(Uri ImageUri, String uriFileExtension, String userId, UploadCallback callback) {
        if (ImageUri != null) {

            StorageReference storageReference = storage.getReference("Profile/" + userId).child(System.currentTimeMillis() + "." + userId + uriFileExtension);
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
                    callback.onSuccessUpload(true, null, myUri, userId);
                }
            }).addOnFailureListener(e -> callback.onSuccessUpload(false, e, null, userId));

        }

    }

    @Override
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
            }).addOnFailureListener(e -> callback.onSuccessUpload(false, e, null, postId));

        }

    }

    @NonNull
    public String getRandomIdByCollectionName(String collectionName) {
        return db.collection(collectionName).document().getId();
    }

    @Override
    public void searchUsers(String search, List<UserModel> list, SearchCallback callback) {

        ListenerRegistration listener = userCollectionRef.addSnapshotListener((value, error) -> {
            list.clear();
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            if (value != null && !value.isEmpty()) {
                for (DocumentSnapshot snap : value.getDocuments()) {
                    UserModel user = snap.toObject(UserModel.class);
                    if (user != null && user.getUserName().regionMatches(true, 0, search, 0, search.length()))
                        list.add(user);
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
        listenerRegistrations.put("Temporary:search", listener);
    }

    @Override
    public void addFollow(String followUser, final TextView textView) {
        followingCollectionRef
                .document(firebaseUser.getUid())
                .set(new HashMap<String, Object>() {{
                    put(followUser, true);
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> textView.setText(R.string.Following))
                .addOnFailureListener(e -> textView.setText(R.string.Follow));

        followersCollectionRef
                .document(followUser)
                .set(new HashMap<String, Boolean>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> textView.setText(R.string.Following))
                .addOnFailureListener(e -> textView.setText(R.string.Follow));
    }

    @Override
    public void deleteFollow(String followUser, final TextView textView) {
        followingCollectionRef
                .document(firebaseUser.getUid())
                .set(new HashMap<String, Object>() {{
                    put(followUser, FieldValue.delete());
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> textView.setText(R.string.Follow))
                .addOnFailureListener(e -> textView.setText(R.string.Following));

        followersCollectionRef
                .document(followUser)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge())
                .addOnCompleteListener(task -> textView.setText(R.string.Follow))
                .addOnFailureListener(e -> textView.setText(R.string.Following));
    }

    @Override
    public void getAllPostsByUserId(String userId, final List<Task> mTask, final TextView posts, OnChangeCallback callback) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                postCollectionRef
                        .whereEqualTo("author", userId)
                        .addSnapshotListener((value, error) -> {
                            mTask.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    if (Objects.equals(Objects.requireNonNull(doc.getData()).get("permission"), "Private") &&
                                            Objects.equals(doc.getData().get("author"), getCurrentUserId()))
                                        mTask.add(doc.toObject(Task.class));
                                    else if (Objects.equals(doc.getData().get("permission"), "Public"))
                                        mTask.add(doc.toObject(Task.class));
                                }
                                Collections.reverse(mTask);
                                posts.setText(value.size() + "");

                                Log.w(TAG, "document:Post exists.");
                                // The document exists
                            } else {
                                // The document does not exist

                                Log.w(TAG, "document:Post does not exists.");
                            }
                            callback.onChange();
                        });
        listenerRegistrations.put("Temporary:AllPostSnapshot" + userId, listener);
    }

    @Override
    public void addSavedItem(String postId) {
        savedCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Save Item : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Save Item : " + postId + " Failed."));
    }

    @Override
    public void deleteSavedItem(String postId) {
        savedCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Item : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Item : " + postId + " Failed."));
    }

    @Override
    public void takeATaskByUser(String postId, Map<String, Object> data) {
        postCollectionRef
                .document(postId)
                .update(data)
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful())
                                Log.w(TAG, "user" + data + " take : " + postId + " Success.");
                        }
                )
                .addOnFailureListener(e -> Log.w(TAG, "user" + data + " take : " + postId + " Failed."));
    }

    @Override
    public void addLikeToPost(String postId) {
        likesCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), true);
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Like added : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Task add : " + postId + " Failed."));
    }

    @Override
    public void deleteLikeFromPost(String postId) {
        likesCollectionRef
                .document(postId)
                .set(new HashMap<String, Object>() {{
                    put(firebaseUser.getUid(), FieldValue.delete());
                }}, SetOptions.merge()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Like deleted : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Task deleted : " + postId + " Failed."));
    }

    @Override
    public void getLikeByPostId(final TextView likes, String postId) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                likesCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                likes.setText(Objects.requireNonNull(value.getData()).size() + " likes");
                                Log.w(TAG, "document:LikeByPostId exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                likes.setText("0" + " likes");
                                Log.w(TAG, "document:LikeByPostId does not exists.");

                            }
                        });
        listenerRegistrations.put("user:likeSnapShot" + postId, listener);
    }

    @Override
    public void isLiked(final ImageView imageView, String postId) {
        ListenerRegistration listener =
                likesCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                if (Objects.requireNonNull(value.getData()).containsKey(firebaseUser.getUid())) {
                                    imageView.setImageResource(R.drawable.ic_red_like);
                                    imageView.setTag("liked");
                                } else {
                                    imageView.setImageResource(R.drawable.ic_like);
                                    imageView.setTag("like");
                                }
                                Log.w(TAG, "document:Liked exists.");
                                // The document exists

                            } else {
                                imageView.setImageResource(R.drawable.ic_like);
                                imageView.setTag("like");
                                // The document does not exist
                                Log.w(TAG, "document:Liked does not exists.");
                            }
                        });
        listenerRegistrations.put("user:isLikedSnapShot" + postId, listener);
    }

    @Override
    public void isSaved(final ImageView imageView, String postId) {
        ListenerRegistration listener =
                savedCollectionRef.document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                if (Objects.requireNonNull(value.getData()).containsKey(firebaseUser.getUid())) {
                                    imageView.setImageResource(R.drawable.ic_saved);
                                    imageView.setTag("saved");
                                } else {
                                    imageView.setImageResource(R.drawable.ic_save);
                                    imageView.setTag("save");
                                }
                                Log.w(TAG, "document:Save exists.");
                                // The document exists

                            } else {
                                imageView.setImageResource(R.drawable.ic_save);
                                imageView.setTag("save");
                                // The document does not exist
                                Log.w(TAG, "document:Save does not exists.");
                            }


                        });
        listenerRegistrations.put("user:isSavedSnapShot" + postId, listener);
    }

    @Override
    public void addPost(String postId, @NonNull Task task, UploadCallback callback) {
        postCollectionRef
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

    @Override
    public void addNotification(@NonNull Notification notification) {
        notificationCollectionRef
                .document(notification.getNotificationId())
                .set(notification)
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Notification added : " + notification.getNotificationId() + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Notification add : " + notification.getNotificationId() + " Failed."));
    }

    @Override
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
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Post: " + postId + " Failed."));
    }

    @Override
    public void deleteAllLike(String postId) {
        likesCollectionRef
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                                deleteAllComment(postId);
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Post: " + postId + " Failed."));
    }

    @Override
    public void deleteAllComment(String postId) {
        commentsCollectionRef
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                deleteAllSaved(postId);
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Post: " + postId + " Failed."));


    }

    @Override
    public void deleteAllSaved(String postId) {
        savedCollectionRef
                .document(postId)
                .delete()
                .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Log.w(TAG, "Delete Post: " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Post: " + postId + " Failed."));
    }

    @Override
    public void getAllPost(List<Task> mTask, OnChangeCallback callback) {
        ListenerRegistration listener =
                postCollectionRef
                        .whereIn("author", Arrays.asList(getFollowing().keySet().toArray()))
                        .addSnapshotListener((value, error) -> {
                            mTask.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    Task task = doc.toObject(Task.class);
                                    if (task != null) {
                                        if (currentUser.isAdmin()) {
                                            if (task.getPermission().equals("Public") || task.getAuthor().equals(getCurrentUserId()) || children.containsKey(task.getAuthor()))
                                                mTask.add(doc.toObject(Task.class));
                                        } else {
                                            if (task.getPermission().equals("Public")) {
                                                mTask.add(doc.toObject(Task.class));
                                            }
                                        }
                                    }
                                }
                                Collections.sort(mTask, new Comparing.DateCompare());
                                Log.w(TAG, "document:Post exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:Post does not exists.");
                            }
                            callback.onChange();

                        });
        listenerRegistrations.put("user:currentAllPostSnapshot", listener);
    }

    @Override
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
                                Map<String, Object> map = value.getData();
                                assert map != null;
                                for (Object obj : map.values()) {
                                    String commentId = (String) ((Map<?, ?>) obj).get("commentId");
                                    String author = (String) ((Map<?, ?>) obj).get("author");
                                    String comment = (String) ((Map<?, ?>) obj).get("comment");
                                    String publish = (String) ((Map<?, ?>) obj).get("publish");
                                    String create = (String) ((Map<?, ?>) obj).get("create");
                                    Comment commentObj = new Comment(commentId, postId, author, comment, publish, create);
                                    mComments.add(commentObj);
                                }
                                Log.w(TAG, "document:AllComment exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:AllComment does not exists.");

                            }
                            callback.onChange();
                        });
        listenerRegistrations.put("user:getAllCommentSnapShot" + postId, listener);
    }

    @Override
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
                                newComment.setText("");
                                newComment.setEnabled(true);
                                Log.w(TAG, "Comment added : " + postId + " Success.");
                            }
                        }
                ).addOnFailureListener(e -> {
                    Log.w(TAG, "Comment add : " + postId + " Failed.");
                    newComment.setEnabled(true);
                });
    }

    @Override
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
                ).addOnFailureListener(e -> Log.w(TAG, "Task deleted : " + postId + " Failed."));
    }

    @Override
    public void getCommentByPostId(final TextView comments, String postId) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                commentsCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                comments.setVisibility(View.VISIBLE);
                                comments.setText("View All " + Objects.requireNonNull(value.getData()).size() + " Comments");
                                Log.w(TAG, "document:CommentByPostId exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                comments.setVisibility(View.GONE);
                                Log.w(TAG, "document:CommentByPostId does not exists.");

                            }
                        });
        listenerRegistrations.put("user:commentSnapShot" + postId, listener);
    }

    //------------------------------- DestroyData ---------------------------------

    @Override
    public void destroyListenerRegistration() {
        for (ListenerRegistration listenerRegistration : listenerRegistrations.values())
            listenerRegistration.remove();
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void destroyData() {
        this.following.clear();
        this.followers.clear();
        this.children.clear();
        this.listenerRegistrations.clear();

    }

    @Override
    public void destroy() {
        destroyListenerRegistration();
        destroyData();
        single_instance = null;
    }

    @Override
    public void destroyListener(String str) {

        for (Iterator<Map.Entry<String, ListenerRegistration>> it = listenerRegistrations.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, ListenerRegistration> entry = it.next();
            if (entry.getKey().split(":")[0].equals("Temporary" + str)) {
                entry.getValue().remove();
                it.remove();
            }
        }

    }

    // ------------------------------ Getters -------------------------------------

    public Map<String, Object> getFollowing() {
        return following;
    }

    public boolean isMyChild(String profileId) {
        return children.containsKey(profileId);
    }

    @Override
    public void getAllSavedByUserId(String profileId, List<Task> mSave, OnChangeCallback callback) {

        ListenerRegistration mListener = savedCollectionRef
                .whereEqualTo(profileId, true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        List<String> id = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            id.add(doc.getId());
                        }
                        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                                postCollectionRef
                                        .whereIn("taskId", id)
                                        .addSnapshotListener((value1, error1) -> {
                                            mSave.clear();
                                            if (error1 != null) {
                                                Log.w(TAG, "Listen failed.", error1);
                                                return;
                                            }
                                            if (value1 != null && !value1.isEmpty()) {
                                                for (DocumentSnapshot doc : value1.getDocuments()) {
                                                    mSave.add(doc.toObject(Task.class));
                                                }
                                                Collections.reverse(mSave);
                                                Log.w(TAG, "document:Saved exists.");
                                                // The document exists
                                            } else {
                                                // The document does not exist

                                                Log.w(TAG, "document:Saved does not exists.");
                                            }
                                            callback.onChange();
                                        });
                        listenerRegistrations.put("Temporary:getAllSavedSnapshot" + profileId, listener);

                        Log.w(TAG, "document:Saved exists.");
                        // The document exists
                    } else {
                        // The document does not exist

                        Log.w(TAG, "document:Saved does not exists.");
                    }
                });
        listenerRegistrations.put("Temporary:getAllSavedByUserId" + profileId, mListener);
    }

    @Override
    public void getAllTasksByUserId(String profileId, List<Task> mTasks, OnChangeCallback callback) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                postCollectionRef
                        .whereEqualTo("takenByUserId", profileId)
                        .addSnapshotListener((value, error) -> {
                            mTasks.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && !value.isEmpty()) {
                                for (DocumentSnapshot doc : value.getDocuments()) {
                                    mTasks.add(doc.toObject(Task.class));
                                }
                                Collections.reverse(mTasks);

                                Log.w(TAG, "document:Task exists.");
                                // The document exists
                            } else {
                                // The document does not exist

                                Log.w(TAG, "document:Task does not exists.");
                            }
                            callback.onChange();
                        });
        listenerRegistrations.put("Temporary:getAllTasksSnapshot" + profileId, listener);

    }

    @NonNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Data{" +
                "currentUser=" + currentUser +
                ", following=" + following +
                ", followers=" + followers +
                ", children=" + children +
                ", notifications=" + notifications +
                ", listenerRegistrations=" + listenerRegistrations +
                '}';
    }

    @Override
    public void getPostById(String postId, TaskCallback callback) {

        ListenerRegistration listener = postCollectionRef.document(postId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        callback.result(false, null);
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (value != null && value.exists()) {
                        Task post = value.toObject(Task.class);
                        callback.result(true, post);
                        Log.w(TAG, "document:getPostById exists.");
                        // The document exists
                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:getPostById does not exists.");
                    }
                });
        listenerRegistrations.put("Temporary2:getPostById" + postId, listener);

    }
}



