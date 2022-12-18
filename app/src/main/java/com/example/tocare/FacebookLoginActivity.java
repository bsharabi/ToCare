package com.example.tocare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class FacebookLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private static final String TAG = "FacebookLoginActivity";
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        dialog = new ProgressDialog(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        callbackManager = CallbackManager.Factory.create();

        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Facebook");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AccessToken.setCurrentAccessToken(null);
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        signInWithFacebook();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "facebook:onError", exception);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    private void handleFacebookAccessToken(@NonNull AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        dialog.dismiss();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        reload(MainActivity.class);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(failure -> {


                });
    }

    private void updateFacebookData(FirebaseUser firebaseUser) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
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
            DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                    .document(firebaseUser.getUid());
            reference.set(userModel)
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            dialog.dismiss();
                            Log.d(TAG, "DocumentReference:success");
                            Toast.makeText(this, "The details have been successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Log.d(TAG, "DocumentReference:failed");
                        Toast.makeText(this, "The details were not successfully registered " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(FacebookLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}