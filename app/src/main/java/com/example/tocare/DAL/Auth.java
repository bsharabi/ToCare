package com.example.tocare.DAL;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tocare.BLL.Model.Admin;
import com.example.tocare.BLL.Model.UserModel;
import com.example.tocare.BLL.Listener.Callback;
import com.example.tocare.BLL.Listener.FirebaseLoginCallback;
import com.example.tocare.DAL.api.IAuth;
import com.example.tocare.BLL.Listener.PhoneCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Auth implements IAuth {

    private static Auth single_instance = null;
    private static final String TAG = "LoginUser";
    private PhoneCallback phoneCallback;


    private final FirebaseAuth mAuth;
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

    private Auth() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public static Auth getInstance() {
        if (single_instance == null)
            single_instance = new Auth();
        return single_instance;
    }

    @Override
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

    @Override
    public void signInWithAuthCredential(PhoneAuthCredential credential, PhoneCallback phoneCallback) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                            phoneCallback.onCallback(false, new Exception("not registered"), task);
                            Objects.requireNonNull(task.getResult().getUser()).delete();
                        } else
                            phoneCallback.onCallback(true, null, task);
                    }
                }).addOnFailureListener(failure -> phoneCallback.onCallback(false, failure, null));
    }

    @Override
    public void verifyPhoneNumberWithCode(String code, String mVerificationId, PhoneCallback callback) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithAuthCredential(credential, callback);
    }

    @Override
    public void signInWithEmail(String email, String password, FirebaseLoginCallback callback) {
        mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(Objects.requireNonNull(task.getResult().getUser()).isEmailVerified())
                            callback.onSuccess(true, null,task.getResult().getCredential());
                        else {
                            task.getResult().getUser().sendEmailVerification();
                            callback.onSuccess(false, new Exception("not confirm"),null);
                        }
                    }
                }).addOnFailureListener(e -> callback.onSuccess(false, e,null));
    }

    @Override
    public void createAccountWithEmail(String email, String password, UserModel userModel, Callback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(task.getResult().getUser()).sendEmailVerification();
                        Log.d(TAG, "createUserWithEmail:success");

                        FirebaseUser user = task.getResult().getUser();
                        userModel.setId(user.getUid());
                        callback.onCallback(true, null, user);
                        createUserData(user, userModel, callback);
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "createUserWithEmail:failed");
                    callback.onCallback(false, e, null);

                });
    }

    @Override
    public void handleSignInResult(OAuthProvider.Builder provider, Activity activity, Callback callback, UserModel userModel) {

        Log.w(TAG, "handleSignInResult:get Pending Auth Result");
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> {
                                Log.w(TAG, "signInWithTwitter:success");
                                if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                                    userModel.setId(Objects.requireNonNull(authResult.getUser()).getUid());
                                    createUserData(authResult.getUser(), userModel, callback);
                                } else
                                    callback.onCallback(true, null, authResult.getUser());
                            })
                    .addOnFailureListener(
                            e -> {
                                callback.onCallback(false, e, null);
                                Log.w(TAG, "signInWithTwitter:failed", e);
                            });
        } else {
            mAuth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(
                            authResult -> {
                                callback.onCallback(true, null, authResult.getUser());
                                Log.w(TAG, "signInWithTwitter:success");
                                if (Objects.requireNonNull(authResult.getAdditionalUserInfo()).isNewUser()) {
                                    userModel.setId(Objects.requireNonNull(authResult.getUser()).getUid());
                                    createUserData(authResult.getUser(), userModel, callback);
                                }
                            })
                    .addOnFailureListener(
                            e -> {
                                callback.onCallback(false, e, null);
                                Log.w(TAG, "signInWithTwitter:failed", e);
                            });
        }

    }

    @Override
    public void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTsk, Callback callback, Context context) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount acc = completedTsk.getResult(ApiException.class);
            FirebaseGoogleAuth(acc, callback, context);

        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "signInWithGoogle:failed", e);
            callback.onCallback(false, e, null);
        }
    }

    @Override
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

    private void FirebaseGoogleAuth(@NonNull GoogleSignInAccount acc, Callback callback, Context context) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.w(TAG, "signInWithGoogle:success");
                if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                    buildGoogleData(task.getResult().getUser(), callback, context);
                } else {
                    callback.onCallback(true, null, null);
                }
            }
        }).addOnFailureListener(e -> {
            callback.onCallback(false, e, null);
            Log.w(TAG, "signInWithGoogle:failed", e);
        });
    }

    public void createUserData(@NonNull FirebaseUser firebaseUser, UserModel userModel, Callback callback) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(firebaseUser.getUid());
        reference.set(userModel)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Log.d(TAG, "DocumentReference::User::success");
                        callback.onSuccess(true, null);
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "DocumentReference::User::failed");
                    callback.onSuccess(false, e);
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();
                });
    }

    private void buildGoogleData(FirebaseUser firebaseUser, Callback callback, Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            UserModel userModel = new Admin(
                    firebaseUser.getUid(),
                    account.getDisplayName(),
                    account.getGivenName(),
                    account.getFamilyName(),
                    "",
                    "Hello my name is " + account.getDisplayName(),
                    "https://firebasestorage.googleapis.com/v0/b/tocare-5b2eb.appspot.com/o/placeHolder.png?alt=media&token=fa1fa6f4-233e-4375-84cd-e7cdd6260c7a",
                    true);
            createUserData(firebaseUser, userModel, callback);
        }
    }

    public void sendPasswordResetEmail(String email, Callback callback) {

        mAuth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(true, null);
                        Log.d(TAG, "sendPasswordResetEmail:success");
                    }
                }).addOnFailureListener(e -> {
                    callback.onSuccess(false, e);
                    Log.d(TAG, "sendPasswordResetEmail:failed");
                });


    }
}
