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
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public interface IAuth {

    void signInWithEmail(String email, String password, FirebaseLoginCallback callback);

    void createAccountWithEmail(String email, String password, UserModel userModel, Callback callback);

    void handleSignInResult(OAuthProvider.Builder provider, Activity activity, Callback callback,UserModel userModel);

    void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTsk, Callback callback, Context context);

    void startPhoneNumberVerification(String phoneNumber, Activity activity, PhoneCallback callback);

    void verifyPhoneNumberWithCode(String code, String mVerificationId, PhoneCallback callback);

    void signInWithAuthCredential(PhoneAuthCredential credential, PhoneCallback phoneCallback);

    void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token, Activity activity);
}
