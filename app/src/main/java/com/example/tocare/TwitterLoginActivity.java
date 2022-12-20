package com.example.tocare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tocare.BLL.Departments.Admin;
import com.example.tocare.BLL.Departments.UserModel;
import com.example.tocare.databinding.ActivityTwitterLoginBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class TwitterLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private static final String TAG = "TwitterLoginActivity";
    private ActivityTwitterLoginBinding binding;
    private ImageView imTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwitterLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        imTwitter = binding.imTwitter;

        imTwitter.setTranslationY(300);

        final float alpha = 0;

        imTwitter.setAlpha(alpha);

        imTwitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();

        dialog.setMessage("Please wait..");
        dialog.setTitle("Login with Twitter");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        SignInWithTwitter();


    }

    private void SignInWithTwitter() {
        Log.w(TAG, "signInWithTwitter:create OAuth Provider");
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("lang", "en");
        handleSignInResult(provider);
    }

    private void handleSignInResult(OAuthProvider.Builder provider) {
        Log.w(TAG, "handleSignInResult:get Pending Auth Result");
        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();

        if (pendingResultTask != null) {

            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> {
                                System.out.println(authResult.getAdditionalUserInfo().isNewUser());
                                System.out.println(authResult.getAdditionalUserInfo().getProfile());

                                dialog.dismiss();
                                Log.w(TAG, "signInWithTwitter:success");
                                Toast.makeText(this, "Successfully", Toast.LENGTH_LONG);
                                if (authResult.getAdditionalUserInfo().isNewUser())
                                    buildTwitterData(authResult.getUser());
                                reload(MainActivity.class);
                            })
                    .addOnFailureListener(
                            e -> {
                                Log.w(TAG, "signInWithTwitter:failed", e);
                                dialog.dismiss();
                                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG);
                                reload(LoginActivity.class);
                            });
        } else {
            mAuth
                    .startActivityForSignInWithProvider(/* activity= */ TwitterLoginActivity.this, provider.build())
                    .addOnSuccessListener(
                            authResult -> {
                                Log.w(TAG, "signInWithTwitter:success");
                                Toast.makeText(this, "Successfully", Toast.LENGTH_LONG);
                                dialog.dismiss();
                                if (authResult.getAdditionalUserInfo().isNewUser())
                                    buildTwitterData(authResult.getUser());
                                reload(MainActivity.class);
                            })
                    .addOnFailureListener(

                            e -> {
                                Log.w(TAG, "signInWithTwitter:failed", e);
                                dialog.dismiss();
                                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG);
                                reload(LoginActivity.class);
                            });
        }


    }

    private void buildTwitterData(FirebaseUser firebaseUser) {
        Object account = null;
        if (account != null) {
            UserModel userModel = new Admin(
                    firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    "",
                    "",
                    "",
                    "Hello my name is " + firebaseUser.getDisplayName(),
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
        Intent intent = new Intent(TwitterLoginActivity.this, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}