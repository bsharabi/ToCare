package com.example.tocare.BLL.Listener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface FirebaseLoginCallback {
    void onSuccess(boolean success, Exception e, Task<AuthResult> task);
}
