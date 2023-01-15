package com.example.tocare.DAL.api;

import android.net.Uri;
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
import com.example.tocare.BLL.Model.Comment;
import com.example.tocare.BLL.Model.Notification;
import com.example.tocare.BLL.Model.Task;
import com.example.tocare.BLL.Model.User;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.FirebaseCallback;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;


public interface IData {
    // ----------------- init ---------------

    /**
     * @param callback this is
     */
    void updateUserUI(Callback callback);
    //----------------- Get -------------------

    /**
     * @param userId    this is
     * @param following this is
     */
    void getAllFollowingByUserId(String userId, final TextView following);

    /**
     * @param userId    this is
     * @param followers this is
     */
    void getAllFollowersByUserId(String userId, final TextView followers);

    /**
     * @param mNotification this is
     * @param callback      this is
     */
    void getAllNotification(List<Notification> mNotification, CallbackNotification callback);

    /**
     * @param mUser    this is
     * @param callback this is
     */
    void getAllChildren(List<User> mUser, OnChangeCallback callback);

    /**
     * @param mUser        this is
     * @param mFollowingId this is
     * @param callback     this is
     */
    void getAllFollowingUser(List<UserModel> mUser, List<String> mFollowingId, OnChangeCallback callback);

    /**
     * @param mUser        this is
     * @param mFollowersId this is
     * @param callback     this is
     */
    void getAllFollowersUser(List<UserModel> mUser, List<String> mFollowersId, OnChangeCallback callback);

    /**
     * @param userId       this is
     * @param mFollowingId this is
     * @param callback     this is
     */
    void getAllFollowingIdByUserId(String userId, List<String> mFollowingId, OnChangeCallback callback);

    /**
     * @param userId       this is
     * @param mFollowersId this is
     * @param callback     this is
     */
    void getAllFollowersIdByUserId(String userId, List<String> mFollowersId, OnChangeCallback callback);

    /**
     * @param profileId this is
     * @param mSave     this is
     * @param callback  this is
     */
    void getAllSavedByUserId(String profileId, List<Task> mSave, OnChangeCallback callback);

    /**
     * @param profileId this is
     * @param mTasks    this is
     * @param callback  this is
     */
    void getAllTasksByUserId(String profileId, List<Task> mTasks, OnChangeCallback callback);

    /**
     * @param comments this is
     * @param postId   this is
     */
    void getCommentByPostId(final TextView comments, String postId);

    /**
     * @param postId   this is
     * @param callback this is
     */
    void getPostById(String postId, TaskCallback callback);

    /**
     * @param userID   this is
     * @param callback this is
     */
    void getUserById(String userID, UserCallback callback);

    /**
     * @param userId   this is
     * @param mTask    this is
     * @param posts    this is
     * @param callback this is
     */
    void getAllPostsByUserId(String userId, final List<Task> mTask, final TextView posts, OnChangeCallback callback);

    /**
     * @param mTask    this is
     * @param callback this is
     */
    void getAllPost(List<Task> mTask, OnChangeCallback callback);

    /**
     * @param mComments this is
     * @param postId    this is
     * @param callback  this is
     */
    void getAllComment(List<Comment> mComments, String postId, OnChangeCallback callback);

    /**
     * @param likes  this is
     * @param postId this is
     */
    void getLikeByPostId(final TextView likes, String postId);
    //----------------- Post ------------------

    /**
     * @param followUser this is
     * @param textView   this is
     */
    void addFollow(String followUser, final TextView textView);

    /**
     * @param postId this is
     */
    void addLikeToPost(String postId);

    /**
     * @param postId this is
     */
    void addSavedItem(String postId);

    /**
     * @param firebaseUser this is
     * @param userModel    this is
     * @param callback     this is
     */
    void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, FirebaseCallback callback);

    /**
     * @param callback  this is
     * @param newUserId this is
     */
    void addChildren(FirebaseCallback callback, String newUserId);

    /**
     * @param postId   this is
     * @param task     this is
     * @param callback this is
     */
    void addPost(String postId, @NonNull Task task, UploadCallback callback);

    /**
     * @param notification this is
     */
    void addNotification(@NonNull Notification notification);


    /**
     * @param postId     this is
     * @param comment    this is
     * @param newComment this is
     */
    void addComment(String postId, @NonNull Comment comment, @NonNull final EditText newComment);


    /**
     * @param ImageUri         this is
     * @param uriFileExtension this is
     * @param userId           this is
     * @param callback         this is
     */
    void uploadImageProfile(Uri ImageUri, String uriFileExtension, String userId, UploadCallback callback);

    /**
     * @param ImageUri         this is
     * @param uriFileExtension this is
     * @param postId           this is
     * @param callback         this is
     */
    void uploadImage(Uri ImageUri, String uriFileExtension, String postId, UploadCallback callback);

    //----------------------------------------------------- Put -----------------------------------------------------------

    /**
     * @param imageView this is
     * @param postId    this is
     */
    void isLiked(final ImageView imageView, String postId);

    /**
     * @param imageView this is
     * @param postId    this is
     */
    void isSaved(final ImageView imageView, String postId);

    /**
     * @param profileId this is
     * @return this is
     */
    boolean isMyChild(String profileId);

    /**
     * @param userId   this is
     * @param user     this is
     * @param callback this is
     */
    void updateUserById(String userId, Map<String, Object> user, OnChangeCallback callback);

    /**
     * @param search   this is
     * @param list     this is
     * @param callback this is
     */
    void searchUsers(String search, List<UserModel> list, SearchCallback callback);


    /**
     * @param postId this is
     * @param data   this is
     */
    void takeATaskByUser(String postId, Map<String, Object> data);

    //----------------- Delete ----------------

    /**
     * @param postId this is
     */
    void deleteLikeFromPost(String postId);

    /**
     * @param postId this is
     */
    void deletePost(String postId);

    /**
     * @param postId this is
     */
    void deleteSavedItem(String postId);

    /**
     * @param postId this is
     */
    void deleteAllLike(String postId);

    /**
     * @param postId this is
     */
    void deleteAllComment(String postId);

    /**
     * @param postId this is
     */
    void deleteAllSaved(String postId);

    /**
     * @param userId this is
     */
    void deleteUserById(String userId);

    /**
     * @param followUser this is
     * @param textView   this is
     */
    void deleteFollow(String followUser, final TextView textView);

    /**
     * @param postId    this is
     * @param commentId this is
     */
    void deleteComment(String postId, String commentId);

    //------------------------------- DestroyData ---------------------------------

    /**
     *
     */
    void destroyListenerRegistration();

    /**
     *
     */
    void destroyData();

    /**
     *
     */
    void destroy();

    /**
     * @param str this is
     */
    void destroyListener(String str);


}



