package com.example.tocare.DAL;

import android.annotation.SuppressLint;
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
import com.example.tocare.BLL.Model.Comment;

import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.example.tocare.DAL.api.IData;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.example.tocare.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Data implements IData {

    private static Data single_instance = null;
    private static final String TAG = "DataUser";
    private static AuthCredential credential;
    private static PhoneCallback phoneCallback;

    private UserModel currentUser;

    private Map<String, Object> following;
    private Map<String, Object> followers;
    private Map<String, Object> children;
    private Map<String, Object> saved;
    private List<Object> posts;
    private List<Object> tasks;

    private final Map<String, ListenerRegistration> listenerRegistrations;

    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final FirebaseUser firebaseUser;
    private final FirebaseAuth mAuth;
    private final CollectionReference userCollectionRef;
    private final CollectionReference commentsCollectionRef;
    private final CollectionReference followingCollectionRef;
    private final CollectionReference followersCollectionRef;
    private final CollectionReference childrenCollectionRef;
    private final CollectionReference postCollectionRef;
    private final CollectionReference savedCollectionRef;
    private final CollectionReference likesCollectionRef;


    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            phoneCallback.onVerificationCompleted(true, credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            phoneCallback.onVerificationFailed(e);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(verificationId, token);
            phoneCallback.onCodeSent(verificationId, token);
        }
    };

    private Data() {
        this.following = new HashMap<>();
        this.followers = new HashMap<>();
        this.children = new HashMap<>();
        this.saved = new HashMap<>();
        this.posts = new ArrayList<>();
        this.tasks = new ArrayList<>();

        this.listenerRegistrations = new HashMap<>();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        userCollectionRef = db.collection("User");
        followingCollectionRef = db.collection("Following");
        followersCollectionRef = db.collection("Followers");
        postCollectionRef = db.collection("Posts");
        childrenCollectionRef = db.collection("Children");
        savedCollectionRef = db.collection("Saved");
        commentsCollectionRef = db.collection("Comments");
        likesCollectionRef = db.collection("Likes");

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

                    }
                }).addOnFailureListener(e -> callback.onCallback(false, e, null));
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> phoneCallback.onCallback(true, null, task))
                .addOnFailureListener(failure -> phoneCallback.onCallback(false, failure, null));
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
        getAllFollowingSnapShot();
        getAllFollowersSnapShot();
        getAllChildrenIdSnapShot();
        getAllFollowing(callback);
        ListenerRegistration listener = userCollectionRef
                .document(firebaseUser.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        callback.onCallback(false, error, null);
                        return;
                    }
                    if (value != null && value.exists()) {
                        currentUser = value.toObject(UserModel.class);
                        callback.onCallback(true, null, firebaseUser);
                        Log.d(TAG, "DocumentReferenceCurrentUser:success");
                        Log.w(TAG, "document:updateUserUI exists.");
                        // The document exists

                    } else {
                        // The document does not exist
                        Log.w(TAG, "document:updateUserUI does not exists.");
                    }
                });
        listenerRegistrations.put("currentUserSnapshot", listener);
    }

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

    @Override
    public UserModel getCurrentUser() {
        return currentUser;
    }

    @NonNull
    public String getCurrentUserId() {
        return firebaseUser.getUid();
    }

    public void getFollowingByUserId(String userId, FollowingCallback callback) {
        followingCollectionRef
                .document(userId)
                .collection("following")
                .get()
                .addOnCompleteListener(task -> {
                    List<String> list = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot snap : task.getResult().getDocuments()) {
                            list.add(snap.getId());
                        }
                        callback.result(true, list);

                    }
                })
                .addOnFailureListener(e -> callback.result(false, null));
    }

    public void getUserById(String userID, UserCallback callback) {
        userCollectionRef.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserModel user = task.getResult().toObject(UserModel.class);
                callback.result(true, user);
            }
        }).addOnFailureListener(e -> callback.result(false, null));
    }

    public void addChildren(FirebaseCallback callback, String newUserId) {

        childrenCollectionRef.document(getCurrentUserId())
                .set(new HashMap<String, Boolean>() {{
                    put(newUserId, true);
                }}, SetOptions.merge())
                .addOnSuccessListener(aVoid -> callback.onSuccess(true, null))
                .addOnFailureListener(e -> callback.onSuccess(false, e));
    }

    public void signInWithCredential(Callback callback) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        callback.onSuccess(true, null);
                }).addOnFailureListener(e ->
                        callback.onSuccess(true, e)
                );
    }

    @Override
    public void deleteUserById(String userId) {

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
            }).addOnFailureListener(e -> callback.onSuccessUpload(false, e, null, postId));

        }

    }

    @NonNull
    public String getRandomIdByCollectionName(String collectionName) {
        return db.collection(collectionName).document().getId();
    }

    public void searchUsersAdmin(String search, List<UserModel> list, SearchCallback callback) {
        Query query = db.collection("User")
                .whereEqualTo("admin", false)
                .orderBy("userName")
                .startAt(search)
                .endAt(search + "\uf8ff");
        query.addSnapshotListener((value, error) -> {
            if (value != null) {
                for (DocumentSnapshot snap : value.getDocuments()) {
                    list.add(snap.toObject(UserModel.class));
                }
                callback.result();
            }
        });
    }

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

    public void getAllFollowing(Callback callback) {
        followingCollectionRef
                .document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
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

    public void getAllFollowersSnapShot() {
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
        listenerRegistrations.put("currentFollowersSnapShot", listener);
    }

    public void getAllFollowingSnapShot() {
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
        listenerRegistrations.put("currentFollowingSnapShot", listener);
    }

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

    public void getAllFollowingByUserId(String userId, final List<UserModel> mFollowing, final TextView following) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followingCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {
                            mFollowing.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                following.setText(value.getData().size() + "");
                                Log.w(TAG, "document:FollowingUser exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:FollowingUser does not exists.");
                            }

                        });
        listenerRegistrations.put("getAllFollowingByUserId" + userId, listener);


    }

    public void getAllFollowersByUserId(String userId, final List<UserModel> mFollowers, final TextView followers) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                followersCollectionRef
                        .document(userId)
                        .addSnapshotListener((value, error) -> {
                            mFollowers.clear();
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists() && value.getData() != null) {
                                followers.setText(value.getData().size() + "");
                                Log.w(TAG, "document:FollowersUser exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:FollowersUser does not exists.");
                            }

                        });
        listenerRegistrations.put("getAllFollowingByUserId" + userId, listener);
    }

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
        listenerRegistrations.put("AllPostSnapshot" + userId, listener);
    }

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
                ).addOnFailureListener(e -> Log.w(TAG, "Delete Item : " + postId + " Failed."));
    }

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

    public void addLikeToTask(String postId) {
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
        listenerRegistrations.put("likeSnapShot" + postId, listener);
    }

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
                                // The document does not exist
                                Log.w(TAG, "document:Liked does not exists.");
                            }
                        });
        listenerRegistrations.put("isLikedSnapShot" + postId, listener);
    }

    public void isSaved(final ImageView imageView, String postId) {
        ListenerRegistration listener = db.collection("Saved").document(postId)
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
                        // The document does not exist
                        Log.w(TAG, "document:Save does not exists.");
                    }


                });
        listenerRegistrations.put("isSavedSnapShot" + postId, listener);
    }

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
                                    mTask.add(doc.toObject(Task.class));
                                }
                                Collections.sort(mTask, new DateCompare());
                                Log.w(TAG, "document:Post exists.");
                                // The document exists
                            } else {
                                // The document does not exist
                                Log.w(TAG, "document:Post does not exists.");
                            }
                            callback.onChange();

                        });
        listenerRegistrations.put("currentAllPostSnapshot", listener);
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
                                Map<String, Object> map = value.getData();
                                assert map != null;
                                for (Object obj : map.values()) {
                                    String commentId = (String) ((Map<?, ?>) obj).get("commentId");
                                    String author = (String) ((Map<?, ?>) obj).get("author");
                                    String comment = (String) ((Map<?, ?>) obj).get("comment");
                                    String publish = (String) ((Map<?, ?>) obj).get("publish");
                                    Comment commentObj = new Comment(commentId, postId, author, comment, publish);
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
        listenerRegistrations.put("getAllCommentSnapShot" + postId, listener);
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
                ).addOnFailureListener(e -> Log.w(TAG, "Task deleted : " + postId + " Failed."));
    }

    public void getCommentByPostId(final TextView likes, String postId) {
        @SuppressLint("SetTextI18n") ListenerRegistration listener =
                commentsCollectionRef
                        .document(postId)
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                                return;
                            }
                            if (value != null && value.exists()) {
                                likes.setText("View All " + Objects.requireNonNull(value.getData()).size() + " Comments");
                                Log.w(TAG, "document:CommentByPostId exists.");
                                // The document exists

                            } else {
                                // The document does not exist
                                likes.setText("0" + " Comments");
                                Log.w(TAG, "document:CommentByPostId does not exists.");

                            }
                        });
        listenerRegistrations.put("commentSnapShot" + postId, listener);
    }

    public void getAllChildrenIdSnapShot() {
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
        listenerRegistrations.put("currentAllChildrenIdSnapShot", listener);

    }

    public void getAllChildren(List<UserModel> mUser, OnChangeCallback callback) {

        ListenerRegistration listener =
                userCollectionRef
                        .whereEqualTo("parentId", getCurrentUserId())
//                        .whereIn("id", Arrays.asList( children.keySet().toArray()))
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
        listenerRegistrations.put("currentGetAllChildrenSnapShot", listener);

    }

    //------------------------------- DestroyData ---------------------------------

    @Override
    public void destroyListenerRegistration() {
        for (ListenerRegistration listenerRegistration : listenerRegistrations.values())
            listenerRegistration.remove();
        FirebaseAuth.getInstance().signOut();
    }

    public void destroyData() {
        this.following.clear();
        this.followers.clear();
        this.children.clear();
        this.saved.clear();
        this.posts.clear();
        this.tasks.clear();
        this.listenerRegistrations.clear();

    }

    public void destroy() {
        destroyListenerRegistration();
        destroyData();
        single_instance = null;
    }


    // ------------------------------ Getters -------------------------------------

    public Map<String, Object> getFollowing() {
        return following;
    }

    public Map<String, Object> getFollowers() {
        return followers;
    }

    public boolean isMyChild(String profileId) {
        return children.containsKey(profileId);
    }

    public void getAllSavedByUserId(String profileId, List<Task> mSave, OnChangeCallback callback) {

        savedCollectionRef
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
                        listenerRegistrations.put("getAllSavedSnapshot" + profileId, listener);

                        Log.w(TAG, "document:Saved exists.");
                        // The document exists
                    } else {
                        // The document does not exist

                        Log.w(TAG, "document:Saved does not exists.");
                    }
                });
    }

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
        listenerRegistrations.put("getAllTasksSnapshot" + profileId, listener);

    }
}

//Class to compare Movies by name
class DateCompare implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        return t1.getCreated().compareTo(t2.getCreated());
    }
}

