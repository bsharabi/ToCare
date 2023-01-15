package com.example.tocare.DAL.api;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.BLL.Listener.FirebaseLoginCallback;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public interface IAuth {

    /**
     * @param email    this is
     * @param password this is
     */
    void createCredentialSignIn(String email, String password);

    /**
     * @param callback this is
     */
    void signInWithCredential(Callback callback);

    /**
     * @param firebaseUser this is
     * @param userModel    this is
     * @param callback     this is
     */
    void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, Callback callback);

    /**
     * @param email    this is
     * @param callback this is
     */
    void sendPasswordResetEmail(String email, Callback callback);

    /**
     * @param email    this is
     * @param password this is
     * @param callback this is
     */
    void signInWithEmail(String email, String password, FirebaseLoginCallback callback);

    /**
     * @param email     this is
     * @param password  this is
     * @param userModel this is
     * @param callback  this is
     */
    void createAccountWithEmail(String email, String password, UserModel userModel, Callback callback);

    /**
     * @param provider  this is
     * @param activity  this is
     * @param callback  this is
     * @param userModel this is
     */
    void handleSignInResult(OAuthProvider.Builder provider, Activity activity, Callback callback, UserModel userModel);

    /**
     * @param completedTsk this is
     * @param callback     this is
     * @param context      this is
     */
    void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTsk, Callback callback, Context context);

    /**
     * @param phoneNumber this is
     * @param activity    this is
     * @param callback    this is
     */
    void startPhoneNumberVerification(String phoneNumber, Activity activity, PhoneCallback callback);

    /**
     * @param code            this is
     * @param mVerificationId this is
     * @param callback        this is
     */
    void verifyPhoneNumberWithCode(String code, String mVerificationId, PhoneCallback callback);

    /**
     * @param credential    this is
     * @param phoneCallback this is
     */
    void signInWithAuthCredential(PhoneAuthCredential credential, PhoneCallback phoneCallback);

    /**
     * @param phoneNumber this is
     * @param token       this is
     * @param activity    this is
     */
    void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token, Activity activity);
}
