package com.example.tocare.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tocare.R;
import com.example.tocare.DAL.Auth;
import com.example.tocare.BLL.Listener.FirebaseCallback;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class GoogleLoginActivity extends AppCompatActivity implements FirebaseCallback {

    private static final String TAG = "GoogleLoginActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient gsc;
    private Auth login;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        dialog = new ProgressDialog(this);
        AccessToken.setCurrentAccessToken(null);

        login = Auth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            login.handleSignInResult(task,this,getApplicationContext());
        }

    }


    private void signInWithGoogle() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void reload(Class<?> name) {
        Intent intent = new Intent(GoogleLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCallback(boolean success, Exception e, FirebaseUser firebaseUser) {
        if (success) {
            Log.w(TAG, "signInWithGoogle:success");
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        } else {
            dialog.dismiss();
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);

        }
    }

    @Override
    public void onSuccess(boolean success, Exception e) {
        if (success) {
            Log.d(TAG, "DocumentReference::User::success");
            Toast.makeText(this, "The details have been successfully registered " + e, Toast.LENGTH_SHORT).show();
            reload(MainActivity.class);
        } else {
            Log.d(TAG, "DocumentReference::User::failed");
            Toast.makeText(this, "The details were not successfully registered " + e, Toast.LENGTH_SHORT).show();
            reload(LoginActivity.class);
        }
    }


}