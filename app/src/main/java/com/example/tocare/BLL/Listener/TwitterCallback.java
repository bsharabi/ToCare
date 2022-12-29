package com.example.tocare.BLL.Listener;

import com.google.firebase.auth.FirebaseUser;

public interface TwitterCallback extends Callback {
    void onCallback(boolean success, Exception e, FirebaseUser user);
    void onSuccess(boolean success, Exception e);
    void onComplete(boolean success,Exception e );
}
