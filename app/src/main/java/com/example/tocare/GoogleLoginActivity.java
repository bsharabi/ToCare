package com.example.tocare;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class GoogleLoginActivity extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private static final int RC_SIGN_IN = 1;
    private ProgressDialog dialog;
    private static final String TAG = "GoogleLoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        dialog = new ProgressDialog(this);
        AccessToken.setCurrentAccessToken(null);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Google");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        signInWithGoogle();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
//         Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTsk) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount acc = completedTsk.getResult(ApiException.class);
            Log.w(TAG, "signInWithGoogle:success");
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);

        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "signInWithGoogle:failed", e);
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(@NonNull GoogleSignInAccount acc) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.w(TAG, "signInWithGoogle:success");
                Toast.makeText(GoogleLoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                updateGoogleData(task.getResult().getUser());

                reload(MainActivity.class);
            }
        }).addOnFailureListener(this, e -> {
            Toast.makeText(GoogleLoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "signInWithGoogle:failed", e);
        });
    }

    private void updateGoogleData(FirebaseUser firebaseUser) {
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
        Intent intent = new Intent(GoogleLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}