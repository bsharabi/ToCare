package com.example.tocare.BLL.Listener;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public interface PhoneCallback {
    void onCallback(boolean success, Exception e, Task<AuthResult> task);

    void onVerificationFailed(FirebaseException e);

    void onVerificationCompleted(boolean success, PhoneAuthCredential credential);

    void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token);
}
